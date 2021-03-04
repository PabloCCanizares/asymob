package training;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import generator.IntentLanguageInputs;

/**
 * This class represent a collection of variations obtained from different elements.
 * For example: A collection includes an intent, and all the phrases generated applying the mutation core. 
 * @author Pablo C. Ca&ntildeizares
 *
 * @param <T>
 */
public class VariationsCollectionText {

	private List<Pair<String,List<String>>> intentVariationPhrases;
	private List<Pair<String,List<String>>> actionVariationPhrases;
	private List<Pair<String,List<String>>> entityVariations;
	public VariationsCollectionText()
	{
		intentVariationPhrases = actionVariationPhrases = entityVariations = null;
	}
	public void insertIntentPhrases(String strName, LinkedList<String> strRetList) {
		if(intentVariationPhrases == null)
			intentVariationPhrases = new LinkedList<Pair<String,List<String>>>();
		
		intentVariationPhrases.add(Pair.of(strName, strRetList));
	}
	public String getIntentsString() {
		return plainPairList(intentVariationPhrases);
	}
	public String getActionsString() {
		return  plainPairList(actionVariationPhrases);
	}	
	private String plainPairList(List<Pair<String,List<String>>> listIn)
	{
		String strRet;
		LinkedList<String> plainValues;
		
		strRet = null;
		if(listIn != null)
		{
			for(Pair<String,List<String>> intentPair: intentVariationPhrases)
			{
				strRet += intentPair.getLeft()+"\n";
				plainValues = (LinkedList<String>) intentPair.getRight();
				if(plainValues != null)
				{
					for(String strValue: plainValues)
					{
						strRet += strValue+"\n";
					}
					strRet += "\n";
				}
			}
		}
		return strRet;
	}

}
