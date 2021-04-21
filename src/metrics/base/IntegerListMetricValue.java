package metrics.base;

import java.util.LinkedList;

import auxiliar.Common;

public class IntegerListMetricValue  extends MetricValue {
	LinkedList<Integer> intList;
	
	public IntegerListMetricValue(Metric metricApplied, LinkedList<Integer> intList)
	{
		super(metricApplied);
		this.intList = intList;
		
		super.setUnit(EMetricUnit.eString);
		super.setValue(String.format("%s",Common.intListToString(intList)));
	}
	
	public LinkedList<Integer>  getIntValue()
	{
		return intList;
	}

	public LinkedList<Integer> getList() {
		return intList;
	}
}
