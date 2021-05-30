package metrics.base;

import java.util.LinkedList;

import auxiliar.Common;
import metrics.base.confusingmatrix.ConfusingMatrix;

public class ConfusingMatrixMetricValue  extends MetricValue {
	LinkedList<Integer> intList;
	LinkedList<ConfusingMatrix>  confMatrixList;
	public ConfusingMatrixMetricValue(Metric metricApplied, LinkedList<ConfusingMatrix> matrixList)
	{
		super(metricApplied);
		this.intList = intList;
		
		super.setUnit(EMetricUnit.eString);
		confMatrixList = matrixList;
		super.setValue(String.format("%s",Common.ConfMatrixListToString(matrixList)));
	}
	
	public LinkedList<Integer>  getIntValue()
	{
		return intList;
	}

	public LinkedList<Integer> getList() {
		return intList;
	}
}
