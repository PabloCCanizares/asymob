package analyser.flowTree.conversationSplitter;

import java.util.LinkedList;

import generator.Parameter;
import testCases.botium.BotiumAction;

public class ConversationGroup {

	boolean bDisabledIntent;
	boolean bDisabledAction;
	
	public ConversationGroup(IntentConversationGroup intentGroupIn, ActionConversationGroup actionGroupIn) {
		this.intentGroup = intentGroupIn;
		this.actionGroup = actionGroupIn;
		bDisabledIntent = bDisabledAction = false;
	}
	private IntentConversationGroup intentGroup;
	private ActionConversationGroup actionGroup;
	
	public String getIntentGroupName() {
		return intentGroup != null ? intentGroup.getIntentGroupName() : "";
	}

	public LinkedList<String> getPlainTrainingPhases() {
		
		return intentGroup != null ? intentGroup.getPlainTrainingPhases() : null;
	}

	public String getActionGroupName() {
		return getIntentGroupName();
	}

	public LinkedList<String> getActionResponses() {
		return actionGroup != null ? actionGroup.getResponseList() : null;
	}

	public boolean allParametersPresent() {
		return intentGroup != null ? intentGroup.areAllParametersPresent(): false;
	}

	public LinkedList<Parameter> getNotPresentParams() {
		return  intentGroup != null ? intentGroup.getNotPresentParameters(): null;
	}

	public LinkedList<String> getDefaultResponsesForParam(Parameter param) {
		return actionGroup != null ? actionGroup.getDefaultResponseForParam(param) : null;
		
	}

	public void disableActions() {
		bDisabledAction = true;
	}

	public boolean isDisabledIntent()
	{
		return this.bDisabledIntent;
	}
	
	public boolean isDisabledAction()
	{
		return this.bDisabledAction;
	}

	public LinkedList<String> getFinishingResponse() {
		// TODO Auto-generated method stub
		return actionGroup.getFinishingResponse();
	}	
}