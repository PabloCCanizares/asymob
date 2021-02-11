package operators.wordvariation;

import java.util.LinkedList;
import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.stanford.nlp.ling.TaggedWord;

public class WordVariationSyn extends WordVariation {

	public WordVariationSyn(EWordType eWordType) {

		this.eWordType = eWordType;
	}


	@Override
	public LinkedList<String> doOperation(IDictionary dict, String strInputWord) {
		LinkedList<String> retList, indexList;
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
		        
		        //Metodo mas general
		        indexList = getWords(synset);
		        
		        
		        indexList = getHypernym(dict, synset);

		        indexList = getSimilarWords(dict, synset);
		        //TODO: Filter by hyper
		        
		        if(indexList != null && indexList.size()>0)
		        	retList.addAll(indexList);
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


	private LinkedList<String> getSimilarWords(IDictionary dict, ISynset synset) {
		   LinkedList<String> retList;
	       List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.SIMILAR_TO);
	       
	       retList = null;
	       
	       for(ISynsetID sid : hypernyms) {
	            System.out.println(sid + " " + dict.getSynset(sid).getGloss());
	            for(IWord w : dict.getSynset(sid).getWords()) {
	                System.out.println("similar> "+w.getLemma());
	            }
	        }
	        return retList;
	}


	private LinkedList<String> getHypernym(IDictionary dict, ISynset synset)
	{
	   LinkedList<String> retList;
       List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
       
       retList = null;
       
       for(ISynsetID sid : hypernyms) {
            System.out.println(sid + " " + dict.getSynset(sid).getGloss());
            for(IWord w : dict.getSynset(sid).getWords()) {
                System.out.println(w.getLemma());
            }
        }
        return retList;
	}
	private LinkedList<String> getWords(ISynset synset) {
		LinkedList<String> retList;
		String strLemma;
		
		retList = null;
		if(synset != null && synset.getWords() != null)
		{
			retList = new LinkedList<String>();
			for (IWord w : synset.getWords()) {
				if(w != null)
				{
				    System.out.println(" "+w.getLemma());
				    strLemma = w.getLemma();
				  
				    //Filter variations marked with '_'
				    if(strLemma != null && strLemma.indexOf("_")==-1)
				    {
				    	strLemma = strLemma.replace("_",  " ");
				    	retList.add(strLemma);
				    }					
				}
			}
		}
		return retList;
	}



}
