package training;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EList;

import analyser.TokenAnalyser;
import generator.Bot;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.TrainingPhrase;
import operators.base.MutationOperatorSet;
import training.chaos.TrainingPhraseVarChaosComposed;
import training.chaos.TrainingPhraseVarChaosSingle;
import utteranceVariantCore.UtteranceVariantCore;

public abstract class VariantPhraseGeneratorBase implements IVariantPhraseGenerator {

	protected UtteranceVariantCore oMutCore;
	protected PhraseVariationSet trainingPhraseSet;	
	protected TokenAnalyser tokenAnalyser;
	
	
	protected abstract LinkedList<PhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn);
	public abstract VariationsCollectionText getVariationsCollectionTxt();
	
	
	public boolean applyTrainingPhrasesToChatbot() {
		boolean bRet, bInserted;
		TrainingPhrase tPhrase;
		
		bRet = false;
		if(trainingPhraseSet != null)
		{
			IntentLanguageInputs inputVariant;
			LinkedList<PhraseVariation> variantList;

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
					//Go through all the elements of the variant list
					for(PhraseVariation tPhraseVar: variantList)
					{
						tPhrase = createPhrase(tPhraseVar);
						
						//Add the training phrase to the inputLanguage						
						if(tPhrase != null)
							inputVariant.getInputs().add(tPhrase);
					}
				}
			} 
			bRet = true;
		}
		return bRet;
	}
	protected abstract TrainingPhrase createPhrase(PhraseVariation tPhraseVar);
	public boolean generateTrainingPhraseFull(Bot botIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;		
		EList<Intent> listIntent;
		
		bRet = false;
		
		//Iterate the intents and call the existing method
		if(botIn != null)
		{
			listIntent = botIn.getIntents();
			
			for (Intent intent : listIntent) {
				bRet &= generateTrainingPhrase(intent, cfgIn);
			}
		}
		return bRet;
	}
	
	public boolean generateTrainingPhrase(Intent intentIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		List<IntentLanguageInputs> listLanguages;
		List<IntentInput> inputList;
		LinkedList<PhraseVariation> listVarPhrase;
		bRet = true;

		System.out.println("generateTrainingPhraseFull - Init");
		try
		{
			//Find all the inputs and process them
			//Analyse the different languages
			listLanguages = intentIn.getInputs();

			for (IntentLanguageInputs intentLan : listLanguages) {

				System.out.println("generateTrainingPhraseFull - Analysing intent <"+intentIn.getName()+"> in language "+intentLan.getLanguage().getLiteral());
				inputList = intentLan.getInputs();

				//Find all the inputs and process them
				for (IntentInput input : inputList) {
					listVarPhrase = createTrainingPhrase(input, cfgIn);

					//remove the first one, due to its identical to the original one
					if(listVarPhrase != null)
						associateVarListToIntent(intentLan, listVarPhrase);
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

	
	protected void associateVarListToIntent(IntentLanguageInputs intentLan,
			LinkedList<PhraseVariation> listVarPhrase) {

		if(listVarPhrase != null && intentLan != null)
		{
			trainingPhraseSet.insertPhrase(intentLan, listVarPhrase);
		}
	}
}
