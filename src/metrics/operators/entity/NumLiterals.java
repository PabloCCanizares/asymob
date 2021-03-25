package metrics.operators.entity;

import analyser.EntityAnalyser;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.EntityMetricBase;

public class NumLiterals extends EntityMetricBase{

	public NumLiterals() {
		super(EMetricOperator.eNumLiterals);
	}

	@Override
	public void calculateMetric() {
		
		int nLiterals;
		EntityAnalyser enAnalyser;
		
		//Initialise
		nLiterals = 0;
		enAnalyser = new EntityAnalyser();
		
		//Calculate
		nLiterals = enAnalyser.analyseNumLiterals(entityIn);		
		metricRet = new IntegerMetricValue(this, nLiterals);
	}

}
