package metrics.operators.intents;

import analyser.IntentAnalyser;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentNumPhrases extends IntentMetricBase{

	public IntentNumPhrases() {
		super(EMetricOperator.eIntentNumPhrases);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases;
		
		inAnalyser = new IntentAnalyser();
		nPhrases = inAnalyser.getTotalPhrases(this.intentIn);
		
		metricRet = new IntegerMetricValue(this, nPhrases);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "";
		this.strMetricDescription = "";
	}
}
