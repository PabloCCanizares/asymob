package metrics.operators.bot.globalflow;

import java.util.LinkedList;

import metrics.base.FloatMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgActionsPerFlow extends BotMetricBase{

	public AvgActionsPerFlow() {
		super(EMetricOperator.eGlobalFlowAvgActions);
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
			metricResList = db.getFlowMetric(EMetricOperator.eFlowActionsAverage);
			
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
