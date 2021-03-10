package metrics;

import generator.Element;
import metrics.base.EMetricCategory;
import metrics.base.EMetricUnit;
import metrics.operators.EMetricOperator;

public abstract class Metric {

	EMetricOperator metric;
	EMetricCategory category;
	
	public Metric(EMetricOperator metric, EMetricCategory category)
	{
		this.metric = metric;
		this.category = category;
	}
	public EMetricCategory getCategory() {
		return category;
	}
	public void setCategory(EMetricCategory category) {
		this.category = category;
	}
	
	public abstract void reset();
	public abstract void calculateMetric(Element elementIn);
	public abstract EMetricUnit getResults();
}
