package analyser;

import java.util.LinkedList;

import generator.Token;

public class InputAnalyser {

	TokenAnalyser tokenAnalyser;
	private Conversor converter;
	public InputAnalyser()
	{
		tokenAnalyser = new TokenAnalyser();
	}
	public InputAnalyser(Conversor converter) {
		this.converter = converter;
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
				strElement = tokenAnalyser.getTokenText(tokIn, false);
				retList.add(strElement);
			}
		}
		
		return retList;
	}

	
}
