package variants.operators.wordvariation;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import auxiliar.Common;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.TaggedWord;
import support.stanford.StandfordTagger;
import support.wordnet.WordNet;
import support.wordnet.disambiguate.IWSD_Disambiguator;

public abstract class WordVariation {

	protected EWordType eWordType;
	protected IWSD_Disambiguator wordSenseDisambiguator;
	
	
	private LinkedList<String> generateWordVariation(TaggedWord tag, int nSense) {
		LinkedList<String> indexList;
		
		indexList = null;
		if(tag != null && tag.word() != null)
		{
			//Split the input phrase in parts
			indexList = doOperation(tag.word(), nSense);// getSynonyms(dict, tag.word());
			
			if(indexList == null || indexList.size()==0)
			{
				indexList = createUnitaryList(tag);
			}
		    
			//delete the repeated phrases
			if(indexList.size()>0)
				indexList = Common.deleteRepeatedTerms(indexList);			
			
			//If 
			//if(indexList.indexOf(tag.word()) != -1)
			//	indexList.remove(tag.word());
		}
		
		return indexList;
	}

		/**
	 * Creates a list with a single element, which corresponds to the provided tag
	 * @param tag
	 * @return
	 */
	private LinkedList<String> createUnitaryList(TaggedWord tag) {
		LinkedList<String> indexList;
		
		indexList = new LinkedList<String>();
		indexList.add(tag.word());

		return indexList;
	}
	
	public LinkedList<String> wordNetVariation(String strInputPhrase) {
		// construct the URL to the Wordnet dictionary directory
		LinkedList<String> retList, indexList;
		List<List<TaggedWord>>  phraseList;
		LinkedList<LinkedList<String>> composedList;
		URL urlWordnet;
		int nSense, nOrder, nChangesPerformed;
				
		retList = null;
		nChangesPerformed = 0;
		nOrder = 0;
		
		//Tokenize the phrase and tag it.,
		phraseList = StandfordTagger.getInstance().getTaggedWordList(strInputPhrase);
		
		if(phraseList != null)
		{
			retList = new LinkedList<String>();
			composedList = new LinkedList<LinkedList<String>>();
			
			//If the dissambiguator is enabled, use it
			if(wordSenseDisambiguator != null)
				wordSenseDisambiguator.disambiguatePhrase(strInputPhrase);
			
			for(List<TaggedWord> tagList: phraseList)
			{int i=0;
				if(tagList != null)
				{
					for(TaggedWord tag: tagList)
					{
						//Analyse if the word satisfies a property (NOUN in this case)
						if(checkTagProperty(tag))
						{
							nChangesPerformed++;
							if(wordSenseDisambiguator == null)
							{
								//Create a semantic
								indexList = generateWordVariation(tag, -1);
							}
							else
							{					
								nSense = wordSenseDisambiguator.getSense(tagList, tag, nOrder);
								//It means that the tag is not a noun
								if(nSense != -1)
									indexList = generateWordVariation(tag, nSense);
								else
								{
									indexList= null;
									nChangesPerformed--;
								}
							}
							//If 
							if(indexList != null && indexList.size()==1 && indexList.indexOf(tag.value())!= -1)
								nChangesPerformed--;							
						}
						else
						{
							indexList = createUnitaryList(tag);
						}
						if(indexList != null)
							composedList.add(indexList);
						
						nOrder++;
					}
				}
			}
			indexList = new LinkedList<String>();
			if(nChangesPerformed>0)
				composeList(0, composedList, indexList, retList);
			else
			{
				retList = null;
			}
		}		

		
		return retList;
	}

	private void composeList(int nIndex, LinkedList<LinkedList<String>> composedPhraseList, LinkedList<String> currentPhrase, LinkedList<String> retList) {

		LinkedList<String> indexList;
		if(nIndex<composedPhraseList.size())
		{
			indexList = composedPhraseList.get(nIndex);
			
			for(String strIndex: indexList)
			{
				Common.addOrReplaceToken(currentPhrase, nIndex, strIndex);
				
				composeList(nIndex+1, composedPhraseList, currentPhrase, retList);
				
				if(nIndex+1>=composedPhraseList.size())
					Common.addCopyToRetList(retList, currentPhrase, " ");
			}
		}
	}
	
	public boolean checkTagProperty(TaggedWord tag) {
		boolean bRet;
		String strWordType;
		
		bRet = false;
		
		switch (eWordType)
		{
		case eNoun:
			strWordType = "NN";
			break;
		case eAdverb:
			strWordType = "RB";
			break;
		case eAdjective:
			strWordType = "JJ";
			break;			
		default:
			strWordType = "";
			break;
		}
		if (tag.tag().startsWith(strWordType)) 
			bRet = true;
			
		return bRet;
	}
	
	protected POS getPosPerType(EWordType eWordType) {
		POS posType; 
		
		switch(eWordType)
		{
		case eNoun:
			posType = POS.NOUN;
			break;
		case eAdjective:
			posType = POS.ADJECTIVE;
			break;
		case eAdverb:
			posType = POS.ADVERB;
			break;
		default:
			posType = null;
			break;
		}
		return posType;
	}
	
	abstract public LinkedList<String> doOperation(String word, int nSense);
}
