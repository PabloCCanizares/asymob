package metrics.base;

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
}