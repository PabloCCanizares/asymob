package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dict.Dictionaries.SemanticClass;
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
import edu.mit.jwi.item.SynsetID;

public class WordNet_old {
    
    protected IRAMDictionary dict = null;
    
    protected Set<ISynsetID> PERSON = new HashSet<ISynsetID>();
    protected Set<ISynsetID> ANIMAL = new HashSet<ISynsetID>();
    protected Set<ISynsetID> ORGANIZATION = new HashSet<ISynsetID>();
    protected Set<ISynsetID> LOCATION = new HashSet<ISynsetID>();
    protected Set<ISynsetID> GPE = new HashSet<ISynsetID>();
    protected Set<ISynsetID> TIME = new HashSet<ISynsetID>();
    protected Set<ISynsetID> NUMBER = new HashSet<ISynsetID>();
    protected Set<ISynsetID> QUANTITY = new HashSet<ISynsetID>();
    protected Set<ISynsetID> MONEY = new HashSet<ISynsetID>();
    protected Set<ISynsetID> PERCENT = new HashSet<ISynsetID>();
    
    protected ISynsetID MALE;
    protected ISynsetID FEMALE;
    
    protected void init() {
        PERSON.add(new SynsetID(7626, POS.NOUN));
        
        ANIMAL.add(new SynsetID(15024, POS.NOUN));
        
        ORGANIZATION.add(new SynsetID(7899136, POS.NOUN));
        
        LOCATION.add(new SynsetID(26074, POS.NOUN));
        
        GPE.add(new SynsetID(8374658, POS.NOUN));
        
        TIME.add(new SynsetID(14960543, POS.NOUN));
        TIME.add(new SynsetID(14914858, POS.NOUN));
        TIME.add(new SynsetID(14923492, POS.NOUN));
        TIME.add(new SynsetID(15069770, POS.NOUN));
        TIME.add(new SynsetID(14931076, POS.NOUN));
        //TIME.add(new SynsetID(WordNet.getInstance().getSynsetID("night", POS.NOUN), POS.NOUN));
        
        NUMBER.add(new SynsetID(13407358, POS.NOUN));
        
        QUANTITY.add(new SynsetID(32028, POS.NOUN));
        
        MONEY.add(new SynsetID(13212169, POS.NOUN));
        
        PERCENT.add(new SynsetID(13636179, POS.NOUN));
        
        MALE = new SynsetID(9487097, POS.NOUN);
        FEMALE = new SynsetID(9482706, POS.NOUN);
    }

    public WordNet_old(String wnDir) throws IOException {
        dict = new RAMDictionary(new File(wnDir + File.separator + "dict"), ILoadPolicy.IMMEDIATE_LOAD);
        dict.open();
        init();
    }
    
    public void close() {
        dict.close();
    }
    
    public SemanticClass getSemanticClass(String lemma) {
        IIndexWord indexWord = dict.getIndexWord(lemma, POS.NOUN);
        if(indexWord == null) {
            return SemanticClass.UNKNOWN;
        }
        IWord sense = dict.getWord(indexWord.getWordIDs().get(0));
        ISynsetID term = sense.getSynset().getID();
        if(isHypernym(PERSON, term)) {
            return SemanticClass.PERSON;
        }
        else if(isHypernym(ANIMAL, term)) {
            return SemanticClass.ANIMAL;
        }
        else if(isHypernym(ORGANIZATION, term)) {
            return SemanticClass.ORGANIZATION;
        }
        else if(isHypernym(GPE, term)) {
            return SemanticClass.GPE;
        }
        else if(isHypernym(LOCATION, term)) {
            return SemanticClass.LOCATION;
        }
        else if(isHypernym(TIME, term)) {
            return SemanticClass.TIME;
        }
        else if(isHypernym(NUMBER, term)) {
            return SemanticClass.NUMBER;
        }
        else if(isHypernym(QUANTITY, term)) {
            return SemanticClass.QUANTITY;
        }
        else if(isHypernym(MONEY, term)) {
            return SemanticClass.MONEY;
        }
        else if(isHypernym(PERCENT, term)) {
            return SemanticClass.PERCENT;
        }
        return SemanticClass.UNKNOWN;
    }
    
    protected boolean isHypernym(Set<ISynsetID> semClass, ISynsetID term) {
        List<ISynsetID> hypernyms = new ArrayList<ISynsetID>();
        hypernyms.add(term);
        for(int s = 0, e = 1; s < e; s++) {
            ISynsetID synsetId = hypernyms.get(s);
            if(semClass.contains(synsetId)) {
                return true;
            }
            hypernyms.addAll(dict.getSynset(synsetId).getRelatedSynsets(Pointer.HYPERNYM));
            e = hypernyms.size();
        }
        return false;
    }
    
    protected boolean isHypernym(ISynsetID hypernym, ISynsetID term) {
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
    
    protected ISynsetID getSynID(String strTerm)
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
    protected ISynsetID getHyponym(ISynsetID term)
    {
    	ISynsetID idRet;
    	
    	idRet = null;
    	
    	return idRet;
    }
    protected boolean isHyponym(ISynsetID hyponym, ISynsetID term) {
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
    public int isMaleOrFemale(String lemma) {
        IIndexWord indexWord = dict.getIndexWord(lemma, POS.NOUN);
        if(indexWord == null) {
            return 0;
        }
        IWord sense = dict.getWord(indexWord.getWordIDs().get(0));
        ISynsetID term = sense.getSynset().getID();
        
        if(isHypernym(MALE, term)) {
            return 1;
        }
        else if(isHypernym(FEMALE, term)) {
            return -1;
        }
        else {
            return 0;
        }
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
    
    public boolean senseMatchHyponim(String lemma1, String lemma2)
    {
    	boolean bRet;
    	
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
    public static void main(String[] args) throws IOException {
        WordNet_old wordNet = new WordNet_old(args[0]);
        IIndexWord idxWord = wordNet.dict.getIndexWord("person", POS.NOUN);
        IWord word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        ISynset synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("animal", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("organization", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("location", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("administrative_district", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("date", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("time", POS.NOUN);
        for(IWordID wordId : idxWord.getWordIDs()) {
            word = wordNet.dict.getWord(wordId);
            synset = word.getSynset();
            System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        }
        
        idxWord = wordNet.dict.getIndexWord("time_period", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("number", POS.NOUN);
        for(IWordID wordId : idxWord.getWordIDs()) {
            word = wordNet.dict.getWord(wordId);
            synset = word.getSynset();
            System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        }
        
        idxWord = wordNet.dict.getIndexWord("money", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("percent", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        //male
        idxWord = wordNet.dict.getIndexWord("male", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(1));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        //female
        idxWord = wordNet.dict.getIndexWord("female", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(1));
        synset = word.getSynset();
        System.out.println(word.getLemma() + " " + synset.getID() + " (" + synset.getGloss() + ")");
        
        idxWord = wordNet.dict.getIndexWord("definite_quantity", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
        for(ISynsetID sid : hypernyms) {
            System.out.println(sid + " " + wordNet.dict.getSynset(sid).getGloss());
            for(IWord w : wordNet.dict.getSynset(sid).getWords()) {
                System.out.println(w.getLemma());
            }
        }
        
        idxWord = wordNet.dict.getIndexWord("tomorrow", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
        for(ISynsetID sid : hypernyms) {
            System.out.println(sid + " " + wordNet.dict.getSynset(sid).getGloss());
            for(IWord w : wordNet.dict.getSynset(sid).getWords()) {
                System.out.println(w.getLemma());
            }
            
            wordNet.getListHypernym(sid);
        }       
        
        
        idxWord = wordNet.dict.getIndexWord("cat", POS.NOUN);
        word = wordNet.dict.getWord(idxWord.getWordIDs().get(0));
        synset = word.getSynset();
        hypernyms = synset.getRelatedSynsets(Pointer.HYPONYM);
        for(ISynsetID sid : hypernyms) {
            System.out.println(sid + " " + wordNet.dict.getSynset(sid).getGloss());
            for(IWord w : wordNet.dict.getSynset(sid).getWords()) {
                System.out.println("Domain "+w.getLemma());
            }
            
            wordNet.getListHypernym(sid);
        }
        
        String lemma = "night";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "dog";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "company";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "Province";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "mile";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "23";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "percent";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "money";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "week";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "June";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "Sunday";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        lemma = "paper";
        System.out.println(lemma + " " + wordNet.getSemanticClass(lemma));
        
        System.out.println(wordNet.isMaleOrFemale("man"));
        System.out.println(wordNet.isMaleOrFemale("woman"));
        System.out.println(wordNet.isMaleOrFemale("boy"));
        System.out.println(wordNet.isMaleOrFemale("girl"));
        System.out.println(wordNet.isMaleOrFemale("dog"));
        System.out.println("SenseMatch: "+wordNet.senseMatch("Thursday", "Th"));
    }
    
    public List<ISynsetID> getListHypernym(ISynsetID sid_pa) throws IOException {
        List<ISynsetID> hypernym_list = new ArrayList<>();

        boolean end = false;

            hypernym_list.add(sid_pa);
            List<ISynsetID> hypernym_tmp = dict.getSynset(sid_pa).getRelatedSynsets(Pointer.HYPERNYM);
            if (hypernym_tmp.isEmpty()) {
                end = true;
            } else {
                sid_pa = hypernym_tmp.get(0);//we will stick with the first hypernym
            }

        for(int i =0; i< hypernym_list.size();i++){
            System.out.println("->>"+hypernym_list.get(i));
        }
        return hypernym_list;
    }
}