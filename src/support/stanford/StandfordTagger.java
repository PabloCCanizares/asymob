package support.stanford;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StandfordTagger {

	private final String TAGGER_ENGLISH_BIDIRECTIONAL = "english-bidirectional-distsim.tagger";
	private static StandfordTagger stanTaggerInstance=null;
	private MaxentTagger tagger;
	
	public final static String VERB_TAG = "VB";
	public final static String NOUN_TAG = "NN";
	public static StandfordTagger getInstance() {

		if (stanTaggerInstance==null) {

			stanTaggerInstance=new StandfordTagger();
		}
		return stanTaggerInstance;
	}

	private StandfordTagger(){

		//Load the maxtentTagger
		tagger = new MaxentTagger(System.getProperty("user.dir")+"/tagmodels/"+TAGGER_ENGLISH_BIDIRECTIONAL);
	}
	
	public LinkedList<String> extractNouns(String strPhrase)
	{
		LinkedList<String> retList;		
		String taggedPhrase;
		String[] lines;
		
		retList = null;
		
		if(strPhrase != null && !strPhrase.isBlank())
		{
			retList = new LinkedList<String>();
			taggedPhrase = tagger.tagString(strPhrase);
			lines = taggedPhrase.split(" ");
			
			
	        for (String line : lines) {
	        	if(line.endsWith("_NN"))
	        	{
	        		if(line != null)
	        			retList.add(line.replace("_NN", ""));
	        	}
	        }
		}

        
		return retList;
	}
	//
	public List<List<TaggedWord>> getTaggedWordList(String strSentenceIn) {
		List<List<TaggedWord>> retList;
		List<TaggedWord> tSentence;
		List<List<HasWord>> wordList;
		
		wordList = this.tokenizeToWordList(strSentenceIn); 
		retList = null;

		if(wordList != null && wordList.size()>0)
		{
			retList = new LinkedList<List<TaggedWord>>();
			
			for (List<HasWord> sentence : wordList) 
			{
				tSentence = tagger.tagSentence(sentence);
				if(tSentence != null)
				{
					retList.add(tSentence);
				}
			}
		}
		
		return retList;
	}	
	/**
	 * Splits the sentence into individual strings.
	 * 
	 * @param sentence Input sentence
	 * @return Array of strings
	 */
	public LinkedList<String> tokenizeToStringList(String strSentenceIn) {
		LinkedList<String> retList;
		List<List<HasWord>> sentences;
		
		retList = null;
		sentences = MaxentTagger.tokenizeText(new StringReader(strSentenceIn));
		
		if(sentences != null && sentences.size()>0)
		{
			retList = new LinkedList<String>();		
			
			for (List<HasWord> sentence : sentences) {

				for(HasWord hw : sentence)
				{					
					retList.add(hw.word());
				}
			}
		}
		
		return retList;
	}	
	/**
	 * Splits the sentence into individual tokens.
	 * 
	 * @param sentence Input sentence
	 * @return Array of tokens
	 */
	public List<List<HasWord>> tokenizeToWordList(String strSentenceIn) {
		List<List<HasWord>> sentences;
		
		sentences = null;
		if(strSentenceIn != null && !strSentenceIn.isBlank())
		{
			sentences = MaxentTagger.tokenizeText(new StringReader(strSentenceIn));
		}
		
		return sentences;
	}	
	
	//
	public List<List<Boolean>> checkNouns(List<List<HasWord>> wordList) {
		List<List<Boolean>> retList;
		List<TaggedWord> tSentence;
		List<Boolean> booleanList;
		boolean bTag;
		
		retList = null;

		if(wordList != null && wordList.size()>0)
		{
			retList = new LinkedList<List<Boolean>>();
			
			for (List<HasWord> sentence : wordList) 
			{
				tSentence = tagger.tagSentence(sentence);
				if(tSentence != null && tSentence.size()>0)
				{
					booleanList = new LinkedList<Boolean>();
					for(TaggedWord tagWord: tSentence)
					{
						if(tagWord.tag().startsWith("NN"))
							bTag = true;
						else
							bTag = false;
						
						booleanList.add(bTag);
					}
					retList.add(booleanList);
				}
			}
		}
		
		return retList;
	}	
	
	public int countElements(LinkedList<String> strPhrases, String strElement)
	{
		List<List<TaggedWord>> taggedPhraseList;
		int nRet = 0;
		
		if(strElement.indexOf("NN") != -1 || strElement.indexOf("VB") != -1)
		{
			for(String strPhrase: strPhrases)
			{
				taggedPhraseList = StandfordTagger.getInstance().getTaggedWordList(strPhrase);
				
				nRet += countElements(taggedPhraseList, strElement);
			}
		}
		
		return nRet;
	}

	public int countElements(List<List<TaggedWord>> taggedPhraseList, String strElement) {
		int nRet;
		String strTag;
		nRet = 0;
		
		if(taggedPhraseList != null)
		{
			for(List<TaggedWord> tagList: taggedPhraseList)
			{
				for(TaggedWord tagWord: tagList)
				{
					strTag = tagWord.tag();
					if(strTag != null && strTag.indexOf(strElement) != -1)
						nRet++;
				}
			}
		}

		return nRet;
	}
	public int getTagListLen(List<List<TaggedWord>> taggedPhraseList) {
		int nRet;
		nRet = 0;
		
		if(taggedPhraseList != null)
		{
			for(List<TaggedWord> tagList: taggedPhraseList)
			{
				nRet++;
			}
		}

		return nRet;
	}
}
