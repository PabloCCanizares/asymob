package training;

import java.util.LinkedList;

import generator.ParameterReferenceToken;
import generator.Token;

public class TrainingPhraseVarTemplate extends TrainingPhraseVarBase {

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

	public void insertVariation(int nIndex, String strToken) {
		if(trainingVariationList == null)
			trainingVariationList = new LinkedList<String>();
		
		if(nIndex <= trainingVariationList.size())
			trainingVariationList.add(nIndex, strToken);
		
	}
}
