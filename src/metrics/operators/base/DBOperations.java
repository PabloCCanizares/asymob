package metrics.operators.base;

import java.util.LinkedList;

import metrics.base.EMetricCategory;
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

	public static float getAverage(ReadOnlyMetricDB db, EMetricCategory eCategory, EMetricOperator metricIn) {
		int nElements, nParams;
		float fValue;
		LinkedList<MetricValue> metricResList;
		
		nElements=nParams=0;
		fValue = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = extractMetricList(db, eCategory, metricIn);
			
			if (metricResList != null)
			{
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
		}
		return fValue;
	}

	private static LinkedList<MetricValue> extractMetricList(ReadOnlyMetricDB db, EMetricCategory eCategory,
			EMetricOperator metricIn) {
		LinkedList<MetricValue> metricResList;
		switch(eCategory)
		{
		case eIntent:
			metricResList = db.getIntentMetric(metricIn);
			break;
		case eEntity:
			metricResList = db.getEntityMetric(metricIn);
			break;
		case eBot:
			metricResList = db.getBotMetrics();
		case eFlow:
			metricResList = db.getFlowMetric(metricIn);
			break;
		case eGlobalFlow:
			metricResList = db.getBotMetrics();
			break;
		default:
			metricResList = null;
			break;
		}
		return metricResList;
	}
}
