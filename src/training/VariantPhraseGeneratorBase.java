package training;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import analyser.TokenAnalyser;
import generator.Bot;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import operators.base.MutationOperatorSet;
import training.chaos.TrainingPhraseSet;
import training.chaos.TrainingPhraseVariation;
import utteranceVariantCore.UtteranceVariantCore;

public abstract class VariantPhraseGeneratorBase {

	protected UtteranceVariantCore oMutCore;
	protected TrainingPhraseSet trainingPhraseSet;	
	protected TokenAnalyser tokenAnalyser;
	
	
	protected abstract LinkedList<TrainingPhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn);
	public abstract boolean applyTrainingPhrasesToChatbot();
	public abstract VariationsCollectionText getVariationsCollectionTxt();
	
	public boolean generateTrainingPhraseFull(Bot botIn, MutationOperatorSet cfgIn)
	{
		boolean bRet;
		bRet = false;
		EList<Intent> listIntent;
		
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
		LinkedList<TrainingPhraseVariation> listVarPhrase;
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
			LinkedList<TrainingPhraseVariation> listVarPhrase) {

		if(listVarPhrase != null && intentLan != null)
		{
			trainingPhraseSet.insertPhrase(intentLan, listVarPhrase);
		}
	}
}
