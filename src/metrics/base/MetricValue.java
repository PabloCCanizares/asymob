package metrics.base;

import metrics.operators.EMetricOperator;

/**
 * Represents the generic results of a metric
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class MetricValue {

	String strId;
	Metric metricApplied;
	EMetricUnit eUnit;
	String strValue;
	
	public MetricValue(Metric metricApplied)
	{
		setMetricApplied(metricApplied);
	}
	public EMetricUnit getUnit() {
		return eUnit;
	}
	public void setUnit(EMetricUnit eUnit) {
		this.eUnit = eUnit;
	}
	public String getValue() {
		return strValue;
	}
	public void setValue(String strValue) {
		this.strValue = strValue;
	}

	//Nam
	public String getMetricApplied()
	{
		return metricApplied != null ? metricApplied.getMetricName() : "null";
	}
	public void setMetricApplied(Metric metricApplied)	{
		this.metricApplied = metricApplied;
	}
	
	public String toString()
	{
		return String.format("%s = %s", metricApplied.getMetricName(), strValue);
	}
	public boolean matchesMetric(EMetricOperator eMetricIn) {
		boolean bRet;
		
		if(eMetricIn != null && metricApplied != null)
		{
			bRet = (eMetricIn == metricApplied.getMetricEnum()); 
		}
		else
			bRet = false;
		
		return bRet;
	}
}
