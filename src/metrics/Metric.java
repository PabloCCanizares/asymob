package metrics;

import metrics.category.EMetricCategory;
import metrics.operators.EMetricOperator;

public abstract class Metric {

	EMetricOperator metric;
	EMetricCategory category;
	
	public Metric(EMetricOperator metric, EMetricCategory category)
	{
		this.metric = metric;
		this.category = category;
	}
	public abstract void reset();
	public abstract void calculateMetric();
	public abstract String getResults();
}
