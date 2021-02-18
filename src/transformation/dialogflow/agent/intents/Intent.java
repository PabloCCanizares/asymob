package transformation.dialogflow.agent.intents;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import generator.Action;
import generator.Bot;
import generator.GeneratorFactory;
import generator.IntentLanguageInputs;
import transformation.dialogflow.agent.Agent;

public class Intent {

	private String parentId;
	private String rootParentId;
	private String name;
	private boolean auto;
	private List<String> contexts = new ArrayList<>();
	private List<Response> responses = new ArrayList<>();
	private int priority;
	private boolean webhookUsed;
	private boolean webhookForSlotFilling;
	private boolean fallbackIntent;
	private List<Event> events;
	private List<ConditionalResponse> conditionalResponses;
	private String condition;
	private List<ConditionalFollowUpEvent> conditionalFollowupEvents;
	private List<UserSaysLanguage> usersays= new ArrayList<>();
	private generator.Intent intent = null;
	private List<Action> actions = null;

	public Intent()
	{
		conditionalResponses = new LinkedList<ConditionalResponse>();
	}
	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getRootParentId() {
		return rootParentId;
	}


	public void setRootParentId(String rootParentId) {
		this.rootParentId = rootParentId;
	}


	public boolean isAuto() {
		return auto;
	}


	public void setAuto(boolean auto) {
		this.auto = auto;
	}


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}


	public boolean isWebhookForSlotFilling() {
		return webhookForSlotFilling;
	}


	public void setWebhookForSlotFilling(boolean webhookForSlotFilling) {
		this.webhookForSlotFilling = webhookForSlotFilling;
	}	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<String> getContexts() {
		if (contexts == null) {
			return new ArrayList<>();
		}
		return contexts;
	}


	public void setContexts(List<String> context) {
		this.contexts = context;
	}


	public List<Response> getResponses() {
		return responses;
	}


	public void setResponses(List<Response> responses) {
		this.responses = responses;
	}


	public boolean isWebhookUsed() {
		return webhookUsed;
	}


	public void setWebhookUsed(boolean webhookUsed) {
		this.webhookUsed = webhookUsed;
	}


	public boolean isFallbackIntent() {
		return fallbackIntent;
	}


	public void setFallbackIntent(boolean fallbackIntent) {
		this.fallbackIntent = fallbackIntent;
	}


	public List<UserSaysLanguage> getUsersays() {
		return usersays;
	}


	public void setUsersays(List<UserSaysLanguage> usersays) {
		this.usersays = usersays;
	}

	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public List<ConditionalResponse> getConditionalResponses() {
		return conditionalResponses;
	}
	public void setConditionalResponses(List<ConditionalResponse> conditionalResponses) {
		this.conditionalResponses = conditionalResponses;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public List<ConditionalFollowUpEvent> getConditionalFollowupEvents() {
		return conditionalFollowupEvents;
	}
	public void setConditionalFollowupEvents(List<ConditionalFollowUpEvent> conditionalFollowupEvents) {
		this.conditionalFollowupEvents = conditionalFollowupEvents;
	}

	public void addUsersays(UserSaysLanguage userSaysLanguage) {
		if (this.usersays == null) {
			this.usersays = new ArrayList<UserSaysLanguage>();
		}
		this.usersays.add(userSaysLanguage);
	}


	public generator.Intent getBotIntent(Bot bot) {
		if (intent!= null) {
			return intent;
		}
		intent = GeneratorFactory.eINSTANCE.createIntent();
		intent.setName(getName());
		intent.setFallbackIntent(isFallbackIntent());

		for (Response responses : getResponses()) {
			for (Parameter param : responses.getParameters()) {
				intent.getParameters().add(param.getBotParam(bot));
			}
		}

		for (UserSaysLanguage usersays : getUsersays()) {
			IntentLanguageInputs languageInput = usersays.getBotLanguageInput(intent);
			intent.getInputs().add(languageInput);
		}

		return intent;

	}

	public List<Action> getBotIntentActions(Bot bot, Agent agent) {
		if (actions!= null) {
			return actions;
		}
		actions = new ArrayList<>();
		if (isWebhookUsed()) {
			actions.add(bot.getAction("HttpRequest"));
			actions.add(bot.getAction("HttpResponse"));
		}
		for (Response response: getResponses()) {
			actions.addAll(response.getBotActions(getBotIntent(bot), bot, agent));
		}
		return actions;

	}


	public boolean containsAffectedContext(String context) {
		for (Response response: getResponses()) {
			if (response.containsAffextedContext(context)) {
				return true;
			}
		}
		return false;
	}
}

