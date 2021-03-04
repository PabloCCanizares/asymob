package training;

import java.util.LinkedList;
import java.util.List;

import generator.IntentInput;
import generator.Token;
import generator.TrainingPhrase;
import operators.base.MutationOperatorSet;
import training.chaos.TrainingPhraseVarTemplate;
import training.chaos.TrainingPhraseVariation;

public class SimplePhraseGenerator extends VariantPhraseGeneratorBase {


	@Override
	public boolean applyTrainingPhrasesToChatbot() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public VariationsCollectionText getVariationsCollectionTxt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected LinkedList<TrainingPhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn) {
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


				//This method generates a list of elements, which have a pointer to the input token,
				//and a list of sentences variations in form of strings. 
				partialResults = generateTrainingPhrase(tokenList, cfgIn); //TODO: Procesar con la lista entera de tokens para generar un contexto

				if(partialResults != null) 
				{
					mutedTrainingPhrases.add(partialResults);
				}
				
				
				
			}
		}
		return null;
	}

}
