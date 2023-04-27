package testCases.botium.testcase;

import java.util.LinkedList;

public class BotiumAction {

	String strActionName;
	LinkedList<String> responsePhrases;
	
	public BotiumAction(String strActionGroupName, LinkedList<String> responses) {
		this.strActionName = strActionGroupName;
		this.responsePhrases = responses;
	}

	public String getName() {
		return this.strActionName;
	}

	public LinkedList<String> getPhrases() {
		return this.responsePhrases;
	}
}
