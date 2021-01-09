package core;

import java.util.LinkedList;
import java.util.List;

import generator.Bot;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.Token;
import generator.TrainingPhrase;
import main.MutationOperatorSet;
import utteranceVariantCore.UtteranceVariantCore;

public class TrainPhraseGenerator {

	UtteranceVariantCore oMutCore;
	LinkedList<String> tempTrainingPhrases;
	public TrainPhraseGenerator()
	{
		oMutCore =  new UtteranceVariantCore();
	}
	boolean generateTrainingPhraseFull(Bot botIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		bRet = false;
		
		//Iterate the intents and call the existing method
		if(botIn != null)
		{
			
		}
		return bRet;
	}
	boolean generateTrainingPhraseFull(Intent intentIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		List<IntentLanguageInputs> listLanguages;
		List<IntentInput> inputList;
		bRet = true;
		
		System.out.println("generateTrainingPhraseFull - Init");
		try
		{
			//Find all the inputs and process them
			if(intentIn != null)
			{
				//Analyse the different languages
				listLanguages = intentIn.getInputs();
				
				for (IntentLanguageInputs intentLan : listLanguages) {
					
					if(intentLan != null)
					{
						System.out.println("generateTrainingPhraseFull - Analysing intent in language "+intentLan.getLanguage().getLiteral());
						inputList = intentLan.getInputs();
						
						//Find all the inputs and process them
						if(inputList != null)
						{
							for (IntentInput input : inputList) {
								createTrainingPhrase(input, cfgIn);
							}
						}			
					}
				}
			}			
		}
		catch(Exception e)
		{
			System.out.println("generateTrainingPhraseFull - Exception generating train phrases: "+e.getMessage());

			bRet = false;
		}

		System.out.println("generateTrainingPhraseFull - End");
		return bRet;
	}
	/**
	 * Generates a set of training phrases taking into consideration the provided cfg
	 * @param inputIn: Input used as base to generate the new set of phrases
	 * @param cfgIn
	 * @return
	 */
	boolean createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn)
	{
		TrainingPhrase trainIn;
		List<Token> tokenList;
		Literal lit;
		LinkedList<String> mutedTrainingPhrases, partialResults;
		boolean bRet;
		
		bRet = false;
		if(inputIn != null)
		{
			mutedTrainingPhrases = new LinkedList<String>();
			
			//Dynamically check if are training phrases
			if (inputIn instanceof TrainingPhrase) {
				trainIn = (TrainingPhrase) inputIn;
				
				tokenList = trainIn.getTokens();
				
				//Check if the token is literal or composed 
				for(Token tokenIn: tokenList)
				{
					if(generateTrainingPhrase(tokenIn, cfgIn))
					{
						partialResults = this.getLastResults();
						if(partialResults!= null)
							mutedTrainingPhrases.addAll(partialResults);
					}
				}
				//Add to the previous collection of tokens, the mutated ones
				for (String variantPhrase : mutedTrainingPhrases) {
					lit = GeneratorFactory.eINSTANCE.createLiteral();
					lit.setText(variantPhrase);
					trainIn.getTokens().add(lit);
				}
					mutedTrainingPhrases.clear();
					this.releaseLastResults();
			}
			
			bRet = true;
		}

		return bRet;
	}
	private void releaseLastResults()
	{
		if(tempTrainingPhrases != null)
			tempTrainingPhrases.clear();
	}
	private LinkedList<String> getLastResults() {
		return tempTrainingPhrases;
	}
	boolean generateTrainingPhrase(Token tokenIn, MutationOperatorSet cfgIn)
	{
		Literal litIn;
		ParameterReferenceToken paramRefIn;			
		boolean bRet;
		
		bRet = false;
		if(tokenIn != null)
		{
			if (tokenIn instanceof Literal) {
				//process as literal
				litIn = (Literal) tokenIn;
				System.out.println("Token/Literal: "+litIn.getText());
				
				tempTrainingPhrases = oMutCore.generateVariants(cfgIn, litIn.getText());			
			}
			else if(tokenIn instanceof ParameterReferenceToken)
			{
				//process as parameter
				paramRefIn = (ParameterReferenceToken) tokenIn;
				
				//TODO: Aqui hay que tener ojo, porque no son frases literales, son compuestas y deberiamos devolverlas 
				//construidas ya
				System.out.println("Token/ParameterReferenceToken: "+paramRefIn.getTextReference());				
			}
			
			bRet = true;
		}

		return bRet;
	}
}
