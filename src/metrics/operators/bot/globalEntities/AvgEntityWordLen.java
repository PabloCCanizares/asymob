package metrics.operators.bot.globalEntities;

import java.util.LinkedList;

import metrics.base.EMetricUnit;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgEntityWordLen extends BotMetricBase{

	public AvgEntityWordLen() {
		super(EMetricOperator.eGlobalEntityWordLen);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = getNumLiterals();
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}

	private float getNumLiterals() {
		int nElements;
		float fValue, fAcc;
		LinkedList<MetricValue> metricResList;
		
		nElements=0;
		fValue = 0;
		fAcc = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getEntityMetric(EMetricOperator.eEntityWordLen);
			
			for(MetricValue metricVal: metricResList)
			{
				if(metricVal != null && metricVal instanceof FloatMetricValue)
				{
					fAcc += ((FloatMetricValue)metricVal).getFloatValue();
					nElements++;
				}
			}
			if(fAcc >0 && nElements >0)
				fValue = (float)((float)fAcc/(float)nElements);
			else
				fValue =0;
					
			
		}
		return fValue;
	}

}
