package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.EMetricUnit;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class MinIntentNumPhrases extends BotMetricBase{

	public MinIntentNumPhrases() {
		super(EMetricOperator.eGlobalAvgIntentPhrases);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = getNumTPI();
		
		if(fLiteralsAvg == -1)
		{
			fLiteralsAvg = 0;
		}
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

	private float getNumTPI() {
		int nLiterals, nElements;
		float fValue;
		LinkedList<MetricValue> metricResList;
		
		nLiterals=nElements=0;
		fValue = 0;
		
		//access to the DB
		if(db != null)
		{
			fValue = DBOperations.getMin(db, EMetricOperator.eIntentNumPhrases);
		}
		return fValue;
	}

	@Override
	public void setMetadata() {
		this.strMetricName = "MTPI"; 
		this.strMetricDescription = "Training phrases per intent"; 
	}

}
