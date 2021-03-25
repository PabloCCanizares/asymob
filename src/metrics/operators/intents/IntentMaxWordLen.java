package metrics.operators.intents;

import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentMaxWordLen extends IntentMetricBase{

	public IntentMaxWordLen() {
		super(EMetricOperator.eIntentMaxWordLen);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nMaxWordLen;
		
		inAnalyser = new IntentAnalyser();
		
		nMaxWordLen = inAnalyser.getMaxWordLen(intentIn);
		
		metricRet = new IntegerMetricValue(this, nMaxWordLen);
		metricRet.setMetricApplied(this);
	}

}