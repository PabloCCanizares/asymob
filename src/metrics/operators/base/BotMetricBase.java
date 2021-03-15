package metrics.operators.base;

import generator.Bot;
import metrics.base.EMetricCategory;
import metrics.base.Metric;
import metrics.operators.EMetricOperator;

public abstract class BotMetricBase extends Metric{

	protected Bot botIn;
	
	public BotMetricBase(EMetricOperator metric) {
		super(metric, EMetricCategory.eBot);
	}

	public void configure(Bot botIn)
	{
		this.botIn = botIn;
	}
}
