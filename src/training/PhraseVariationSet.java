package training;

import java.util.HashMap;
import java.util.LinkedList;

import generator.Intent;
import generator.IntentLanguageInputs;

public class PhraseVariationSet {

	//Map that correlates an intent with a new generated training phrase
	HashMap<IntentLanguageInputs,LinkedList<PhraseVariation>> intentMap;
	
	//Result of processing the intentMap
	HashMap<String,LinkedList<String>> intentStringMap;
	
	public PhraseVariationSet()
	{
		intentMap = new HashMap<IntentLanguageInputs,LinkedList<PhraseVariation>>();
		intentStringMap = null;
	}

	//Se debe tener una lista de las nuevas frases que hay que introducir, asociadas a su 'nodo padre'.
	public void insertPhrase(IntentLanguageInputs intentLan, LinkedList<PhraseVariation> listVarPhrase) {

		LinkedList<PhraseVariation> listActualPhrases;
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
	public HashMap<IntentLanguageInputs,LinkedList<PhraseVariation>> getHashMap() {
		
		return intentMap;
	}

	public void insertStringIntentPhrases(IntentLanguageInputs inputVariant, LinkedList<String> phraseList) {
		String strIntenName;
		LinkedList<String>  tempList;
		 
		if(inputVariant != null && phraseList != null)
		{
			if(intentStringMap == null)
				intentStringMap = new HashMap<String,LinkedList<String>>();
			
			strIntenName = ((Intent)inputVariant.eContainer()).getName();
			
			if(intentStringMap.containsKey(strIntenName))
			{
				tempList = intentStringMap.get(strIntenName);
				phraseList.addAll(tempList);
			}
			intentStringMap.put(strIntenName, phraseList);
		}
	}

	public HashMap<String, LinkedList<String>> getStringHashMap() {
		return intentStringMap;
	}
	
	

}
