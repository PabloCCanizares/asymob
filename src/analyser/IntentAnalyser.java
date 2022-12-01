package analyser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import auxiliar.Common;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Language;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.PromptLanguage;
import generator.RegexInput;
import generator.Token;
import generator.TrainingPhrase;

public class IntentAnalyser {

	private final Language CONST_LANGUAGE = Language.ENGLISH;
	private Conversor converter;
	public IntentAnalyser(Conversor converter) {
		this.converter = converter;
	}

	public IntentAnalyser() {

		converter =null;
	}

	/**
	 * Search the Tokens of an Intent, which have an specific Parameter in parameterReferenceToken.
	 * @param intentIn: Intent where the search is performed.
	 * @param paramIn: Parameter used to match the search.
	 * @return
	 */
	public LinkedList<Token> searchTokensByParam(Intent intentIn, Parameter paramIn)
	{
		List<IntentInput> inputList;
		List<Token> tokenList, tokenFilteredList;
		LinkedList<Token> tokenRet;
		
		tokenRet = null;
		try
		{
			//Initialise the return list
			tokenRet = new LinkedList<Token>();
			
			//Check if the intent input is null
			if(intentIn == null)
				throw new Exception("The input intent is empty");
			
			//Extract the input list
			inputList = extractAllInputs(intentIn);
			
			//Check if the inputlist is null
			if(inputList == null)
				throw new Exception("The input list is empty");
			
			for(IntentInput intent: inputList)
			{
				tokenList = extractTokensFromInput(intent);
				
				tokenFilteredList = filterTokensByParam(tokenList, paramIn);
				
				if(tokenFilteredList != null)
					tokenRet.addAll(tokenFilteredList);
			}
		}
		catch(Exception e)
		{
			tokenRet = null;
			System.out.println("[searchTokensByParam] Exception while searching: "+e.getMessage());
		}
		return tokenRet;
	}

	private List<Token> filterTokensByParam(List<Token> tokenList, Parameter paramIn) {
		
		List<Token> tokenRet;
		ParameterReferenceToken paramRef;
		
		tokenRet = null;
		if(tokenList != null && paramIn != null)
		{
			
			for(Token tokIn: tokenList)
			{
				if (tokIn instanceof ParameterReferenceToken)
				{
					paramRef = (ParameterReferenceToken) tokIn;
							
					if(tokIn != null && paramIn == paramRef.getParameter())
					{
						if(tokenRet == null)
							tokenRet = new LinkedList<Token>();
						
						tokenRet.add(tokIn);
					}
				}
			}
		}
		
		return tokenRet;
	}

	private EList<Token> extractTokensFromInput(IntentInput inputIn) {
		EList<Token>  retList;
		TrainingPhrase trainIn;
		
		retList = null;
		if(inputIn != null)
		{
			//Dynamically check if are training phrases [TrainingPhrase] 
			if (inputIn instanceof TrainingPhrase) {
				trainIn = (TrainingPhrase) inputIn;

				retList = trainIn.getTokens();
			}
		}
			
		return retList;
	}

	private LinkedList<IntentInput> extractAllInputs(Intent intentIn) {
		List<IntentLanguageInputs> listLanguages;
		LinkedList<IntentInput> retList;
		EList<IntentInput> inputList;
		
		retList = null;
		listLanguages = null;
		
		if(intentIn != null)
		{
			//Analyse the different languages
			listLanguages = intentIn.getInputs();
			
			retList = new LinkedList<IntentInput>();
			
			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null)
				{
					inputList = intentLan.getInputs();

					//Find all the inputs and process them
					if(inputList != null)
					{						
						for (IntentInput input : inputList) {
							
							retList.add(input);
						}
					}			
				}
			}
		}
		
		return retList;
	}
	
	LinkedList<IntentInput> extractAllInputs(Intent intentIn, Language lanIn) {
		List<IntentLanguageInputs> listLanguages;
		LinkedList<IntentInput> retList;
		EList<IntentInput> inputList;
		
		retList = null;
		listLanguages = null;
		
		if(intentIn != null)
		{
			//Analyse the different languages
			listLanguages = intentIn.getInputs();
			
			retList = new LinkedList<IntentInput>();
			
			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null && intentLan.getLanguage() == lanIn)
				{
					inputList = intentLan.getInputs();

					//Find all the inputs and process them
					if(inputList != null)
					{						
						for (IntentInput input : inputList) {
							
							retList.add(input);
						}
					}			
				}
			}
		}
		
		return retList;
	}
	
	public LinkedList<LinkedList<String>> getAllPhrases(Intent intentIn)
	{
		List<IntentLanguageInputs> listLanguages;
		EList<IntentInput> inputList;
		LinkedList<LinkedList<String>> stringList;
		LinkedList<String> listAux;
		
		listLanguages = null;
		stringList = new LinkedList<LinkedList<String>>();
		
		if(intentIn != null)
		{
			//Analyse the different languages
			listLanguages = intentIn.getInputs();
			
			for (IntentLanguageInputs intentLan : listLanguages) {
				if(intentLan != null)
				{
					listAux = new LinkedList<String>();
					inputList = intentLan.getInputs();

					//Find all the inputs and process them
					if(inputList != null)
					{						
						for (IntentInput input : inputList) {
							
							listAux = extractStringTrainingPhrasesFromIntent(intentIn);
						}
					}
					stringList.add(listAux);
				}
			}
		}
		
		return stringList;
	}
	
	public int getTotalPhrases(Intent intentIn)
	{
		LinkedList<IntentInput>  retList;
		int nRet;

		nRet = 0;
		if(intentIn != null)
		{
			retList = extractAllInputs(intentIn);
			
			if(retList != null)
				nRet = retList.size();
		}
		
		return nRet;
	}

	public int getTotalWords(Intent intentIn) {
		LinkedList<String>  retList, auxList;
		int nRet;

		nRet = 0;
		if(intentIn != null)
		{
			retList = extractStringTrainingPhrasesFromIntent(intentIn);
			
			nRet = getTotalWords(retList);
		}
		
		return nRet;
	}

	public int getTotalWords(LinkedList<String> retList) {
		LinkedList<String> auxList;
		int nRet;
		
		nRet = 0;
		if(retList != null)
		{
			for(String strIn: retList)
			{
				//Split in parts
				//TODO: Tener cuidado con las comas y caracteres
				auxList = Common.SplitUsingTokenizer(strIn);
				
				//Count the words
				if(auxList != null)
					nRet +=auxList.size();
			}
		}
		return nRet;
	}
	
	public LinkedList<String> extractStringTrainingPhrasesFromIntent(Intent intentIn)
	{
		LinkedList<IntentInput>  intentList;
		LinkedList<String> retList;
		List<Token> tokList;
		String strToken;
		TokenAnalyser tokenAnalyser;
		int nRet;

		nRet = 0;
		retList = null;
		if(intentIn != null)
		{
			tokenAnalyser = new TokenAnalyser();
			intentList = extractAllInputs(intentIn);
			
			if(intentList != null)
			{
				retList = new LinkedList<String>();
				for(IntentInput inIn: intentList)
				{
					tokList = extractTokensFromInput(inIn);
					
					strToken = tokenAnalyser.convertListToString(tokList);
					if(strToken != null)
						retList.add(strToken);
				}
			}
		}
		return retList;
	}

	public int getTotalChars(Intent intentIn) {
		LinkedList<String>  retList, auxList;
		int nRet;

		nRet = 0;
		if(intentIn != null)
		{
			retList = extractStringTrainingPhrasesFromIntent(intentIn);
			
			nRet = getTotalCharsFromList(retList);
		}
		
		return nRet;
	}

	public int getTotalCharsFromList(LinkedList<String> retList) {
		int nRet;
		LinkedList<String> auxList;
		
		nRet = 0;
		if(retList != null)
		{
			for(String strIn: retList)
			{
				//Split in parts
				//TODO: Tener cuidado con las comas y caracteres
				auxList = Common.SplitUsingTokenizer(strIn);
				
				//Count the words
				if(auxList != null)
				{
					for(String strAux: auxList)
					{
						nRet +=strAux.length();
					}
				}
			}
		}
		return nRet;
	}
	public int getMaxWordLen(Intent intentIn) {
		LinkedList<String>  retList, auxList;
		int nRet, nMax;

		nRet = nMax = 0;
		if(intentIn != null)
		{
			retList = extractStringTrainingPhrasesFromIntent(intentIn);
			
			if(retList != null)
			{
				for(String strIn: retList)
				{
					//Split in parts
					//TODO: Tener cuidado con las comas y caracteres
					auxList = Common.SplitUsingTokenizer(strIn);
					
					//Count the words
					if(auxList != null)
					{
						for(String strAux: auxList)
						{
							nMax = Math.max(nMax, strAux.length());							
						}
					}
				}
			}
		}
		
		return nMax;
	}

	public int getTotalWordsFromList(LinkedList<String> phrasesList) {
		LinkedList<String>  retList, auxList;
		int nRet, nMax;

		nRet = 0;
		if(phrasesList != null)
		{
			for(String strIn: phrasesList)
			{
				//Split in parts
				//TODO: Tener cuidado con las comas y caracteres
				auxList = Common.SplitUsingTokenizer(strIn);
				
				//Count the words
				if(auxList != null)
				{
					nRet+= auxList.size();
				}
			}
		}
		
		return nRet;
	}

	public int getTotalRegularExp(Intent intentIn) { 
		int nRet;
		LinkedList<IntentInput> intentList;
		
		nRet = 0;
		if(intentIn != null)
		{
			intentList = extractAllInputs(intentIn);
			
			if(intentList != null)
			{
				for(IntentInput inIn: intentList)
				{
					
					if(inIn instanceof RegexInput)
						nRet++;
				}
			}
		}
		return nRet;
	}

	public LinkedList<String> extractAllIntentParameterPrompts(Intent intentIn) {
		LinkedList<String> retList;
		LinkedList<Parameter> paramList;

		try {
		
			retList = new LinkedList<String>();
			for(Parameter param: intentIn.getParameters())
			{
				for(PromptLanguage prompt: param.getPrompts())
				{
					for(String strPrompt: prompt.getPrompts())
					{
						retList.add(strPrompt);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Exception catched while extracting parameters prompts");
			retList = null;
		}

		return retList;
	}
	
	public List<Parameter>  extractParameters(Intent intentIn) {
		
		return intentIn.getParameters();
	}
	
	public int getNumParametersFromIntentInput(IntentInput inputIn)
	{
		int nRet;
		EList<Token> tokList;
		
		nRet = 0;
		tokList = extractTokensFromInput(inputIn);
		
		for(Token tok: tokList)
		{
			if(tok instanceof ParameterToken)
				nRet++;
			else if (tok instanceof ParameterReferenceToken)
				nRet++;
		}
		return nRet;
	}
	public LinkedList<Parameter> getRequiredParameters(Intent intentIn)
	{
		LinkedList<Parameter> paramRet;
		
		try
		{
			paramRet = new LinkedList<Parameter>();
			for(Parameter param: intentIn.getParameters())
			{
				if(param.isRequired())
					paramRet.add(param);
			}
		}
		catch (Exception e)
		{
			paramRet = null;
		}
		
		return paramRet;
	}
	public LinkedList<Parameter> getParametersFromIntentInput(IntentInput inputIn)
	{
		int nRet;
		EList<Token> tokList;
		LinkedList<Parameter> paramRet;
		
		nRet = 0;
		tokList = extractTokensFromInput(inputIn);
		paramRet = new LinkedList<Parameter>();
		try
		{
			for(Token tok: tokList)
			{
				if(tok instanceof ParameterToken)
					paramRet.add(((ParameterToken) tok).getParameter());
				else if (tok instanceof ParameterReferenceToken)
					paramRet.add(((ParameterReferenceToken) tok).getParameter());
			}
		}catch(Exception e)
		{
			paramRet=null;
			System.out.println("Exception catched while extracting parameters from input");
		}


		return paramRet;
	}
	
}
