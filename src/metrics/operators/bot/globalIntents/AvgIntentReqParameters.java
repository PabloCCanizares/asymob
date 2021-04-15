package metrics.operators.bot.globalIntents;

import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgIntentReqParameters extends BotMetricBase{

	
	public AvgIntentReqParameters() {
		super(EMetricOperator.eGlobalAvgReqIntentParameters);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;		
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricOperator.eIntentNumReqParameters);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

}