package operators;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import aux.Common;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import operators.base.EMutationOperators;
import operators.base.MutationOperator;

public class MutObjectsToSynonymsOp extends MutationOperator{

	int nPercentage;

	public MutObjectsToSynonymsOp()
	{
		this(1,1,10);
	}

	public MutObjectsToSynonymsOp(int nMin, int nMax, int nPercentage)
	{		
		this.eMutOp = EMutationOperators.EMutObjectsToSynonyms;		
		this.nMax = nMax;
		this.nMin = nMin;
		this.nPercentage = nPercentage;
		bCommandLineOp = true;
	}

	@Override
	public String ToString() {
		return String.format("%s %d", this.eMutOp, nPercentage);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {

		// construct the URL to the Wordnet dictionary directory
		LinkedList<String> retList, indexList, phraseList;
		LinkedList<LinkedList<String>> composedList;
		URL url;

		url = Common.openWordNet();
		retList = null;
		
		if(url != null)
		{
			// construct the dictionary object and open it
			IDictionary dict = new Dictionary(url);
			try {
				dict.open();


				phraseList = Common.SplitUsingTokenizer(strInputPhrase, " ");
				
				if(phraseList != null)
				{
					retList = new LinkedList<String>();
					composedList = new LinkedList<LinkedList<String>>();
					for(String strIndex: phraseList)
					{
						
						//Split the input phrase in parts
						indexList = getSynonyms(dict, strIndex);
						
						if(indexList == null || indexList.size()==0)
						{
							indexList = new LinkedList<String>();
							indexList.add(strIndex);
						}
					    
						//delete the repeated phrases
						if(indexList.size()>0)
							indexList = Common.deleteRepeatedTerms(indexList);
						composedList.add(indexList);
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
				
				if(nIndex+1==composedPhraseList.size())
					Common.addCopyToRetList(retList, currentPhrase, " ");
			}
		}
		
	}

	private LinkedList<String> getSynonyms(IDictionary dict, String strInputWord) {
		LinkedList<String> retList;
		String strLemma;
		
		IIndexWord idxWord = dict.getIndexWord(strInputWord, POS.NOUN);
		
		retList = null;
		try {
			System.out.print(strInputWord);	
		    int x = idxWord.getTagSenseCount();
		    retList = new LinkedList<String>();
		    for (int i = 0; i < x; i++) {
		        IWordID wordID = idxWord.getWordIDs().get(i);
		        IWord word = dict.getWord(wordID);

		        // Adding Related Words to List of Realted Words
		        ISynset synset = word.getSynset();
		        for (IWord w : synset.getWords()) {
		            System.out.println(" "+w.getLemma());
		            strLemma = w.getLemma();
		            
		            if(strLemma != null)
		            {
		            	strLemma = strLemma.replace("_",  " ");
		            	retList.add(strLemma);
		            }
		        }
		    }
		} catch (Exception ex) {
		    System.out.println("> No synonym found!");		    
		}
		return retList;
	}

}
