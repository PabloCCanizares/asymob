package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.EMetricUnit;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgIntentVerbPerPhrase extends BotMetricBase{

	public AvgIntentVerbPerPhrase() {
		super(EMetricOperator.eGlobalAvgIntentVerbPerPhrase);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricOperator.eIntentAvgVerbsPerTPhrase);
		if(fLiteralsAvg == -1)
		{
			fLiteralsAvg = 0;
		}
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

	
	@Override
	public void setMetadata() {
		this.strMetricName = "VPTP";
		this.strMetricDescription = "Average verbs per training phrase"; 
	}
}
