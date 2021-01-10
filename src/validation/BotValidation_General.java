package validation;

public class BotValidation_General extends BotValidator {

	public BotValidation_General()
	{
		super();
		this.methodList.add(new FlowValidation());
		this.methodList.add(new IntentValidation());
	}

}
