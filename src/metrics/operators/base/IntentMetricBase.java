package metrics.operators.base;

import generator.Intent;
import metrics.base.EMetricCategory;
import metrics.base.Metric;
import metrics.operators.EMetricOperator;

public abstract class IntentMetricBase extends Metric{

	protected Intent intentIn;
	public IntentMetricBase(EMetricOperator metric) {
		super(metric, EMetricCategory.eIntent);
	}

	public void configure(Intent intentIn)
	{
		this.intentIn = intentIn;
	}
}
