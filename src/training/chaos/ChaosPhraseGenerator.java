package training.chaos;

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
import training.PhraseVariation;
import training.PhraseVariationSet;
import training.VariantPhraseGeneratorBase;
import training.VariationsCollectionText;
import variants.UtteranceVariantCore;
import variants.operators.base.MutationOperatorSet;

public class ChaosPhraseGenerator extends VariantPhraseGeneratorBase{


	public ChaosPhraseGenerator()
	{
		oMutCore =  new UtteranceVariantCore();
		trainingPhraseSet = new PhraseVariationSet();
		tokenAnalyser = new TokenAnalyser();
	}
	

	/**
	 * Generates a set of training phrases taking into consideration both an IOntentInput and a set of mutation operators
	 * @param inputIn: Input used as base to generate the new set of phrases
	 * @param cfgIn
	 * @return
	 */
	protected LinkedList<PhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn)
	{
		TrainingPhrase trainIn;
		List<Token> tokenList;
		LinkedList<TrainingPhraseVarChaosTemplate> mutedTrainingPhrases;
		TrainingPhraseVarChaosTemplate partialResults;
		LinkedList<PhraseVariation> retList;

		retList = null;
		if(inputIn != null)
		{
			retList = new LinkedList<PhraseVariation>();
			mutedTrainingPhrases = new LinkedList<TrainingPhraseVarChaosTemplate>();

			//Dynamically check if are training phrases [TrainingPhrase] 
			if (inputIn instanceof TrainingPhrase) {
				trainIn = (TrainingPhrase) inputIn;

				tokenList = trainIn.getTokens();

				//Check if the token is literal or composed [Tokens]
				for(Token tokenIn: tokenList)
				{
					//This method generates a list of elements, which have a pointer to the input token,
					//and a list of sentences variations in form of strings. 
					partialResults = generateTrainingPhrase(tokenIn, cfgIn); //TODO: Procesar con la lista entera de tokens para generar un contexto
					if(partialResults != null) 
					{
						mutedTrainingPhrases.add(partialResults);
					}
				}

				//TODO: Refactor to another method
				//En este caso, lo suyo es asociarlo a un intentlanguage y guardarlo hasta despues.
				if(mutedTrainingPhrases.size() == 1)
				{
					TrainingPhraseVarChaosTemplate variantPhrase;
					LinkedList<String> listVariants;

					if(hasVariations(mutedTrainingPhrases))
					{
						variantPhrase = mutedTrainingPhrases.getFirst();

						//Flatten the template into individual phrases
						listVariants = variantPhrase.getVariations();

						for(String strVariant: listVariants)
						{
							//Add to the return list, a simple training phrase variation
							retList.add(new TrainingPhraseVarChaosSingle(variantPhrase.getToken(), strVariant));
						}
					}

				}
				//Composed list
				else if (mutedTrainingPhrases.size() > 1)
				{
					TrainingPhraseVarChaosComposed variantComposedPhrase;
					
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
						variantComposedPhrase = new TrainingPhraseVarChaosComposed();
						
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
	
	
	private void includeOriginalTrainPhrases(LinkedList<TrainingPhraseVarChaosTemplate> mutedTrainingPhrases) {
		ListIterator<TrainingPhraseVarChaosTemplate> iter;
		TrainingPhraseVarChaosTemplate element;
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
	
	private boolean hasVariations(LinkedList<TrainingPhraseVarChaosTemplate> mutedTrainingPhrases) {
		
		boolean bRet;
		ListIterator<TrainingPhraseVarChaosTemplate> iter;
		
		bRet = false;
		iter = mutedTrainingPhrases.listIterator();
		
		//Search almost one element with variations.
		while( iter.hasNext() && !bRet)
		{
			TrainingPhraseVarChaosTemplate element = iter.next();
			
			bRet = (!element.isEmpty());
		}
		
		return bRet;
	}
	private void createComposedPhrase(int nIndex, LinkedList<TrainingPhraseVarChaosTemplate> mutedTrainingPhrases, TrainingPhraseVarChaosComposed variantComposedPhrase,
			LinkedList<PhraseVariation> retList) {
		TrainingPhraseVarChaosTemplate variantChildPhrase;
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
				variantComposedPhrase.addOrReplacePhrase(nIndexChild, new TrainingPhraseVarChaosSingle(variantChildPhrase.getToken(), strSimplePhrase));
				
				//Recursive call, nIndex+1
				createComposedPhrase(nIndex+1, mutedTrainingPhrases, variantComposedPhrase, retList);
				
				//If is the last element in mutedtraining phrase, store in the return list
				//But it is necessary to create a copy
				if(nIndexChild+1 == mutedTrainingPhrases.size())
					addCopyToRetList(retList, variantComposedPhrase);
				
			}
		}
	}
	private void addCopyToRetList(LinkedList<PhraseVariation> retList,
			TrainingPhraseVarChaosComposed variantComposedPhrase) {
		TrainingPhraseVarChaosComposed newCompPhrase;
		LinkedList<TrainingPhraseVarChaosSingle> list;
		TrainingPhraseVarChaosSingle newPhrase;
		
		if(variantComposedPhrase != null)
		{
			newCompPhrase = new TrainingPhraseVarChaosComposed();
			list = variantComposedPhrase.getTrainingPhrases();
			
			for(TrainingPhraseVarChaosSingle oldPhrase: list)
			{
				if(oldPhrase != null)
				{
					newPhrase = new TrainingPhraseVarChaosSingle(oldPhrase.originalToken, oldPhrase.getTrainingPhrase());
					newCompPhrase.addPhrase(newPhrase);
				}				
			}
			retList.add(newCompPhrase);
		}
	}

	private TrainingPhraseVarChaosTemplate generateTrainingPhrase(Token tokenIn, MutationOperatorSet cfgIn)
	{
		Literal litIn;
		LinkedList<String> listStrVariants;
		ParameterReferenceToken paramRefIn;		
		TrainingPhraseVarChaosTemplate trainingRet;
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

				listStrVariants = oMutCore.generateVariantsPlain(cfgIn, litIn.getText());		

				//Filter list
				if(listStrVariants != null && listStrVariants.size() >0)
					trainingRet = new TrainingPhraseVarChaosTemplate(tokenIn, listStrVariants);
				else
					//We store a null value, to store the associated token to this training phrase.
					//It is possible that this token will be necessary to create a composed training phrase in the next steps.
					trainingRet = new TrainingPhraseVarChaosTemplate(tokenIn, null);				
			}
			else if(tokenIn instanceof ParameterReferenceToken)
			{
				//process as parameter
				paramRefIn = (ParameterReferenceToken) tokenIn;

				//Generate variants of the token.
				listStrVariants = oMutCore.generateVariantsPlain(cfgIn, paramRefIn.getTextReference());						
				
				System.out.println("Token/ParameterReferenceToken: "+paramRefIn.getTextReference());	

				if(listStrVariants != null && listStrVariants.size() >0)
					trainingRet = new TrainingPhraseVarChaosTemplate(tokenIn, listStrVariants);
				else
					//We store a null value, to store the associated token to this training phrase.
					//It is possible that this token will be necessary to create a composed training phrase in the next steps.
					trainingRet = new TrainingPhraseVarChaosTemplate(tokenIn, null);
			}

			bRet = true;
		}

		if(bRet == false)
			trainingRet = null;

		return trainingRet;
	}
	
	private String getComposedPhraseString(PhraseVariation tPhraseVar) {
		TrainingPhraseVarChaosComposed varComposed;
		LinkedList<TrainingPhraseVarChaosSingle> simpleList;
		ParameterReferenceToken parRef;
		Token originalTokenPhrase;
		String strRet;
		String strTrainingPhrase;
		
		strRet = null;
		varComposed = (TrainingPhraseVarChaosComposed) tPhraseVar;
		if(varComposed != null)
		{
			strRet = "";
			//The varComposed, consist of different varSimple
			simpleList = varComposed.getTrainingPhrases();
			for(TrainingPhraseVarChaosSingle varSimplePhrase : simpleList)
			{
				originalTokenPhrase = varSimplePhrase.getToken();

				if(originalTokenPhrase instanceof Literal)
				{
					strTrainingPhrase =varSimplePhrase.getTrainingPhrase(); 
					
					if(strTrainingPhrase != null)
						strRet = strRet.concat(strTrainingPhrase);
				}
				else if(originalTokenPhrase instanceof ParameterReferenceToken)
				{
					strTrainingPhrase =varSimplePhrase.getTrainingPhrase();
					//parRef.setParameter(((ParameterReferenceToken) originalTokenPhrase).getParameter());
					if(strTrainingPhrase != null)
						strRet = strRet.concat(strTrainingPhrase);
				}
			}
		}
		return strRet;
	}
	private void insertSimplePhrase(TrainingPhrase tPhrase, PhraseVariation tPhraseVar) {
		TrainingPhraseVarChaosSingle varSimple;
		Literal lit;

		varSimple = (TrainingPhraseVarChaosSingle) tPhraseVar;
		if(varSimple != null && tPhrase != null)
		{
			lit = GeneratorFactory.eINSTANCE.createLiteral();
			lit.setText(varSimple.getTrainingPhrase());
			tPhrase.getTokens().add(lit);
		}
	}
	private void insertComposedPhrase(TrainingPhrase tPhrase, PhraseVariation tPhraseVar) {
		TrainingPhraseVarChaosComposed varComposed;
		LinkedList<TrainingPhraseVarChaosSingle> simpleList;
		ParameterReferenceToken parRef;
		Token originalTokenPhrase;

		varComposed = (TrainingPhraseVarChaosComposed) tPhraseVar;
		if(varComposed != null && tPhrase != null)
		{
			//The varComposed, consist of different varSimple
			simpleList = varComposed.getTrainingPhrases();
			for(TrainingPhraseVarChaosSingle varSimplePhrase : simpleList)
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


	@Override
	protected TrainingPhrase createPhrase(PhraseVariation tPhraseVar) {
		TrainingPhrase tPhrase;
		boolean bInserted;
		
		bInserted = false;
		tPhrase =  GeneratorFactory.eINSTANCE.createTrainingPhrase();;
		//Depending on the type of each trainingPhraseVariation, we must create different types of elements.
		if(tPhraseVar instanceof TrainingPhraseVarChaosSingle)
		{
			bInserted = true;
			insertSimplePhrase(tPhrase, tPhraseVar);	        			  
		}
		else if(tPhraseVar instanceof TrainingPhraseVarChaosComposed)
		{
			bInserted = true;
			insertComposedPhrase(tPhrase, tPhraseVar);
		}
		
		if(!bInserted)
			tPhrase = null;
		
		return tPhrase;
	}


	@Override
	protected String handleTrainingPhrase(PhraseVariation tPhraseVar) {
		String strPartialPhrase;
		
		strPartialPhrase = null;
		//Depending on the type of each trainingPhraseVariation, we must create different types of elements.
		if(tPhraseVar instanceof TrainingPhraseVarChaosSingle)
		{
			strPartialPhrase =((TrainingPhraseVarChaosSingle) tPhraseVar).getTrainingPhrase();
		}
		else if(tPhraseVar instanceof TrainingPhraseVarChaosComposed)
		{
			strPartialPhrase = getComposedPhraseString(tPhraseVar);
		}
		
		return strPartialPhrase;
	}
}



/*public VariationsCollectionText getVariationsCollectionTxt() {
	LinkedList<String> strRetList;
	String strPartialPhrase, strIntentName;
	VariationsCollectionText colRet;
	boolean bRet, bInserted;
	
	bRet = false;
	strRetList = null;
	colRet = null;
	if(trainingPhraseSet != null)
	{
		IntentLanguageInputs inputVariant;
		LinkedList<PhraseVariation> variantList;
		
		colRet = new VariationsCollectionText();
		//TODO: Make the iterator private element of the trainingPhraseSet class
		Iterator<Entry<IntentLanguageInputs, LinkedList<PhraseVariation>>> iterator = trainingPhraseSet.getHashMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<IntentLanguageInputs, LinkedList<PhraseVariation>> me2 = iterator.next();
			System.out.println("Key: "+me2.getKey() + " & Value: " + me2.getValue());

			inputVariant = (IntentLanguageInputs) me2.getKey();
			variantList = (LinkedList<PhraseVariation>) me2.getValue();
			
			//Insert into the intent, the training phrases
			if(inputVariant != null)
			{
				strRetList = new LinkedList<String>();
				//Go through all the elements of the variant list
				for(PhraseVariation tPhraseVar: variantList)
				{
					//Create a training phrase
					strPartialPhrase = handleTrainingPhrase(tPhraseVar);
					
					//Add the training phrase to the inputLanguage						
					//if(bInserted)
					//	inputVariant.getInputs().add(tPhrase);
					if(strPartialPhrase != null)
						strRetList.add(strPartialPhrase);	
				}
				if(strRetList != null && strRetList.size()>0)
				{
					strIntentName = ((Intent)inputVariant.eContainer()).getName();
					colRet.insertIntentPhrases(strIntentName, strRetList);
				}
					//trainingPhraseSet.insertStringIntentPhrases(inputVariant, strRetList);
			}
		} 
		bRet = true;
	}
	return colRet;
}*/