package metrics.base;

public class IntMetricValue extends MetricValue{

	public IntMetricValue(Metric metricApplied) {
		super(metricApplied);
	}

	int nValue;

	public int getnValue() {
		return nValue;
	}

	public void setnValue(int nValue) {
		this.nValue = nValue;
	}
	
	
}
