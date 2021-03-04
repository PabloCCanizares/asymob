package training.chaos;

import generator.Token;

public class TrainingPhraseVarSimple extends TrainingPhraseVarBase {

	String trainingPhrase;

	public TrainingPhraseVarSimple(Token tokenIn, String trainingPhrase) {
		super(tokenIn);
		this.trainingPhrase = trainingPhrase;
	}

	public boolean isEmpty() {
		
		return trainingPhrase == null || trainingPhrase.length()==0;
	}

	public String getTrainingPhrase() {
		return trainingPhrase;
	}
}
