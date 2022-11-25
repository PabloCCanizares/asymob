package metrics.operators.intents;

import analyser.IntentAnalyser;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentNumPhrasesNFWI extends IntentMetricBase{

	public IntentNumPhrasesNFWI() {
		super(EMetricOperator.eIntentNumPhrases);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases;
		
		if(!this.intentIn.isFallbackIntent())
		{
			String strName;
			strName = this.intentIn.getName(); 
			if(!strName.matches("Default Welcome Intent") && !strName.matches("form") && !strName.matches("Form"))
			{			
				inAnalyser = new IntentAnalyser();
				nPhrases = inAnalyser.getTotalPhrases(this.intentIn);
				
				if(nPhrases == 0)
					System.out.println("=========>Zero phrases intent: "+this.intentIn.getName());
				metricRet = new IntegerMetricValue(this, nPhrases);
			}	
			else
				metricRet = null;
		}
		else
			metricRet = null;
		
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "TPI";
		this.strMetricDescription = "Number of training phrases";
	}
}
