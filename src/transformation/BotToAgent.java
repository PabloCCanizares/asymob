package transformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import analyser.TokenAnalyser;
import aux.BotResourcesManager;
import aux.Common;
import generator.Bot;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Token;
import generator.TrainingPhrase;
import transformation.dialogflow.agent.intents.Data;

public class BotToAgent implements ITransformation{

	private TokenAnalyser tokAnalyser;
	private final String LAN_ENGLISH = "ENGLISH";
	private final String TRAIN_PHRASES_FILE_TAG= "_usersays_";
	private final String JSON_DOT_TAG = ".json";
	String strOutputPath;
	
	public BotToAgent() {
		tokAnalyser = new TokenAnalyser();
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
		exportIntents(botIn.getIntents());
		//Actions: Bot responses
		//exportActions(botIn.getActions());
		
		//*Crear los intents al uso, y desenrollar los actions, que ya toca
		
		//Nombre: Nombre_intent + '_usersays_' + lan [Default Fallback Intent_usersays_en.json]
		return bRet;
	}


	private void exportIntents(EList<Intent> intentsList) 
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
			jsonString = mapper.writeValueAsString(jsonList);
			
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
		String strTokenText, strTokenParameter;
		Data tokenJson;
		
		tokenJson = new Data();
		
		strTokenText = tokAnalyser.getTokenText(tokIn);
		strTokenParameter = tokAnalyser.getParameterToken(tokIn);

		if(strTokenParameter != null)
			strTokenParameter = "@"+strTokenParameter;
		
		tokenJson.setText(strTokenText);
		tokenJson.setMeta(strTokenParameter);
		tokenJson.setUserDefined(false);
		
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
	}
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
