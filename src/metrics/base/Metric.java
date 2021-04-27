package metrics.base;

import metrics.db.MetricDataBase;
import metrics.db.ReadOnlyMetricDB;
import metrics.operators.EMetricOperator;

public abstract class Metric {


	EMetricOperator metric;
	EMetricCategory category;
	
	protected ReadOnlyMetricDB db;
	protected MetricValue metricRet;
	
	protected String strMetricName;
	protected String strMetricDescription;
	
	public String getMetricName()
	{
		return strMetricName != null ? strMetricName : "null";
	}
	public String getMetricDescription() {
		return strMetricDescription;
	}
	public Metric(EMetricOperator metric, EMetricCategory category)
	{
		this.db = null;
		this.metric = metric;
		this.category = category;
	}
	public EMetricCategory getCategory() {
		return category;
	}
	public void setCategory(EMetricCategory category) {
		this.category = category;
	}

	
	public MetricValue getResults() {
		return metricRet;
	}
	
	public void reset()
	{
		metricRet = null;
	}
	public abstract void calculateMetric();
	
	public abstract void setMetadata();
}
