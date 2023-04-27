package testCases.botium.testcase;

import java.util.LinkedList;

public class BotiumIntent {

	String strIntentname;
	LinkedList<String> trainingPhrases;
	
	public BotiumIntent(String strGroupName, LinkedList<String> trainingPhrases) {
		this.strIntentname = strGroupName;
		this.trainingPhrases = trainingPhrases;
	}

	public String getName() {
		return strIntentname;
	}

	public LinkedList<String> getTrainingPhrases() {
		return trainingPhrases;
	}
}
