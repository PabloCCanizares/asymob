package training.chaos;

import java.util.LinkedList;

import generator.Token;
import training.PhraseVariation;

public class TrainingPhraseVarChaosComposed extends PhraseVariation {

	Token originalToken;
	LinkedList<TrainingPhraseVarChaosSingle> phraseParts; 

	public TrainingPhraseVarChaosComposed() {
		
		phraseParts = new LinkedList<TrainingPhraseVarChaosSingle>();
	}

	public void insertPart()
	{
		
	}
	public boolean isEmpty() {
		
		return phraseParts == null || phraseParts.size()==0;
	}

	public LinkedList<TrainingPhraseVarChaosSingle> getTrainingPhrases() {
		return phraseParts;
	}

	public void reserveSize(int nSize) {
		
		for(int i=0;i<nSize;i++)
			phraseParts.add(i, null);
	}

	public void addOrReplacePhrase(int nIndexChild, TrainingPhraseVarChaosSingle trainingPhraseVarSimple) {
		
		if(phraseParts != null && nIndexChild<phraseParts.size())
		{
			phraseParts.remove(nIndexChild);
			phraseParts.add(nIndexChild, trainingPhraseVarSimple);
		}
		
	}

	public void addPhrase(TrainingPhraseVarChaosSingle newPhrase) {
		phraseParts.add(newPhrase);
		
	}
}
