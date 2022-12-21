package testCases.botium;

import java.util.LinkedList;

public class BotiumTestCase {

	LinkedList<BotiumTestCaseFragment> tcList;
	
	public BotiumTestCase()
	{
		tcList = new LinkedList<BotiumTestCaseFragment>();
	}
	
	public void addFragment(BotiumTestCaseFragment fragment)
	{
		tcList.add(fragment);
	}
	
	public BotiumTestCase dup()
	{
		BotiumTestCase tcRet;
		
		tcRet = new BotiumTestCase();
		for(BotiumTestCaseFragment tc: tcList)
		{
			tcRet.addFragment(tc);
		}
		
		return tcRet;
	}

	public LinkedList<BotiumTestCaseFragment> getFragments() {
		return tcList;
	}
}
