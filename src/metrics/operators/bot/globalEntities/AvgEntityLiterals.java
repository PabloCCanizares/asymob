package metrics.operators.bot.globalEntities;

import java.util.LinkedList;

import metrics.base.EMetricUnit;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgEntityLiterals extends BotMetricBase{

	public AvgEntityLiterals() {
		super(EMetricOperator.eGlobalAvgEntityLiterals);
	}

	@Override
	public void calculateMetric() {
		int nPaths;
		metricRet = new MetricValue(this);
		System.out.println("[AvgEntityLiterals::calculateMetric] - Init");
		
		
		nPaths = getNumLiterals();
		metricRet.setUnit(EMetricUnit.eInt);
		metricRet.setValue(Integer.toString(nPaths));
	}

	private int getNumLiterals() {
		int nLiterals, nElements;
		float fValue;
		LinkedList<MetricValue> metricResList;
		
		nLiterals=nElements=0;
		fValue = 0;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getEntityMetric(EMetricOperator.eEntityNumLiterals);
			
			for(MetricValue metricVal: metricResList)
			{
				if(metricVal != null && metricVal instanceof IntegerMetricValue)
				{
					nLiterals += ((IntegerMetricValue)metricVal).getIntValue();
					nElements++;
				}
			}
			if(nLiterals >0 && nElements >0)
				fValue = nLiterals/nElements;
			else
				fValue =0;
					
			this.metricRet = new FloatMetricValue(this, fValue);
		}
		return nLiterals;
	}

}
