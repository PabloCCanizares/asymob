package metrics.operators.base;

import generator.Bot;
import generator.Intent;
import metrics.base.EMetricCategory;
import metrics.base.Metric;
import metrics.operators.EMetricOperator;

public abstract class IntentMetricBase extends Metric{

	protected Intent intentIn;
	protected Bot botIn;
	public IntentMetricBase(EMetricOperator metric) {
		super(metric, EMetricCategory.eIntent);
	}

	public void configure(Bot botIn, Intent intentIn)
	{
		this.intentIn = intentIn;
		this.botIn = botIn;
	}
}
