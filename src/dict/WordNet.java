package dict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WordNet {

	protected static WordNet wnInstance=null;
	protected IRAMDictionary dict = null;
	private final String WNDIR = "/usr/local/WordNet-3.0";
	
	public void reloadDict(String wnDir) throws IOException {
		
		//For the cost of several hundred megabytes of memory, you get a significant speedup.
    	dict = new RAMDictionary(new File(wnDir + File.separator + "dict"), ILoadPolicy.IMMEDIATE_LOAD);
        dict.open();
	}
    private WordNet(String wnDir){
    	
    	if(wnDir == null)
    		dict = new RAMDictionary(new File(WNDIR + File.separator + "dict"), ILoadPolicy.IMMEDIATE_LOAD);
    	else
    		dict = new RAMDictionary(new File(wnDir + File.separator + "dict"), ILoadPolicy.IMMEDIATE_LOAD);
    	
        try {
			dict.open();
		} catch (IOException e) {
			dict = null;
		}
    }
    
    public void close() {
        dict.close();
    }	
    
    public boolean isHyponym(ISynsetID hyponym, ISynsetID term) {
        List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
        hypernyms.add(term);
        for(int s = 0, e = 1; s < e; s++) {
            ISynsetID synsetId = hypernyms.get(s);
            if(hyponym.equals(synsetId)) {
                return true;
            }
            hypernyms.addAll(dict.getSynset(synsetId).getRelatedSynsets(Pointer.HYPONYM));
            e = hypernyms.size();
        }
        return false;
    }        
    
    public boolean isHypernym(ISynsetID hypernym, ISynsetID term) {
        List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
        hypernyms.add(term);
        for(int s = 0, e = 1; s < e; s++) {
            ISynsetID synsetId = hypernyms.get(s);
            if(hypernym.equals(synsetId)) {
                return true;
            }
            hypernyms.addAll(dict.getSynset(synsetId).getRelatedSynsets(Pointer.HYPERNYM));
            e = hypernyms.size();
        }
        return false;
    }
    
    /**
     * check if two lemma has matched sense
     * @param lemma1
     * @param lemma2
     * @return
     */
    public boolean senseMatch(String lemma1, String lemma2) {
        IIndexWord indexWord1 = dict.getIndexWord(lemma1, POS.NOUN);
        IIndexWord indexWord2 = dict.getIndexWord(lemma2, POS.NOUN);
        if(indexWord1 == null || indexWord2 == null) {
            return lemma1.equalsIgnoreCase(lemma2);
        }
        IWord sense1 = dict.getWord(indexWord1.getWordIDs().get(0));
        ISynsetID term1 = sense1.getSynset().getID();
        
        IWord sense2 = dict.getWord(indexWord2.getWordIDs().get(0));
        ISynsetID term2 = sense2.getSynset().getID();
        
        return isHypernym(term1, term2) || isHypernym(term2, term1);
    }    
    
    public ISynsetID getSynsetID(String strTerm, POS pos)
    {
    	ISynsetID synIdRet;
    	IIndexWord indexWord1;
    	IWord sense1;
    	
    	//Initialise
    	synIdRet = null;
    	
    	if(dict != null)
    	{
        	indexWord1 = dict.getIndexWord(strTerm, pos);
        	
            if(indexWord1 != null) {
                sense1 = dict.getWord(indexWord1.getWordIDs().get(0));
                synIdRet = sense1.getSynset().getID();
            } 		
    	}

        return synIdRet;
    }    
    
    //getSynonym
    public HashMap<Integer, LinkedList<String>> getSynonymsMap(String strTerm, POS pos) {
    	ISynset syn;
        IIndexWord idxWord;
        IWord word;
        IWordID wId;
        List<IWordID> wordList;
        LinkedList<String> indexList;
        HashMap<Integer, LinkedList<String>> mapRet;
        
        mapRet = null;
        idxWord = dict.getIndexWord(strTerm, pos);
        if(idxWord != null)
        {
        	mapRet = new HashMap<Integer, LinkedList<String>>();
        	wordList = idxWord.getWordIDs();
        	if(wordList != null)
        	{
        		for(int i=0;i<wordList.size();i++)
            	{
        			wId = wordList.get(i);
        			
        			word = dict.getWord(wId);
        			syn = word.getSynset();
        			indexList = getSynonyms(syn, false);
        			mapRet.put(i, indexList);
            	}
        	}
        }
        
		return mapRet;
	}
    public LinkedList<String> getSynonyms(ISynset synset, boolean bFilterComposed) {
		LinkedList<String> retList;
		String strLemma;
		
		retList = null;
		if(synset != null && synset.getWords() != null)
		{
			retList = new LinkedList<String>();
			for (IWord w : synset.getWords()) {
				if(w != null)
				{
				    strLemma = w.getLemma();
				  
				    //Filter variations marked with '_'
				    if(!bFilterComposed && (strLemma != null && strLemma.indexOf("_")==-1))
				    {
				    	strLemma = strLemma.replace("_",  " ");
				    	retList.add(strLemma);
				    }					
				}
			}
		}
		return retList;
	}    
    //getAntonym

    //getMeronym
    
    //getHypernym
    
    //file:///home/j0hn/Downloads/edu.mit.jwi_2.2.0_manual.pdf
	public static WordNet getInstance() {
				
		if(wnInstance == null)
			wnInstance = new WordNet(null);
		
		return wnInstance;
	}
	
    
    
}
