package testCases.botium.testcase;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class contains a fragment of conversation that consists of a single intent and a single action
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class BotiumTestCaseFragment {

	boolean bInline;		//If the fragment includes phrases inline (without the TAG)
	boolean bReverse;		//If the fragment must be written in an inverted way (user - bot <-> bot - user)
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
	
	public BotiumTestCaseFragment(String strIntent, String strAction, LinkedList<String> defaultResp, LinkedList<String> defaultPrompts, boolean bReverse) {
		this.intent = new BotiumIntent(strIntent, defaultResp);
		this.action = new BotiumAction(strAction, defaultPrompts);
		this.bInline = false;
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
