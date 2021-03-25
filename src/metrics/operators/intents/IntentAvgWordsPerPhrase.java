package metrics.operators.intents;

import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentAvgWordsPerPhrase extends IntentMetricBase{

	public IntentAvgWordsPerPhrase() {
		super(EMetricOperator.eIntentAvgWordsPerPhrase);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases, nWords;
		float fAverage;
		
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		
		nPhrases = inAnalyser.getTotalPhrases(this.intentIn);
		nWords = inAnalyser.getTotalWords(this.intentIn);
		
		if(nWords >0 && nPhrases >0)
			fAverage = (float)((float)nWords/(float)nPhrases);
		
		metricRet = new FloatMetricValue(this, fAverage);
		metricRet.setMetricApplied(this);
	}

}