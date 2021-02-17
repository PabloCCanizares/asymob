package transformation;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import aux.BotResourcesManager;
import core.Asymob;
import generator.Bot;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.TrainingPhrase;
import transformation.dialogflow.TrainingPhraseJSON;

public class Transform_Dialogflow implements ITransformation{

	String strOutputPath;
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
		
		if(intentIn != null)
		{
			//Analyse the different languages
			listLanguages = intentIn.getInputs();
			
			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null)
				{
					exportIntentLanguage(intentLan);			
				}
			}
		}
	}


	private void exportIntentLanguage(IntentLanguageInputs intentLan) {
		EList<IntentInput> inputList;
		TrainingPhrase trainPhrase;
		String strName;
		TrainingPhraseJSON trainingJSON;
		try
		{
			inputList = intentLan.getInputs();
			strName = intentLan.getLanguage().getName();
			//Find all the inputs and process them
			for (IntentInput input : inputList) 
			{
				
				if(input instanceof TrainingPhrase)
				{
					trainPhrase = (TrainingPhrase)input;
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
				
				//Cada frase tiene:
				/*
				 * {
			        "text": "tell me some jokes",
			        "userDefined": false
			       }
				 */
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception while exporting intent");
		}
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

}
