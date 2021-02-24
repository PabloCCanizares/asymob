package dict.disambiguate;

import java.util.LinkedList;
import java.util.List;

import aux.Common;
import aux.JavaRunCommand;
import edu.stanford.nlp.ling.TaggedWord;

public class SenseRelate_AllWords implements IWSD_Disambiguator {

	LinkedList<LinkedList<DisTaggedWord>> disambiguatedPhrases;
	
	public SenseRelate_AllWords() {
		disambiguatedPhrases = null;
	}
	@Override
	public boolean disambiguatePhrase(String strPhrase) {
		boolean bRet;
		JavaRunCommand command;
		LinkedList<String> resList;
		LinkedList<DisTaggedWord> indexDisPhrase;
		
		bRet = false;
		command = new JavaRunCommand();
		
		command.setProgram("perl");
		command.setInputPhrase(strPhrase);
		command.setProgramPath(System.getProperty("user.dir")+"/scripts/wsd_asymob.pl");
		if(command.runCommand("--trace 0 --context \"\" --format raw\n"))
		{
			disambiguatedPhrases = new LinkedList<LinkedList<DisTaggedWord>>();
			resList = command.getLastResults();
			
			for(String strIn: resList)
			{
				indexDisPhrase = parsePhrase(strIn);
				
				if(indexDisPhrase != null)
					disambiguatedPhrases.add(indexDisPhrase);
			}
		}
		
		//Now, we must parse and generate a LinkedList<WordTagged>
		return bRet;
	}
	private LinkedList<DisTaggedWord> parsePhrase(String strIn) {
		LinkedList<DisTaggedWord> retList;
		LinkedList<String> separatedList;
		DisTaggedWord indexDisWord;
		
		retList = null;
		
		//Here we parse: split in lines ' ' + each component split in lines with #
		if(strIn != null)
		{
			retList = new LinkedList<DisTaggedWord>();
			
			separatedList = Common.SplitUsingTokenizer(strIn, " ");
			
			if(separatedList != null)
			{
				for(String strToken: separatedList)
				{
					indexDisWord = parseDisToken(strToken);
					retList.add(indexDisWord);
				}
			}
		}
		return retList;
	}
	private DisTaggedWord parseDisToken(String strToken) {
		LinkedList<String> separatedList;
		DisTaggedWord disRet;
		
		disRet = null;
		try
		{
			if(strToken != null)
			{
				separatedList = Common.SplitUsingTokenizer(strToken, "#");
				
				if(separatedList != null && separatedList.size() >= 2)
				{
					disRet = new DisTaggedWord();
					
					disRet.setWord(separatedList.get(0));
					disRet.setTag(separatedList.get(1));
					
					if(separatedList.size()  == 3)
						disRet.setSynsetOrder(Integer.parseInt(separatedList.get(2)));
					
				}
			}
		}catch(Exception e)
		{
			disRet = null;
		}
		
		return disRet;
	}
	@Override
	public LinkedList<DisTaggedWord> getDisambiguatedPhrase() {
		LinkedList<DisTaggedWord> distRet;
		
		distRet = null;
		if(disambiguatedPhrases != null && disambiguatedPhrases.size()>0)
			distRet = disambiguatedPhrases.getFirst();
		return distRet;
	}
	@Override
	public int getSense(List<TaggedWord> tagList, TaggedWord tag, int nOrder) {
		int nSenseRet, nPosition, nTimes;
		LinkedList<DisTaggedWord> disPhrase;
		DisTaggedWord distWord;
		String strTagIn, strDisTag;
		nSenseRet = -1;
		nTimes =0;
		//It is possible that the sentence is tagged in a different way, because two different parsers have been used
		//TODO: In the future, count the words directy
		if(tagList != null && tag != null && disambiguatedPhrases != null)
		{
			disPhrase = getDisambiguatedPhrase();
			
			if(disPhrase != null)
			{
				if(tagList.size() == disPhrase.size())
				{
					nPosition = nOrder;
				}
				else
				{
					//TODO: Consideraciones: que sean de distinto tamaño -> hay que buscar a que elemento se refiere.
					
					nTimes = searchWordTimes(tagList, tag, nOrder);
					nPosition=getPosition(disPhrase, tag, nTimes);
				}
				
				if(nPosition >=0 && nPosition<disPhrase.size())
					distWord = disPhrase.get(nPosition);
				else
					distWord = null;
				
				if(distWord != null)
				{
					strTagIn = tag.tag();
					strDisTag = distWord.tag();
					
					if(Common.checkStringContainedIn(strTagIn, strDisTag))//strTagIn.equals(strDisTag))
						nSenseRet = distWord.getSynsetOrder();
				}
			}
			
		}
		
		//Count the number of times which appear each phrase
		
		return nSenseRet;
	}
	private int getPosition(LinkedList<DisTaggedWord> disPhrase, TaggedWord tag, int nTimes) {
		int nRet, nIndex;
		DisTaggedWord disTag;
		String strName, strCompare;
		
		nRet = -1;
		nIndex = 0;
		
		if(disPhrase != null &tag!= null)
		{
			strName = tag.tag();
			while(nTimes>0 && nIndex <disPhrase.size())
			{
				disTag = disPhrase.get(nIndex);
				if(disTag != null)
				{
					if(Common.checkStringContainedIn(disTag.tag(), tag.tag()))
						nTimes--;
					
					if(nTimes ==0)
						nRet =nIndex;
				}
				nIndex++;
			}
			
		}
		
		return nRet;
	}
	private int searchWordTimes(List<TaggedWord> tagList, TaggedWord tag, int nOrder) {
		int nTimes;
		TaggedWord tagWord;
		
		nTimes = 0;
		if(tagList != null)
		{
			for(int i=0;i<=nOrder&&i<tagList.size();i++)
			{
				tagWord = tagList.get(i);
				if(tagWord!= null && Common.checkStringContainedIn(tag.value(), tagWord.value()))
				{
					nTimes++;
				}
					
			}
		}
		
		
		return nTimes;
	}

}
