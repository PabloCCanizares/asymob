package analyser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.databind.util.Converter;

import aux.Common;
import generator.Action;
import generator.BotInteraction;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;
import generator.UserInteraction;

public class FlowAnalyser {

	IntentAnalyser intentAnalyser;
	InputAnalyser inputAnalyser;
	TokenAnalyser tokenAnalyser; 
	
	public FlowAnalyser()
	{
		intentAnalyser = new IntentAnalyser();
		inputAnalyser = new InputAnalyser();
		tokenAnalyser = new TokenAnalyser();
	}
	public FlowAnalyser(Conversor converter) {
		intentAnalyser = new IntentAnalyser(converter);
		inputAnalyser = new InputAnalyser(converter);
		tokenAnalyser = new TokenAnalyser(converter);
	}
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

	private LinkedList<String> handleTextAction(Action actionIn, boolean bRef) {
		Text actionText;
		EList<TextLanguageInput> textLanInputList;
		EList<TextInput> textInputList;
		LinkedList<String> retList, auxList;
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
							if(!bRef)
								auxList =extractPhrasesFromTextAction(textIn);
							else
								auxList = extractPhrasesFromTextActionByRef(textIn);
							retList.addAll(auxList);
						}
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
		LinkedList<String> retList, currentPhrase;		
		Map<ParameterToken, LinkedList<String>> paramPhrasesMap;
		LinkedList<String> composedPhrasesList;
		String strText;
		boolean bIsComposedPhrase;

		//Initialise
		retList = null;
		paramPhrasesMap = null;
		bIsComposedPhrase = false;

		//TODO: Aplanar aqui, y gestionar excepciones.
		if(textIn != null)
		{
			retList = new LinkedList<String>();
			tokenList = textIn.getTokens();

			//If the token list size is greater than 1, it means that the phrase may be composed.
			//BUT It is possible (but rare) to have a single element.
			//If the phrase is composed, it is neccesary to create combinations of the parameters whether it have different values.
			bIsComposedPhrase = checkComposedPhrase(textIn);
			if(!bIsComposedPhrase)
			{
				for(Token tokIndex: tokenList)
				{
					strText = tokenAnalyser.getTokenText(tokIndex, false);
					retList.add(strText);
				}
			}
			else
			{
				//create the Map
				paramPhrasesMap = createParamPhrasesMap(textIn);

				//compose the phrases using the map
				if(paramPhrasesMap != null)
				{
					composedPhrasesList = new LinkedList<String>();
					currentPhrase = new LinkedList<String>();
					composePhrases(0, tokenList, paramPhrasesMap, currentPhrase,composedPhrasesList);
					
					retList = composedPhrasesList;
				}
			}
		}
		return retList;
	}

	private void composePhrases(int nIndex, EList<Token> tokenList, Map<ParameterToken, LinkedList<String>> paramPhrasesMap, LinkedList<String> currentPhrase,
			LinkedList<String> composedPhrasesList) {
		Token tokenIn;
		String strTokenText, strText;
		Literal litIn;
		ParameterToken paramRefIn;
		LinkedList<String> nestedStringList;
		strTokenText = null;
			
		//Not null elements
		if(nIndex >=0 && tokenList != null && paramPhrasesMap != null && composedPhrasesList != null && currentPhrase != null)
		{
			if(nIndex<tokenList.size())
			{
				tokenIn = tokenList.get(nIndex);
				
				if(tokenIn != null)
				{
					if (tokenIn instanceof Literal) 
					{
						//process as literal
						litIn = (Literal) tokenIn;
						
						if(litIn != null)
						{
							strText = litIn.getText();
							
							Common.addOrReplaceToken(currentPhrase, nIndex, strText);
							
							//Recursive call, nIndex+1
							composePhrases(nIndex+1, tokenList, paramPhrasesMap, currentPhrase, composedPhrasesList);
							
							//If is the last element in mutedtraining phrase, store in the return list
							//But it is necessary to create a copy
							if(nIndex+1 == tokenList.size())
								Common.addCopyToRetList(composedPhrasesList, currentPhrase, "");
						}
					}
					else if(tokenIn instanceof ParameterToken)
					{
						paramRefIn = (ParameterToken) tokenIn;
						
						nestedStringList = paramPhrasesMap.get(paramRefIn);
							
						if(nestedStringList != null)
						{
							for(String strPhrase: nestedStringList)
							{
								Common.addOrReplaceToken(currentPhrase, nIndex, strPhrase);
								
								//Recursive call, nIndex+1
								composePhrases(nIndex+1, tokenList, paramPhrasesMap, currentPhrase, composedPhrasesList);
								
								//If is the last element in mutedtraining phrase, store in the return list
								//But it is necessary to create a copy
								if(nIndex+1 == tokenList.size())
									Common.addCopyToRetList(composedPhrasesList, currentPhrase, "");
							}
						}
					}
				}
			}
		}
		
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
						
						//and insert them in the map
						if(phrasesList != null && phrasesList.size()>0)
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

			if(tokenList != null)
			{
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
				}while(tokIndex != null && !bRet && nIndex < tokenList.size());
			}
		}

		return bRet;
	}
	//Given a ParameterToken -> return back a list of possible elements that can be included in a phrase.
	LinkedList<String> extractTextFragmentsFromParameter(ParameterToken paramToken)
	{
		Intent currentIntent;
		LinkedList<String>  retListAux, retList;
		LinkedList<Token> tokenList;
		retList = null;
		
		//Get the intent container of the parameter
		currentIntent = tokenAnalyser.getIntentContainer(paramToken);
		
		//Check if we have associated a current intent, for searching on it.
		if(currentIntent != null)
		{
			tokenList = intentAnalyser.searchTokensByParam(currentIntent,paramToken.getParameter());
			retList = inputAnalyser.extractStringsFromTokenList(tokenList);
			
			retList = new LinkedList<>(new HashSet<>(retList));
		}
		
		return retList;
	}
	private LinkedList<String> extractPhrasesFromTextActionByRef(TextInput textIn) {
		EList<Token> tokenList;
		String strText;
		LinkedList<String> retList;
		
		retList= null;
		if(textIn != null)
		{
			retList = new LinkedList<String>();
			tokenList = textIn.getTokens();
			for(Token tokIndex: tokenList)
			{
				strText = tokenAnalyser.getTokenText(tokIndex, true);
				retList.add(strText);
			}
		}
		
		return retList;
	}
	public int getTotalEdges(UserInteraction userActIn)
	{
		BotInteraction botInteraction;
		EList<UserInteraction> userActionList;
		int nRet, nSize, nSizeAux;
		
		nRet = 0;
		if(userActIn !=null)
		{
			nRet = 1;
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				userActionList = botInteraction.getOutcoming();
				if(botInteraction.getOutcoming() != null && botInteraction.getOutcoming().size()>0)
				{
					nSizeAux = nSize = 0;
					//Here we have another intent with a action list
					for(UserInteraction userAct: userActionList)
					{
						nSizeAux = getTotalEdges(userAct);
						nRet += nSizeAux;
					}
				}
			}
		}
		return nRet;
	}
	public int getTotalActions(UserInteraction userActIn) {
		BotInteraction botInteraction;
		EList<UserInteraction> userActionList;
		int nRet, nSize, nSizeAux;
		
		nRet = 0;
		if(userActIn !=null)
		{
			
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				if(botInteraction.getActions() != null)
					nRet = botInteraction.getActions().size();
				
				userActionList = botInteraction.getOutcoming();
				if(botInteraction.getOutcoming() != null && botInteraction.getOutcoming().size()>0)
				{
					nSizeAux = nSize = 0;
					//Here we have another intent with a action list
					for(UserInteraction userAct: userActionList)
					{
						nSizeAux = getTotalActions(userAct);
						nRet += nSizeAux;
					}
				}
			}
		}
		return nRet;
	}

}
