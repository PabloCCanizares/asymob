package training.simple;

import generator.TrainingPhrase;
import training.PhraseVariation;

public class PhraseVarSimple extends PhraseVariation{

	TrainingPhrase trainingPhrase;
	
	@Override
	protected boolean isEmpty() {
		return trainingPhrase== null && super.isEmpty();
	}

	public TrainingPhrase getOriginalPhrase() {
		return trainingPhrase;
	}

	public void setTrainingPhrase(TrainingPhrase trainIn) {
		trainingPhrase = trainIn;
	}

}
