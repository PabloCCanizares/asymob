package operators.wordvariation;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import aux.Common;
import aux.StandfordTagger;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.TaggedWord;

public abstract class WordVariation {

	protected EWordType eWordType;
	
	private LinkedList<String> generateWordVariation(IDictionary dict, TaggedWord tag) {
		LinkedList<String> indexList;
		
		indexList = null;
		if(dict != null && tag != null && tag.word() != null)
		{
			//Split the input phrase in parts
			indexList = doOperation(dict, tag.word());// getSynonyms(dict, tag.word());
			
			if(indexList == null || indexList.size()==0)
			{
				indexList = createUnitaryList(tag);
			}
		    
			//delete the repeated phrases
			if(indexList.size()>0)
				indexList = Common.deleteRepeatedTerms(indexList);			
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

		urlWordnet = Common.openWordNet();
		retList = null;
		
		if(urlWordnet != null)
		{
			// construct the dictionary object and open it
			IDictionary wordnetDict = new Dictionary(urlWordnet);
			try {
				wordnetDict.open();
				
				//Tokenize the phrase and tag it.,
				phraseList = StandfordTagger.getInstance().getTaggedWordList(strInputPhrase);
				
				if(phraseList != null)
				{
					retList = new LinkedList<String>();
					composedList = new LinkedList<LinkedList<String>>();
					for(List<TaggedWord> tagList: phraseList)
					{
						if(tagList != null)
						{
							for(TaggedWord tag: tagList)
							{
								//Analyse if the word satisfies a property (NOUN in this case)
								if(checkTagProperty(tag))
								{
									//Create a semantic 
									indexList = generateWordVariation(wordnetDict, tag);
								}
								else
								{
									indexList = createUnitaryList(tag);
								}
								
								composedList.add(indexList);
							}
						}
					}
					indexList = new LinkedList<String>();
					composeList(0, composedList, indexList, retList);
				}				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	
	abstract public LinkedList<String> doOperation(IDictionary dict, String word);
}
