package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.db.ReadOnlyMetricDB;
import metrics.operators.EMetricOperator;

public class DBOperations {

	public static float getAverage(ReadOnlyMetricDB db, EMetricOperator metricIn) {
		int nElements, nParams;
		float fValue;
		LinkedList<MetricValue> metricResList;
		
		nElements=nParams=0;
		fValue = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getIntentMetric(metricIn);
			
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
	}
}
