package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.FloatMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgIntentReadingTime extends BotMetricBase{

	private final float WRPM = 70;
	public AvgIntentReadingTime() {
		super(EMetricOperator.eGlobalAvgReadingTime);
	}

	@Override
	public void calculateMetric() {
		float fWords, fSeconds;
		
		fSeconds = 0;
		fWords = getNumWords();
		if(fWords>0)
			fSeconds = (long) (fWords * (60/WRPM));
		
		metricRet = new FloatMetricValue(this, fSeconds);
	}

	private float getNumWords() {
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

}
