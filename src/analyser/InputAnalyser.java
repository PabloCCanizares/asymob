package analyser;

import java.util.LinkedList;

import generator.Token;

public class InputAnalyser {

	TokenAnalyser tokenAnalyser;
	public InputAnalyser()
	{
		tokenAnalyser = new TokenAnalyser();
	}
	public LinkedList<String> extractStringsFromTokenList(LinkedList<Token> tokenList) {
		LinkedList<String> retList;
		String strElement;
		retList = null;
		if (tokenList != null)
		{
			retList = new LinkedList<String>();
			for(Token tokIn: tokenList)
			{
				strElement = tokenAnalyser.getTokenText(tokIn);
				retList.add(strElement);
			}
		}
		
		return retList;
	}

	
}
