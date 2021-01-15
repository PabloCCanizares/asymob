package training;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;

public class TrainingPhraseSet {

	//Map that correlates an intent with a new generated training phrase
	HashMap<IntentLanguageInputs,LinkedList<TrainingPhraseVariation>> intentMap;
	
	public TrainingPhraseSet()
	{
		intentMap = new HashMap<IntentLanguageInputs,LinkedList<TrainingPhraseVariation>>();
	}

	//Se debe tener una lista de las nuevas frases que hay que introducir, asociadas a su 'nodo padre'.
	public void insertPhrase(IntentLanguageInputs intentLan, LinkedList<TrainingPhraseVariation> listVarPhrase) {

		LinkedList<TrainingPhraseVariation> listActualPhrases;
		if(intentLan != null && listVarPhrase != null)
		{
			//search the intentLan in the hashmap
			if(intentMap.containsKey(intentLan))
			{
				//If the intent exists in the map, update the content
				listActualPhrases =  intentMap.get(intentLan);
				
				listActualPhrases.addAll(listVarPhrase);
				intentMap.put(intentLan, listActualPhrases);
			}
			else
			{
				//I.O.C create a new entry in the hashmap
				intentMap.put(intentLan, listVarPhrase);
			}
		}
	}
	// TODO: Borrar esto y adaptarlo
	public HashMap<IntentLanguageInputs,LinkedList<TrainingPhraseVariation>> getHashMap() {
		
		return intentMap;
	}
	
	

}
