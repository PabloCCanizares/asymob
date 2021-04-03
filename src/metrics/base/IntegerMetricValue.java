package metrics.base;

public class IntegerMetricValue extends MetricValue{

	int nValue;
	
	public IntegerMetricValue(Metric metricApplied, int nValue)
	{
		super(metricApplied);
		this.nValue = nValue;
		
		super.setUnit(EMetricUnit.eInt);
		super.setValue(String.format("%d",nValue));
	}
	
	public int getIntValue()
	{
		return nValue;
	}
}
