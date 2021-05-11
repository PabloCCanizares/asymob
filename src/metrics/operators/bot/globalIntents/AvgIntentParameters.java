package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgIntentParameters extends BotMetricBase{

	public AvgIntentParameters() {
		super(EMetricOperator.eGlobalAvgIntentParameters);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		//fLiteralsAvg = getNumLiterals() ;
		if(db != null)
		{
			fLiteralsAvg = DBOperations.getAverage(db, EMetricOperator.eIntentNumParameters);
			metricRet = new FloatMetricValue(this, fLiteralsAvg);
		}
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "PPTP";
		this.strMetricDescription = "Parameters per training phrase"; 
	}
}
