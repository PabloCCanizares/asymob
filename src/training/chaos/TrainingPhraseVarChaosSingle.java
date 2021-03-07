package training.chaos;

import generator.Token;

public class TrainingPhraseVarChaosSingle extends PhraseVarChaosBase {

	String trainingPhrase;

	public TrainingPhraseVarChaosSingle(Token tokenIn, String trainingPhrase) {
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
