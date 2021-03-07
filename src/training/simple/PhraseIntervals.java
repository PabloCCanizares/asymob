package training.simple;

import java.util.LinkedList;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class PhraseIntervals {

	LinkedList<Pair<Integer, Integer>> pairList;
	
	public PhraseIntervals()
	{
		pairList = new LinkedList<Pair<Integer, Integer>>();
	}
	
	public boolean createPairList(String strVariatedPhrase) {
		
		boolean bRet;
		
		bRet =  false;
		pairList = null;
		if(strVariatedPhrase != null)
		{
			pairList = new LinkedList<Pair<Integer, Integer>>();
			pairList.add(Pair.of(0,strVariatedPhrase.length()));
			bRet = true;
		}
		
		return bRet;
	}
	public void createInterval(int nInit, int nEnd, int nIndex) {
		pairList.add(nIndex, Pair.of(nInit, nEnd));
	}

	public Pair<Integer, Integer> get(int nIndex) {
		return (pairList!= null && pairList.size()>nIndex) ? pairList.get(nIndex) : null;
	}
}
