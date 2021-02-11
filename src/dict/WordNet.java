package dict;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.IRAMDictionary;
import edu.mit.jwi.RAMDictionary;
import edu.mit.jwi.data.ILoadPolicy;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WordNet {

	protected IRAMDictionary dict = null;
	
    private WordNet(String wnDir) throws IOException {
        dict = new RAMDictionary(new File(wnDir + File.separator + "dict"), ILoadPolicy.IMMEDIATE_LOAD);
        dict.open();
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
    
    public ISynsetID getSynID(String strTerm)
    {
    	ISynsetID synIdRet;
    	IIndexWord indexWord1;
    	IWord sense1;
    	
    	//Initialise
    	synIdRet = null;
    	
    	if(dict != null)
    	{
        	indexWord1 = dict.getIndexWord(strTerm, POS.NOUN);
        	
            if(indexWord1 != null) {
                sense1 = dict.getWord(indexWord1.getWordIDs().get(0));
                synIdRet = sense1.getSynset().getID();
            } 		
    	}

        return synIdRet;
    }    
}
