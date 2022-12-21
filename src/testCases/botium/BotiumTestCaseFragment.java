package testCases.botium;

import java.util.Arrays;
import java.util.LinkedList;

import analyser.flowTree.TreeInterAction;

/**
 * This class contains a fragment of conversation that consists of  single intent and a single action
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class BotiumTestCaseFragment {

	boolean bInline;
	boolean bReverse;
	BotiumIntent intent; 
	BotiumAction action;
	
	public BotiumTestCaseFragment(BotiumIntent intent, BotiumAction action, boolean bInline)
	{
		this.intent = intent;
		this.action = action;
		this.bInline = bInline;
		bReverse= false;
	}

	//Only for inline actions
	public BotiumTestCaseFragment(String strIntent, String strAction, boolean bReverse)
	{
		this.intent = new BotiumIntent(strIntent, new LinkedList<String>(Arrays.asList(strIntent)));
		this.action = new BotiumAction(strAction, new LinkedList<String>(Arrays.asList(strAction)));
		this.bInline = true;
		this.bReverse = bReverse;
	}
	
	public String getIntentName() {
		return intent!=null ? intent.getName(): "";
	}

	public LinkedList<String> getTrainingPhrases() {
		return intent!=null ? intent.getTrainingPhrases(): null;
	}

	public String getActionName() {
		return action!=null ? action.getName(): "";
	}

	public LinkedList<String> getActionPhrases() {
		return action!=null ? action.getPhrases(): null;
	}

	public boolean isInline() {
		return bInline;
	}

	public boolean isReverse() {
		return this.bReverse;
	}

}
