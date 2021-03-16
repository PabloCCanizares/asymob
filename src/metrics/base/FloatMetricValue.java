package metrics.base;

public class FloatMetricValue extends MetricValue{

	float fValue;
	
	public FloatMetricValue(float fValue)
	{
		this.fValue = fValue;
		
		super.setUnit(EMetricUnit.eFloat);
		super.setValue(String.format("%f",fValue));
	}
}
