package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.EMetricUnit;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgIntentWordPerTrainingPhrase extends BotMetricBase{

	public AvgIntentWordPerTrainingPhrase() {
		super(EMetricOperator.eGlobalAvgIntentWordPerPhrase);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = getNumLiterals();
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

	private float getNumLiterals() {
		int nElements;
		float fValue, fAvg;
		LinkedList<MetricValue> metricResList;
		
		nElements=0;
		fValue = fAvg = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getIntentMetric(EMetricOperator.eIntentAvgWordsPerPhrase);
			
			for(MetricValue metricVal: metricResList)
			{
				if(metricVal != null && metricVal instanceof FloatMetricValue)
				{
					fAvg += ((FloatMetricValue)metricVal).getFloatValue();
					nElements++;
				}
			}
			if(fAvg >0 && nElements >0)
				fValue = (float)((float)fAvg/(float)nElements);
			else
				fValue =0;
					
			
		}
		return fValue;
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "WPTP";
		this.strMetricDescription = "Average word per training phrase"; 
	}
}
