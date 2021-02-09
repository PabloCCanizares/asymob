package operators.wordvariation;

import java.util.LinkedList;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.stanford.nlp.ling.TaggedWord;

public class WordVariationSyn extends WordVariation {

	public WordVariationSyn(EWordType eWordType) {

		this.eWordType = eWordType;
	}


	@Override
	public LinkedList<String> doOperation(IDictionary dict, String strInputWord) {
		LinkedList<String> retList;
		String strLemma;
		IWordID wordID;
		IWord word;
		ISynset synset;
		IIndexWord idxWord;
		POS posWordType;
		
		posWordType = getPosPerType(eWordType);
		
		idxWord = dict.getIndexWord(strInputWord, posWordType);
		
		retList = null;
		try {
			System.out.print(strInputWord);	
		    int x = idxWord.getTagSenseCount();
		    retList = new LinkedList<String>();
		    for (int i = 0; i < x; i++) {
		        wordID = idxWord.getWordIDs().get(i);
		        word = dict.getWord(wordID);

		        // Adding Related Words to List of Realted Words
		        synset = word.getSynset();		        
		        for (IWord w : synset.getWords()) {
		            System.out.println(" "+w.getLemma());
		            strLemma = w.getLemma();
		          
		            //Filter variations marked with '_'
		            if(strLemma != null && strLemma.indexOf("_")==-1)
		            {
		            	strLemma = strLemma.replace("_",  " ");
		            	retList.add(strLemma);
		            }
		        }
		        /*Set<String> lexicon = new HashSet<>();

		        for (POS p : POS.values()) {
		            idxWord = dict.getIndexWord(strInputWord, p);
		            if (idxWord != null) {
		                System.out.println("\t : " + idxWord.getWordIDs().size());
		                wordID = idxWord.getWordIDs().get(0);
		                word = dict.getWord(wordID);
		                synset = word.getSynset();
		                System.out.print(synset.getWords().size());
		                for (IWord w : synset.getWords()) {
		                    lexicon.add(w.getLemma());
		                }
		            }
		        }*/
		    }
		} catch (Exception ex) {
		    System.out.println("> No synonym found!");		    
		}
		return retList;
	}



}
