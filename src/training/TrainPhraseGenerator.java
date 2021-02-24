package training;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import analyser.TokenAnalyser;
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
	TokenAnalyser tokenAnalyser;
	public TrainPhraseGenerator()
	{
		oMutCore =  new UtteranceVariantCore();
		trainingPhraseSet = new TrainingPhraseSet();
		tokenAnalyser = new TokenAnalyser();
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
			//TODO: Aplanar esto con excepciones.
			
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

								//remove the first one, due to its identical to the original one
								if(listVarPhrase != null)
									associateVarListToIntent(intentLan, listVarPhrase);
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
	private void associateVarListToIntent(IntentLanguageInputs intentLan,
			LinkedList<TrainingPhraseVariation> listVarPhrase) {

		if(listVarPhrase != null && intentLan != null)
		{
			trainingPhraseSet.insertPhrase(intentLan, listVarPhrase);
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
					partialResults = generateTrainingPhrase(tokenIn, cfgIn); //TODO: Procesar con la lista entera de tokens
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

					if(hasVariations(mutedTrainingPhrases))
					{
						variantPhrase = mutedTrainingPhrases.getFirst();

						//Flatten the template into individual phrases
						listVariants = variantPhrase.getVariations();

						for(String strVariant: listVariants)
						{
							//Add to the return list, a simple training phrase variation
							retList.add(new TrainingPhraseVarSimple(variantPhrase.getToken(), strVariant));
						}
					}

				}
				//Composed list
				else if (mutedTrainingPhrases.size() > 1)
				{
					TrainingPhraseVarComposed variantComposedPhrase;
					
					//First of all, we must check whether almost one of the trainingphrases have been generated.
					//Let us remember that, due to the restrictions size of a composed phrase,
					//all the simple sentences that compounds a composed phrase are managed as a single unit.
					//That is, if the variation have been applied only to a simple phrase, we need to store the
					//rest of phrases to create a new variation.
					
					//Ej: if we have the following composed training phrase:
					/*
					 *  <inputs xsi:type="generator:TrainingPhrase">
				        (1) <tokens xsi:type="generator:Literal" text="I need a "/>
				        (2) <tokens xsi:type="generator:ParameterReferenceToken" parameter="//@intents.3/@parameters.0" textReference="repair"/>
				        (3) <tokens xsi:type="generator:Literal" text="."/>
				      </inputs>
					 *
					 * And the mutation engine only generates variations on the phrase 2. We must create, in the generateTrainingPhrase method,
					 * a TrainingPhraseVarTemp object with a null value in the list associated in the 
					 */
					if(hasVariations(mutedTrainingPhrases))
					{
						variantComposedPhrase = new TrainingPhraseVarComposed();
						
						//Necessaries to create all the posibilities  the composed phrases
						includeOriginalTrainPhrases(mutedTrainingPhrases);
						//We reserve the size of the composed phrase
						variantComposedPhrase.reserveSize(mutedTrainingPhrases.size());
						
						//Recursive call, to create
						createComposedPhrase(0, mutedTrainingPhrases, variantComposedPhrase, retList);
						
						//if not is empty, remove the first element
						if(retList != null && retList.size()>0)
							retList.removeFirst();
					}
					//I.o.c. we discard the composed phrase.
				}
			}
			mutedTrainingPhrases.clear();
		}


		return retList;
	}
	private void includeOriginalTrainPhrases(LinkedList<TrainingPhraseVarTemplate> mutedTrainingPhrases) {
		ListIterator<TrainingPhraseVarTemplate> iter;
		TrainingPhraseVarTemplate element;
		Token token;
		String strTokenStr;
		
		iter = mutedTrainingPhrases.listIterator();
		
		
		//Search almost one element with variations.
		while( iter.hasNext())
		{
			element = iter.next();
			
			token = element.getToken();
			strTokenStr =  tokenAnalyser.getTokenText(token, false);
			
			if(strTokenStr != null)
				element.insertVariation(0, strTokenStr);
		}
	}
	
	private boolean hasVariations(LinkedList<TrainingPhraseVarTemplate> mutedTrainingPhrases) {
		
		boolean bRet;
		ListIterator<TrainingPhraseVarTemplate> iter;
		
		bRet = false;
		iter = mutedTrainingPhrases.listIterator();
		
		//Search almost one element with variations.
		while( iter.hasNext() && !bRet)
		{
			TrainingPhraseVarTemplate element = iter.next();
			
			bRet = (!element.isEmpty());
		}
		
		return bRet;
	}
	private void createComposedPhrase(int nIndex, LinkedList<TrainingPhraseVarTemplate> mutedTrainingPhrases, TrainingPhraseVarComposed variantComposedPhrase,
			LinkedList<TrainingPhraseVariation> retList) {
		TrainingPhraseVarTemplate variantChildPhrase;
		LinkedList<String> listSimple;
		
		//for(int nIndexChild=nIndex; nIndexChild<mutedTrainingPhrases.size();nIndexChild++)
		if(nIndex < mutedTrainingPhrases.size())
		{
			int nIndexChild = nIndex;
			//Get the variant
			variantChildPhrase = mutedTrainingPhrases.get(nIndex);
			
			listSimple = variantChildPhrase.getVariations();
			
			//FOR de cada valor
			for(int i=0;i<listSimple.size(); i++)
			{
				String strSimplePhrase;
				strSimplePhrase = listSimple.get(i);
				
				//Add simple value
				variantComposedPhrase.addOrReplacePhrase(nIndexChild, new TrainingPhraseVarSimple(variantChildPhrase.getToken(), strSimplePhrase));
				
				//Recursive call, nIndex+1
				createComposedPhrase(nIndex+1, mutedTrainingPhrases, variantComposedPhrase, retList);
				
				//If is the last element in mutedtraining phrase, store in the return list
				//But it is necessary to create a copy
				if(nIndexChild+1 == mutedTrainingPhrases.size())
					addCopyToRetList(retList, variantComposedPhrase);
				
			}
		}
	}
	private void addCopyToRetList(LinkedList<TrainingPhraseVariation> retList,
			TrainingPhraseVarComposed variantComposedPhrase) {
		TrainingPhraseVarComposed newCompPhrase;
		LinkedList<TrainingPhraseVarSimple> list;
		TrainingPhraseVarSimple newPhrase;
		
		if(variantComposedPhrase != null)
		{
			newCompPhrase = new TrainingPhraseVarComposed();
			list = variantComposedPhrase.getTrainingPhrases();
			
			for(TrainingPhraseVarSimple oldPhrase: list)
			{
				if(oldPhrase != null)
				{
					newPhrase = new TrainingPhraseVarSimple(oldPhrase.originalToken, oldPhrase.getTrainingPhrase());
					newCompPhrase.addPhrase(newPhrase);
				}				
			}
			retList.add(newCompPhrase);
		}
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
			//TODO: it can be reafactorized using getText(Token) method
			listStrVariants = new LinkedList<String>();
			if (tokenIn instanceof Literal) {
				//process as literal
				litIn = (Literal) tokenIn;
				System.out.println("Token/Literal: "+litIn.getText());

				listStrVariants = oMutCore.generateVariants(cfgIn, litIn.getText());		

				//Filter list
				if(listStrVariants != null && listStrVariants.size() >0)
					trainingRet = new TrainingPhraseVarTemplate(tokenIn, listStrVariants);
				else
					//We store a null value, to store the associated token to this training phrase.
					//It is possible that this token will be necessary to create a composed training phrase in the next steps.
					trainingRet = new TrainingPhraseVarTemplate(tokenIn, null);				
			}
			else if(tokenIn instanceof ParameterReferenceToken)
			{
				//process as parameter
				paramRefIn = (ParameterReferenceToken) tokenIn;

				//Generate variants of the token.
				listStrVariants = oMutCore.generateVariants(cfgIn, paramRefIn.getTextReference());						
				
				System.out.println("Token/ParameterReferenceToken: "+paramRefIn.getTextReference());	

				if(listStrVariants != null && listStrVariants.size() >0)
					trainingRet = new TrainingPhraseVarTemplate(tokenIn, listStrVariants);
				else
					//We store a null value, to store the associated token to this training phrase.
					//It is possible that this token will be necessary to create a composed training phrase in the next steps.
					trainingRet = new TrainingPhraseVarTemplate(tokenIn, null);
			}

			bRet = true;
		}

		if(bRet == false)
			trainingRet = null;

		return trainingRet;
	}
	public boolean applyTrainingPhrasesToChatbot() {
		boolean bRet, bInserted;

		bRet = false;
		if(trainingPhraseSet != null)
		{
			IntentLanguageInputs inputVariant;
			LinkedList<TrainingPhraseVariation> variantList;

			//TODO: Make the iterator private element of the trainingPhraseSet class
			Iterator<Entry<IntentLanguageInputs, LinkedList<TrainingPhraseVariation>>> iterator = trainingPhraseSet.getHashMap().entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<IntentLanguageInputs, LinkedList<TrainingPhraseVariation>> me2 = iterator.next();
				System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());

				inputVariant = (IntentLanguageInputs) me2.getKey();
				variantList = (LinkedList<TrainingPhraseVariation>) me2.getValue();

				//Insert into the intent, the training phrases
				if(inputVariant != null)
				{
					//Go through all the elements of the variant list
					for(TrainingPhraseVariation tPhraseVar: variantList)
					{
						//Create a training phrase
						TrainingPhrase tPhrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
						bInserted = false;
						//Depending on the type of each trainingPhraseVariation, we must create different types of elements.
						if(tPhraseVar instanceof TrainingPhraseVarSimple)
						{
							bInserted = true;
							insertSimplePhrase(tPhrase, tPhraseVar);	        			  
						}
						else if(tPhraseVar instanceof TrainingPhraseVarComposed)
						{
							bInserted = true;
							insertComposedPhrase(tPhrase, tPhraseVar);
						}
						
						//Add the training phrase to the inputLanguage						
						if(bInserted)
							inputVariant.getInputs().add(tPhrase);
					}
				}
			} 
			bRet = true;
		}
		return bRet;
	}

	private void insertSimplePhrase(TrainingPhrase tPhrase, TrainingPhraseVariation tPhraseVar) {
		TrainingPhraseVarSimple varSimple;
		Literal lit;

		varSimple = (TrainingPhraseVarSimple) tPhraseVar;
		if(varSimple != null && tPhrase != null)
		{
			lit = GeneratorFactory.eINSTANCE.createLiteral();
			lit.setText(varSimple.getTrainingPhrase());
			tPhrase.getTokens().add(lit);
		}
	}
	private void insertComposedPhrase(TrainingPhrase tPhrase, TrainingPhraseVariation tPhraseVar) {
		TrainingPhraseVarComposed varComposed;
		LinkedList<TrainingPhraseVarSimple> simpleList;
		ParameterReferenceToken parRef;
		Token originalTokenPhrase;

		varComposed = (TrainingPhraseVarComposed) tPhraseVar;
		if(varComposed != null && tPhrase != null)
		{
			//The varComposed, consist of different varSimple
			simpleList = varComposed.getTrainingPhrases();
			for(TrainingPhraseVarSimple varSimplePhrase : simpleList)
			{
				originalTokenPhrase = varSimplePhrase.getToken();

				if(originalTokenPhrase instanceof Literal)
				{
					insertSimplePhrase(tPhrase, varSimplePhrase);
				}
				else if(originalTokenPhrase instanceof ParameterReferenceToken)
				{
					parRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
					parRef.setTextReference(varSimplePhrase.getTrainingPhrase());
					parRef.setParameter(((ParameterReferenceToken) originalTokenPhrase).getParameter());
					tPhrase.getTokens().add(parRef);
				}
			}
		}
	}	
}
