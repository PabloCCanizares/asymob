package training;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;

public class TrainingPhraseSet {

	//Map that correlates an intent with a new generated training phrase
	HashMap<IntentInput,LinkedList<TrainingPhraseVariation>> intentMap;
	
	public TrainingPhraseSet()
	{
		intentMap = new HashMap<IntentInput,LinkedList<TrainingPhraseVariation>>();
	}

	//Se debe tener una lista de las nuevas frases que hay que introducir, asociadas a su 'nodo padre'.
	public void insertPhrase(IntentInput input, LinkedList<TrainingPhraseVariation> listVarPhrase) {

		LinkedList<TrainingPhraseVariation> listActualPhrases;
		if(input != null && listVarPhrase != null)
		{
			//search the intentLan in the hashmap
			if(intentMap.containsKey(input))
			{
				//If the intent exists in the map, update the content
				listActualPhrases =  intentMap.get(input);
				
				listActualPhrases.addAll(listVarPhrase);
				intentMap.put(input, listActualPhrases);
			}
			else
			{
				//I.O.C create a new entry in the hashmap
				intentMap.put(input, listVarPhrase);
			}
		}
	}
	// TODO: Borrar esto y adaptarlo
	public HashMap<IntentInput,LinkedList<TrainingPhraseVariation>> getHashMap() {
		
		return intentMap;
	}
	
	

}
