package transformation.dialogflow;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import analyser.BotAnalyser;
import analyser.Conversor;
import analyser.TokenAnalyser;
import aux.BotResourcesManager;
import aux.Common;
import generator.Action;
import generator.Bot;
import generator.DefaultEntity;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;
import generator.TrainingPhrase;
import generator.UserInteraction;
import transformation.ITransformation;
import transformation.dialogflow.agent.intents.Data;
import transformation.dialogflow.agent.intents.Message;
import transformation.dialogflow.agent.intents.Response;
import zipUtils.Zip;

public class BotToAgent implements ITransformation{

	Conversor conversor;
	private BotAnalyser botAnalyser;
	private TokenAnalyser tokAnalyser;
	private final String LAN_ENGLISH = "ENGLISH";
	private final String TRAIN_PHRASES_FILE_TAG= "_usersays_";
	private final String JSON_DOT_TAG = ".json";
	private Zip zip;
	
	String strOutputPath;
	
	public BotToAgent() {
		conversor = new ConversorDialogFlow();
		tokAnalyser = new TokenAnalyser(conversor);
		botAnalyser = new BotAnalyser(conversor);		
	}
	@Override
	public boolean transform(Bot botIn, String strOutputPath) {
		boolean bRet;
		
		bRet =  false;
		
		this.strOutputPath = strOutputPath;
		
		//Agent: se mantiene. No tenemos forma de gestionarlo despues de procesarlo en Conga.
		//Package: se mantiene
		//
		//Los cambios vienen en los intents
		//Intent: respuestas+message?+affectedContexts?action?
		//Training phrases: fichero usersays con formato -> Fácil [Pero hay aspectos como @sysingore]
		
		//Intents: Training phrases
		exportTrainingPhrases(botIn.getIntents());
		
		//Actions: Bot responses
		exportFlows(botIn.getFlows());
		
		//TODO: Entities + Entity entries
			
		
		//Zip del archivo.
		//doZip();
		return bRet;
	}


	private void exportFlows(EList<UserInteraction> flows) {
		List<Pair<Intent, Action>> indexList;
		HashMap<Intent,List<Action>> intentActionMap;
		Intent intent;
		List<Action> actionList;

		if(flows != null)
		{
			intentActionMap = new HashMap<Intent,List<Action>>();
			//Group the user interactions in a map
			for(UserInteraction userIn: flows)
			{
				indexList = botAnalyser.plainActionTree(userIn);
				processIntentListIntoMap(intentActionMap, indexList);
			}
			
			//Now, for each intent and its associated list of actions: we export it!
			Iterator<?> iterator = intentActionMap.entrySet().iterator();
	        while (iterator.hasNext()) {
	          Map.Entry me2 = (Map.Entry) iterator.next();
	          System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
	          intent = (Intent)me2.getKey();
	          actionList = (List<Action>) me2.getValue();
	          
	          exportIntent(intent, actionList);
	        } 
		}
		
	}
	private void exportIntent(Intent intent, List<Action> actionList) {
		
		EList<IntentInput> inputList;
		String strName, strLan, jsonString;
		List<Response> jsonList;
		ObjectMapper mapper;
		transformation.dialogflow.agent.intents.Intent intentJSON;
		UUID intentId;
		
		try
		{
			//TODO: Here its necessary to check if we have previously loaded the Chabot
			//In this case, we try to re-use all the data included, instead of creating new data.
			intentJSON = new transformation.dialogflow.agent.intents.Intent();
			
			//Name
			strName = intent.getName();
			intentJSON.setName(strName);
			
			//ID
			intentId = Common.generateType1UUID();
			intentJSON.setId(intentId.toString());
			
			//Contexts
			//TODO: Rellenar
			
			//Responses
			jsonList = createResponseList(intent, actionList);
			intentJSON.setResponses(jsonList);
			strName = strName+JSON_DOT_TAG;
			
			//transform to JSON
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
		    //Converting the Object to JSONString			
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(intentJSON);
			
			exportData(strName, jsonString);
		}
		catch(Exception e)
		{
			System.out.println("Exception while exporting intent");
		}
	}
	private List<Response> createResponseList(Intent intent, List<Action> actionList) {
		List<Response> resList;
		List<Message> messageList;
		List<String> speech, textList;
		Response response;
		Message message;
		
		response = new Response();
		message = new Message();
		resList = new LinkedList<Response>();
		messageList = new LinkedList<Message>();
		speech = new LinkedList<String>();

		//reset context
		
		//action
		
		//affectedContexts
		
		//Parameters!
		
		//Messages
		for(Action action: actionList)
		{
			if(action != null)
			{
				textList = botAnalyser.extractAllActionPhrasesByRef(action);
				speech.addAll(textList);
			}
		}
		//Add speech to the message
		message.setSpeech(speech);
		//Message to lists
		messageList.add(message);
		//Message list to response
		response.setMessages(messageList);
		resList.add(response);
		
		return resList;
	}
	private void processIntentListIntoMap(HashMap<Intent, List<Action>> intentActionMap,
			List<Pair<Intent, Action>> indexList) {
		List<Action> actionList;
		Action action;
		Intent intent;
		
		action = null;
		intent = null;
		actionList = null;
		if(intentActionMap!= null && indexList != null)
		{
			for(Pair<Intent, Action> pairIntentAction: indexList)
			{				
				if (pairIntentAction != null)
				{
					intent = (Intent) pairIntentAction.getLeft();
					action = (Action) pairIntentAction.getRight();
				}
				
				if(intent!= null && action != null)
				{
					if(action instanceof Text)
					{
						actionList = null;
						//TODO: Danger! Temporally filtered by textimpl
						if(intentActionMap.containsKey(intent))
						{
							actionList = intentActionMap.get(intent);
						}
						if(actionList==null)
							actionList = new LinkedList<Action>();
						
						actionList.add(action);
						intentActionMap.put(intent, actionList);
					}
				}
			}
		}
	}
	//Nombre: Nombre_intent + '_usersays_' + lan [Default Fallback Intent_usersays_en.json]
	private void exportTrainingPhrases(EList<Intent> intentsList) 
	{
		if(intentsList != null)
		{
			for(Intent intent: intentsList)
			{
				exportIntent(intent);
			}
		}
	}

	private void exportIntent(Intent intentIn) {
		List<IntentLanguageInputs> listLanguages;
		String strIntenName;
		
		if(intentIn != null)
		{
			strIntenName = intentIn.getName();
			//Analyse the different languages
			listLanguages = intentIn.getInputs();
			
			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null)
				{
					exportIntentLanguage(strIntenName, intentLan);			
				}
			}
		}
	}

	private void exportIntentLanguage(String strIntenName, IntentLanguageInputs intentLan) {
		EList<IntentInput> inputList;
		String strName, strLan, jsonString;
		List<transformation.dialogflow.agent.intents.TrainingPhrase> jsonList;
		ObjectMapper mapper;
		try
		{
			//ID
			inputList = intentLan.getInputs();
			strName = intentLan.getLanguage().getName();
			
			if(strName.equals(LAN_ENGLISH))
				strLan = "en";
			else
				strLan = "unk";
			
			jsonList = transformInputListoToJSON(inputList, strLan);
			
			strName = strIntenName+TRAIN_PHRASES_FILE_TAG+strLan+JSON_DOT_TAG;
			
			//transform to JSON
			mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
		    //Converting the Object to JSONString			
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonList);
			
			exportData(strName, jsonString);
		}
		catch(Exception e)
		{
			System.out.println("Exception while exporting intent");
		}
	}
	private void exportData(String strName, String jsonString) {
		File fileToSave;
		BufferedWriter writer;
		
		if(strName != null && jsonString!=null)
		{
			fileToSave = Common.fileWithDirectoryAssurance(strOutputPath, strName);
			
			try {
				writer = new BufferedWriter(new FileWriter(fileToSave));
				writer.write(jsonString);
				writer.flush();		
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("[exportData] Error exporting file: "+strName);
			}
		}
	}
	//Cada TrainingPhrase tiene:
	/*"id": "c20be499-5dd0-475f-bedd-a895f76f3aec",
		    "data": [
		      {
		        "text": "tell me some jokes",
		        "userDefined": false
		      }
		    ],
		    "isTemplate": false,
		    "count": 0,
		    "lang": "en",
		    "updated": 0
		  },
	 */
	private List<transformation.dialogflow.agent.intents.TrainingPhrase> transformInputListoToJSON(EList<IntentInput> inputList, String strLan) {
		TrainingPhrase trainPhrase;
		UUID trainingId;
		Data tokenJson;
		transformation.dialogflow.agent.intents.TrainingPhrase trainingJSON;
		List<Data> jsonList;
		List<transformation.dialogflow.agent.intents.TrainingPhrase> retList;
		
		retList = new LinkedList<transformation.dialogflow.agent.intents.TrainingPhrase>();
		//Find all the inputs and process them
		for (IntentInput input : inputList) 
		{
			jsonList = new LinkedList<Data>();
			trainingJSON = new transformation.dialogflow.agent.intents.TrainingPhrase();
			if(input instanceof TrainingPhrase)
			{
				trainPhrase = (TrainingPhrase) input;
				
				//Generate UUID and assign it to the objetct
				trainingId = Common.generateType1UUID();
				trainingJSON.setId(trainingId.toString());
				
				//Process the Tokens
				for(Token tokIn: trainPhrase.getTokens())
				{
					tokenJson = transformTokenJson(tokIn);
					jsonList.add(tokenJson);
				}
				trainingJSON.setData(jsonList);
				trainingJSON.setTemplate(false);
				trainingJSON.setCount(0);
				trainingJSON.setLang(strLan);
				trainingJSON.setUpdated(0);
				
				retList.add(trainingJSON);
			}
		}
		
		return retList;
	}


	private Data transformTokenJson(Token tokIn) {
		ParameterReferenceToken paramRefIn;
		Parameter param;
		String strText, strName, strMeta;
		Literal litIn;
		Data tokenJson;
		
		tokenJson = new Data();
		strText=strMeta=strText=strName=null;
		
		if(tokIn != null)
		{
			if (tokIn instanceof Literal) 
			{
				//process as literal
				litIn = (Literal) tokIn;
				
				if(litIn != null)
					strText = litIn.getText();
				
				tokenJson.setText(strText);
				tokenJson.setUserDefined(false);
			}
			else if(tokIn instanceof ParameterReferenceToken)
			{
				paramRefIn = (ParameterReferenceToken) tokIn;
				
				if(paramRefIn != null)
				{
					strText = paramRefIn.getTextReference();
					param = paramRefIn.getParameter();
					
					if(param != null)
					{
						strName = param.getName();
						if(param.getEntity() != null)
							strMeta = param.getEntity().getName();
					}
						
					if(conversor!=null)
					{
						strMeta = conversor.convertReferenceToAgent(strName);
					}
					tokenJson.setText(strText);
					tokenJson.setAlias(strName);
					tokenJson.setMeta(strMeta);
					tokenJson.setUserDefined(true);
				}
			}
			
			
		}
		
		return tokenJson;
	}
	
	@Override
	public boolean transform(String strPathIn, String strPathOut) {
		boolean bRet;
		BotResourcesManager botManager;
		Bot bot;
		
		bRet = false;
		if(strPathIn != null)
		{
			botManager = new BotResourcesManager();
			if(botManager.loadChatbot(strPathIn))
			{
				bot = botManager.getCurrentBot();
				bRet = transform(bot, strPathOut);
			}
		}
		return bRet;
	}/*
	public LinkedList<String> extractAllActionPhrases(Action actionIn, boolean bRef) {

		LinkedList<String> retList;

		retList = null;

		if(actionIn != null)
		{
			//text
			if (actionIn instanceof Text)
			{								
				retList = handleTextAction(actionIn, bRef);
			}
			//image
			else if (actionIn instanceof Image)
			{
				handleImageAction(actionIn);
			}
			//HttpRequest
			else if(actionIn instanceof HTTPRequest)
			{
				//TODO: Ver como enfocarlo y terminar esta parte
			}
			//HttpResponse
			else if(actionIn instanceof HTTPResponse)
			{
				//TODO: Ver como enfocarlo y terminar esta parte

			}
		}
		return retList;
	}

	private void handleImageAction(Action actionIn) {
		// TODO Auto-generated method stub

	}

	private LinkedList<Data> handleTextAction(Action actionIn, boolean bRef) {
		Text actionText;
		EList<TextLanguageInput> textLanInputList;
		EList<TextInput> textInputList;
		LinkedList<Data> retList, auxList;
		actionText = (Text) actionIn;

		retList = null;
		if(actionIn != null)
		{
			textLanInputList = actionText.getInputs();

			if(textLanInputList != null)
			{
				retList = new LinkedList<String>();
				for(TextLanguageInput textLanIn: textLanInputList)
				{
					textInputList = textLanIn.getInputs();
					if(textInputList != null)
					{
						for(TextInput textIn: textInputList)
						{
							auxList = extractPhrasesFromTextActionByRef(textIn);
							retList.addAll(auxList);
						}
					}
				}	
			}
		}

		return retList;
	}	*/
	//TODO: darle la vuelta a los terminos y utilizar
	/*
	private Language reverseGetLanguage(String language) {
		switch (language) {
		case :
			return "en"Language.ENGLISH;
		case "es":
			return Language.SPANISH;
		case "da":
			return Language.DANISH;
		case "de":
			return Language.GERMAN;
		case "fr":
			return Language.FRENCH;
		case "hi":
			return Language.HINDI;
		case "id":
			return Language.INDONESIAN;
		case "it":
			return Language.ITALIAN;
		case "ja":
			return Language.JAPANESE;
		case "ko":
			return Language.KOREAN;
		case "nl":
			return Language.DUTCH;
		case "no":
			return Language.NORWEGIAN;
		case "pl":
			return Language.POLISH;
		case "pt":
			return Language.PORTUGUESE;
		case "ru":
			return Language.RUSIAN;
		case "sv":
			return Language.SWEDISH;
		case "th":
			return Language.THAI;
		case "tr":
			return Language.TURKISH;
		case "uk":
			return Language.UKRANIAN;
		case "zh":
			return Language.CHINESE;
		default:
			return Language.ENGLISH;
		}
	}*/

	
}
