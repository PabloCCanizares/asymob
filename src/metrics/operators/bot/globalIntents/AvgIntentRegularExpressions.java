package metrics.operators.bot.globalIntents;

import metrics.base.EMetricCategory;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgIntentRegularExpressions extends BotMetricBase{

	
	public AvgIntentRegularExpressions() {
		super(EMetricOperator.eGlobalAvgRegularExpressions);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;		
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricCategory.eIntent, EMetricOperator.eIntentNumRegularExpressions);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

	@Override
	public void setMetadata() {
		this.strMetricName = "RPI";
		this.strMetricDescription = "Average number of regular expressions per intent"; 
	}
}
