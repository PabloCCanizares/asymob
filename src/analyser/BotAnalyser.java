package analyser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import generator.Action;
import generator.Bot;
import generator.BotInteraction;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;
import generator.TrainingPhrase;
import generator.UserInteraction;

public class BotAnalyser {

	
	private void extractAllIntentInputs(Bot botIn)
	{
		List<Intent> listIntent;
		List<IntentLanguageInputs> listLanguages;
		if(botIn != null)
		{
			//
			listIntent =  botIn.getIntents();
			
			for (Intent intent : listIntent) {
				listLanguages = intent.getInputs();
			}
		}
	}
	
	public LinkedList<String> extractAllIntentPhrases(Intent intentIn)
	{
		LinkedList<String> retList;
		EList<IntentLanguageInputs> listLanguages;
		EList<IntentInput> inputList;
		TrainingPhrase trainIn;
		EList<Token> tokenList;

		//TODO: Refactor y bien
		
		retList = null;
		if(intentIn != null)
		{
			retList = new LinkedList<String>();
			//Analyse the different languages
			listLanguages = intentIn.getInputs();

			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null)
				{
					//System.out.println("extractAllPhrases - Analysing intent in language "+intentLan.getLanguage().getLiteral());
					inputList = intentLan.getInputs();

					//Find all the inputs and process them
					if(inputList != null)
					{
						for (IntentInput input : inputList) {
							
							if (input instanceof TrainingPhrase) {
								trainIn = (TrainingPhrase) input;

								if(trainIn != null)
								{
									tokenList = trainIn.getTokens();
									
									String strPhrase;
									
									strPhrase = "";
									for(Token tokIn: tokenList)
									{
										strPhrase+=getTokenText(tokIn);
										
									}
									if(!strPhrase.isEmpty() && !strPhrase.isBlank())
										retList.add(strPhrase);
								}
							}
						}
					}			
				}
			}
		}	
		
		return retList;
	}
	
	private String getTokenText(Token token) {
		String strText;
		Literal litIn;
		ParameterReferenceToken paramRefIn;
		
		//Initially, the returning string is null
		strText = null;
		
		if(token != null)
		{
			if (token instanceof Literal) 
			{
				//process as literal
				litIn = (Literal) token;
				
				if(litIn != null)
					strText = litIn.getText();
			}
			else if(token instanceof ParameterReferenceToken)
			{
				paramRefIn = (ParameterReferenceToken) token;
				
				if(paramRefIn != null)
					strText = 	paramRefIn.getTextReference();
			}
		}

		return strText;
	}

	public LinkedList<String> extractAllActionPhrases(Action actionIn) {
		
		LinkedList<String> retList;
		
		retList = null;
		
		if(actionIn != null)
		{
			//text
			if (actionIn instanceof Text)
			{								
				retList = handleTextAction(actionIn);
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

	private LinkedList<String> handleTextAction(Action actionIn) {
		Text actionText;
		EList<TextLanguageInput> textLanInputList;
		EList<TextInput> textInputList;
		LinkedList<String> retList, auxList;
		actionText = (Text) actionIn;
		
		retList = null;
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
						auxList =extractPhrasesFromTextAction(textIn);
						retList.addAll(auxList);
					}
				}
			}	
		}
		return retList;
	}

	//Aqui viene lo complejo. Hay que tener en cuenta todas las frases complejas generadas previamente
	//para poder añadirlas como opciones de respuesta del bot.
	private LinkedList<String> extractPhrasesFromTextAction(TextInput textIn) {
		EList<Token> tokenList;
		LinkedList<String> retList;
		Literal lit;
		ParameterToken paramToken;
		
		retList = null;
		if(textIn != null)
		{
			retList = new LinkedList<String>();
			tokenList = textIn.getTokens();
			
			//If the token list size is greater than 1, it means that the phrase is composed.
			//So, it is neccesary to create combinations of the parameters if it have different values.
			for(Token tokIndex: tokenList)
			{
				if(tokIndex != null)
				{
					//Check the type of the token
					if(tokIndex instanceof Literal)
					{
						lit = (Literal) tokIndex;
						
						if(lit != null)
							retList.add(lit.getText());
					}
					else if(tokIndex instanceof ParameterToken)
					{
						paramToken = (ParameterToken) tokIndex;
						paramToken.getParameter();
					}
				}
			}
		}
		return retList;
	}

	public EList<Action> extractActionList(UserInteraction userActIn) {
		EList<Action> retList;
		BotInteraction botInteraction;
		
		retList = null;
		if(userActIn !=null)
		{
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
				retList = botInteraction.getActions();
				
		}
		return retList;
	}
}
