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
		fLiteralsAvg = DBOperations.getAverage(db, EMetricOperator.eIntentNumParameters);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}
/*
	private float getNumLiterals() {
		int nElements, nParams;
		float fValue;
		LinkedList<MetricValue> metricResList;
		
		nElements=nParams=0;
		fValue = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getIntentMetric(EMetricOperator.eIntentNumParameters);
			
			for(MetricValue metricVal: metricResList)
			{
				if(metricVal != null && metricVal instanceof IntegerMetricValue)
				{
					nParams += ((IntegerMetricValue)metricVal).getIntValue();
					nElements++;
				}
			}
			if(nParams >0 && nElements >0)
				fValue = (float)((float)nParams/(float)nElements);
			else
				fValue =0;
		}
		return fValue;
	}*/
}
