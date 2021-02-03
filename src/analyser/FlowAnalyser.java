package analyser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import generator.Action;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.Literal;
import generator.ParameterToken;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;

public class FlowAnalyser {

	Intent currentIntent;
	
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
		Map<ParameterToken, LinkedList<String>> paramPhrasesMap;
		boolean bIsComposedPhrase;

		//Initialise
		retList = null;
		paramPhrasesMap = null;
		bIsComposedPhrase = false;

		if(textIn != null)
		{
			retList = new LinkedList<String>();
			tokenList = textIn.getTokens();

			//If the token list size is greater than 1, it means that the phrase is composed.
			//BUT It is possible (but rare) to have a single element.
			//So, it is neccesary to create combinations of the parameters if it have different values.

			bIsComposedPhrase = checkComposedPhrase(textIn);
			if(!bIsComposedPhrase)
			{
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
					}
				}
			}
			else
			{
				//create the Map
				paramPhrasesMap = createParamPhrasesMap(textIn);

				//compose the phrases using the map
				if(paramPhrasesMap != null)
				{

				}
			}
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

						bIsComposedPhrase = true;
					}
				}
			}
		}
		return retList;
	}

	private Map<ParameterToken, LinkedList<String>> createParamPhrasesMap(TextInput textIn) {

		Map<ParameterToken, LinkedList<String>> retMap;
		EList<Token> tokenList;
		LinkedList<String> phrasesList;
		ParameterToken paramToken;
		int nIndex;

		nIndex = 0;
		retMap = null;
		phrasesList = null;

		if(textIn != null)
		{
			tokenList = textIn.getTokens();
			retMap = new HashMap<ParameterToken, LinkedList<String>>();
			for(Token tokIndex: tokenList)
			{
				tokIndex = tokenList.get(nIndex);
				if(tokIndex != null)
				{
					if(tokIndex instanceof ParameterToken)
					{
						//extract the elements
						paramToken = (ParameterToken) tokIndex;

						phrasesList = extractTextFragmentsFromParameter(paramToken);
						//and inser them in the map
						retMap.put(paramToken, phrasesList);
					}
				}
				nIndex++;
			}
		}

		return retMap;
	}

	private boolean checkComposedPhrase(TextInput textIn) {
		boolean bRet;
		EList<Token> tokenList;
		Token tokIndex;
		int nIndex;

		nIndex = 0;
		bRet = false;
		if(textIn != null)
		{
			tokenList = textIn.getTokens();

			do
			{
				tokIndex = tokenList.get(nIndex);
				if(tokIndex != null)
				{
					if(tokIndex instanceof ParameterToken)
					{
						bRet = true;
					}
				}
				nIndex++;
			}while(tokIndex != null && !bRet);
		}

		return bRet;
	}
	//Given a ParameterToken -> return back a list of possible elements that can be included in a phrase.
	LinkedList<String> extractTextFragmentsFromParameter(ParameterToken paramToken)
	{
		IntentAnalyser intentAnalyser;
		LinkedList<String>  retList;
		
		retList = null;
		//Check if we have associated a current intent, for searching on it.
		if(currentIntent != null)
		{
			intentAnalyser = new IntentAnalyser();
			ya tienes los datos sacados, = intentAnalyser.searchTokensByParam(currentIntent,paramToken.getParameter());
			
			gestionarlos, sacarlos en String y devolverlos.
		}
		
		return retList;
	}

	public void configureIntent(Intent intent) {
		currentIntent = intent;
	}
}
