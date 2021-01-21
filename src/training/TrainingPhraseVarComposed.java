package training;

import java.util.LinkedList;

import generator.Token;

public class TrainingPhraseVarComposed extends TrainingPhraseVariation {

	Token originalToken;
	LinkedList<TrainingPhraseVarSimple> phraseParts; 

	public TrainingPhraseVarComposed() {
		
		phraseParts = new LinkedList<TrainingPhraseVarSimple>();
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

	public void reserveSize(int nSize) {
		
		for(int i=0;i<nSize;i++)
			phraseParts.add(i, null);
	}

	public void addOrReplacePhrase(int nIndexChild, TrainingPhraseVarSimple trainingPhraseVarSimple) {
		
		if(phraseParts != null && nIndexChild<phraseParts.size())
		{
			phraseParts.remove(nIndexChild);
			phraseParts.add(nIndexChild, trainingPhraseVarSimple);
		}
		
	}

	public void addPhrase(TrainingPhraseVarSimple newPhrase) {
		phraseParts.add(newPhrase);
		
	}
}
