package analyser;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Parameter;
import generator.ParameterReferenceToken;
import generator.Token;
import generator.TrainingPhrase;

public class IntentAnalyser {

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
}
