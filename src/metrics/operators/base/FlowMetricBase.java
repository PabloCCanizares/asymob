package metrics.operators.base;

import generator.UserInteraction;
import metrics.base.EMetricCategory;
import metrics.base.Metric;
import metrics.operators.EMetricOperator;

public abstract class FlowMetricBase extends Metric{

	protected UserInteraction flow;
	public FlowMetricBase(EMetricOperator metric) {
		super(metric, EMetricCategory.eFlow);
	}

	public void configure(UserInteraction flow)
	{
		this.flow = flow;
	}
}

