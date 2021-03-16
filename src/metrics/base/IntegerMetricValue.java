package metrics.base;

public class IntegerMetricValue extends MetricValue{

	int nValue;
	
	public IntegerMetricValue(int nValue)
	{
		this.nValue = nValue;
		
		super.setUnit(EMetricUnit.eInt);
		super.setValue(String.format("%d",nValue));
	}
}
