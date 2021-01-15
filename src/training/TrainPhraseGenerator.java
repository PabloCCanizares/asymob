package training;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import generator.Bot;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.Token;
import generator.TrainingPhrase;
import operators.base.MutationOperatorSet;
import utteranceVariantCore.UtteranceVariantCore;

public class TrainPhraseGenerator {

	UtteranceVariantCore oMutCore;
	TrainingPhraseSet trainingPhraseSet;
	public TrainPhraseGenerator()
	{
		oMutCore =  new UtteranceVariantCore();
		trainingPhraseSet = new TrainingPhraseSet();
	}
	boolean generateTrainingPhraseFull(Bot botIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		bRet = false;
		
		//Iterate the intents and call the existing method
		if(botIn != null)
		{
			//TODO: 
		}
		return bRet;
	}
	public boolean generateTrainingPhraseFull(Intent intentIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		List<IntentLanguageInputs> listLanguages;
		List<IntentInput> inputList;
		LinkedList<TrainingPhraseVariation> listVarPhrase;
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
								listVarPhrase = createTrainingPhrase(input, cfgIn);
								
								associateVarListToIntent(input, listVarPhrase);
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
	private void associateVarListToIntent(IntentInput input,
			LinkedList<TrainingPhraseVariation> listVarPhrase) {
		
		if(listVarPhrase != null && input != null)
		{
			trainingPhraseSet.insertPhrase(input, listVarPhrase);
		}
	}
	/**
	 * Generates a set of training phrases taking into consideration both an IOntentInput and a set of mutation operators
	 * @param inputIn: Input used as base to generate the new set of phrases
	 * @param cfgIn
	 * @return
	 */
	LinkedList<TrainingPhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn)
	{
		TrainingPhrase trainIn;
		List<Token> tokenList;
		LinkedList<TrainingPhraseVarTemplate> mutedTrainingPhrases;
		TrainingPhraseVarTemplate partialResults;
		LinkedList<TrainingPhraseVariation> retList;
		
		retList = null;
		if(inputIn != null)
		{
			retList = new LinkedList<TrainingPhraseVariation>();
			mutedTrainingPhrases = new LinkedList<TrainingPhraseVarTemplate>();
			
			//Dynamically check if are training phrases [TrainingPhrase] 
			if (inputIn instanceof TrainingPhrase) {
				trainIn = (TrainingPhrase) inputIn;
				
				tokenList = trainIn.getTokens();
				
				//Check if the token is literal or composed [Tokens]
				for(Token tokenIn: tokenList)
				{
					//This method generates a list of elements, which have a pointer to the input token,
					//and a list of sentences variations in form of strings. 
					partialResults = generateTrainingPhrase(tokenIn, cfgIn); 
					if(partialResults != null) 
					{
						mutedTrainingPhrases.add(partialResults);
					}
				}

				//TODO: Refactor to another method
				//En este caso, lo suyo es asociarlo a un intentlanguage y guardarlo hasta despues.
				if(mutedTrainingPhrases.size() == 1)
				{
					TrainingPhraseVarTemplate variantPhrase;
					LinkedList<String> listVariants;
					
					variantPhrase = mutedTrainingPhrases.getFirst();
					
					//Flatten the template into individual phrases
					listVariants = variantPhrase.getVariations();
					
					for(String strVariant: listVariants)
					{
						//Add to the return list, a simple training phrase variation
						retList.add(new TrainingPhraseVarSimple(variantPhrase.getToken(), strVariant));
						
						//Temporally to make it work
						Literal lit = GeneratorFactory.eINSTANCE.createLiteral();
						lit.setText(strVariant);
						trainIn.getTokens().add(lit);
					}
				}
				//Composed list
				else if (mutedTrainingPhrases.size() > 1)
				{
					/*TrainingPhraseVarTemplate variantPhrase, variantChildPhrase;
					TrainingPhraseVarComposed variantComposedPhrase;
					
					variantChildPhrase = mutedTrainingPhrases.getFirst();	
					if(variantPhrase != null && !variantPhrase.isEmpty())
					{				
						variantComposedPhrase = new TrainingPhraseVarComposed();
						//Recorremos todos los trainingphrasevariation e insertamos (lo sacamos de aqui, mover a save)
						//Create new training phrase instance
						variantComposedPhrase.addPart();
						
						//At this point we must create combinations of these elements
						//Depending, at same time, on the maximum mutants to generate
						
						for(int nIndexChild=1; nIndexChild<mutedTrainingPhrases.size();nIndexChild++)
						{
							//Get the variant
							variantChildPhrase = mutedTrainingPhrases.get(nIndexChild);
						}
						//Here we have a complex  
					}
					nIndex++;
							*/		
				}
				
				//Add to the previous collection of tokens, the mutated ones
				/*for (String variantPhrase : mutedTrainingPhrases) {
					lit = GeneratorFactory.eINSTANCE.createLiteral();
					lit.setText(variantPhrase);
					trainIn.getTokens().add(lit);
				}*/
				//Despues
				//lit = GeneratorFactory.eINSTANCE.createLiteral();
				//lit.setText(variantPhrase);
				//trainIn.getTokens().add(lit);
			}
			mutedTrainingPhrases.clear();
		}
			

		return retList;
	}
	/*private void releaseLastResults()
	{
		if(tempTrainingPhrases != null)
			tempTrainingPhrases.clear();
	}
	private LinkedList<TrainingPhraseVariation> getLastResults() {
		return tempTrainingPhrases;
	}*/
	TrainingPhraseVarTemplate generateTrainingPhrase(Token tokenIn, MutationOperatorSet cfgIn)
	{
		Literal litIn;
		LinkedList<String> listStrVariants;
		ParameterReferenceToken paramRefIn;		
		TrainingPhraseVarTemplate trainingRet;
		boolean bRet;
		
		//Initialise
		trainingRet = null;
		bRet = false;
		if(tokenIn != null)
		{
			listStrVariants = new LinkedList<String>();
			if (tokenIn instanceof Literal) {
				//process as literal
				litIn = (Literal) tokenIn;
				System.out.println("Token/Literal: "+litIn.getText());
				
				listStrVariants = oMutCore.generateVariants(cfgIn, litIn.getText());		
				
				//Filter list
				//Insert in the temporal ... No temporal
				trainingRet = new TrainingPhraseVarTemplate(tokenIn, listStrVariants);
			}
			else if(tokenIn instanceof ParameterReferenceToken)
			{
				//process as parameter
				paramRefIn = (ParameterReferenceToken) tokenIn;

				//Generate variants of the token.
				listStrVariants = oMutCore.generateVariants(cfgIn, paramRefIn.getTextReference());		
				
				System.out.println("Token/ParameterReferenceToken: "+paramRefIn.getTextReference());	
				
				trainingRet = new TrainingPhraseVarTemplate(tokenIn, listStrVariants);
			}
			
			bRet = true;
		}

		if(bRet == false)
			trainingRet = null;
		
		return trainingRet;
	}
	public boolean applyTrainingPhrasesToChatbot() {
		boolean bRet;
		
		bRet = false;
		if(trainingPhraseSet != null)
		{
			//TODO: Make the iterator private element of the trainingPhraseSet class
			Iterator iterator = trainingPhraseSet.getHashMap().entrySet().iterator();
	        while (iterator.hasNext()) {
	          Map.Entry me2 = (Map.Entry) iterator.next();
	          System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());
	        } 
			//Loop the traiining phrases set
			bRet = true;
		}
		return bRet;
	}
}
