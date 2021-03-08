package variants.operators.wordvariation;

import java.util.LinkedList;
import java.util.List;

import dict.WordNet;
import dict.disambiguate.SenseRelate_AllWords;
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
		wordSenseDisambiguator = new SenseRelate_AllWords();
	}


	@Override
	public LinkedList<String> doOperation(String strInputWord, int nPosition) {
		LinkedList<String> retList, indexList;
		String strLemma;
		IWordID wordID;
		IWord word;
		ISynset synset;
		IIndexWord idxWord;
		POS posWordType;
		
		
		retList = null;
		try {
			posWordType = getPosPerType(eWordType);
			idxWord = WordNet.getInstance().getIndexWord(strInputWord, posWordType);
			
		    int x = idxWord.getTagSenseCount();
		    retList = new LinkedList<String>();
		    
		    //If the sense/context of the word is not provided, return back all
		    if(nPosition == -1)
		    {
		    	for (int i = 0; i < x; i++) {
			        indexList = getSynonymsFromSense(idxWord,  i);
			        
			        //TODO: Filter by hyper
			        //indexList = getHypernym(dict, synset);
			        //indexList = getSimilarWords(dict, synset);
			        
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
		    }
		    else
		    { 
		    	if(nPosition<idxWord.getWordIDs().size())
		    	{
		    		indexList = getSynonymsFromSense(idxWord, nPosition);
			        
			        if(indexList != null && indexList.size()>0)
			        	retList.addAll(indexList);
		    	}
		    	
		    }
			    
		} catch (Exception ex) {
		    System.out.println("> No synonym found!");		    
		}
		return retList;
	}


	private LinkedList<String> getSynonymsFromSense(IIndexWord idxWord, int nPosition) {
		LinkedList<String> indexList;
		IWordID wordID;
		IWord word;
		ISynset synset;
		
		try
		{
			//GEt the word related in this position
			wordID = idxWord.getWordIDs().get(nPosition);
			word = WordNet.getInstance().getWord(wordID);
			
			// Adding Related Words to List of Realted Words
			synset = word.getSynset();		        
			
			//Metodo mas general
			indexList = WordNet.getInstance().getSynonyms(synset, false);	
		}
		catch(Exception e)
		{
			indexList = null;
		}
		
		return indexList;
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
	



}
