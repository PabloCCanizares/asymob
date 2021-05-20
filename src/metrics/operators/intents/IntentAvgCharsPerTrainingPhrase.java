package metrics.operators.intents;

import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentAvgCharsPerTrainingPhrase extends IntentMetricBase{

	public IntentAvgCharsPerTrainingPhrase() {
		super(EMetricOperator.eIntentAvgCharsPerPhrase);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases, nChars;
		float fAverage;
		
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		
		nPhrases = inAnalyser.getTotalPhrases(this.intentIn);
		nChars = inAnalyser.getTotalChars(this.intentIn);
		
		if(nChars >0 && nPhrases >0)
			fAverage = (float)((float)nChars/(float)nPhrases);
		
		metricRet = new FloatMetricValue(this, fAverage);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "ICPTP";
		this.strMetricDescription = "Characters per training phrase";
	}
}