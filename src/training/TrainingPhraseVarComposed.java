package training;

import java.util.LinkedList;

import generator.Token;

public class TrainingPhraseVarComposed extends TrainingPhraseVariation {

	Token originalToken;
	LinkedList<TrainingPhraseVarSimple> phraseParts; 

	public TrainingPhraseVarComposed() {
		
	}

	public void insertPart()
	{
		
	}
	public boolean isEmpty() {
		
		return phraseParts == null || phraseParts.size()==0;
	}

	public LinkedList<TrainingPhraseVarSimple> getTrainingPhrases() {
		return phraseParts;
	}
}
