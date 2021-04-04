package metrics.base;

public class FloatMetricValue extends MetricValue{

	float fValue;
	
	public FloatMetricValue(Metric metricApplied, float fValue)
	{
		super(metricApplied);
		this.fValue = fValue;
		super.setUnit(EMetricUnit.eFloat);
		super.setValue(String.format("%.02f",fValue));
	}
	
	public float getFloatValue()
	{
		return fValue;
	}
}
