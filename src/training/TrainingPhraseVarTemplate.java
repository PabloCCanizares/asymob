package training;

import java.util.LinkedList;

import generator.ParameterReferenceToken;
import generator.Token;

public class TrainingPhraseVarTemplate extends TrainingPhraseVarBase {

	Token originalToken;
	LinkedList<String> trainingVariationList;


	public TrainingPhraseVarTemplate(Token tokenIn, LinkedList<String> listStrVariants) {
		super(tokenIn);
		trainingVariationList = listStrVariants;
	}

	public boolean isEmpty() {
		
		return trainingVariationList == null || trainingVariationList.size()==0;
	}

	public LinkedList<String> getVariations() {
		return trainingVariationList;
	}
}
