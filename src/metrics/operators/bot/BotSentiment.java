package metrics.operators.bot;

import java.util.LinkedList;

import metrics.base.FloatMetricValue;
import metrics.base.IntegerListMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class BotSentiment extends BotMetricBase{

	public BotSentiment() {
		super(EMetricOperator.eBotSentiment);
	}

	@Override
	public void calculateMetric() {
		LinkedList<Integer> intList;
		
		intList = getSentiment();
		metricRet = new IntegerListMetricValue(this, intList);
	}
	
	public LinkedList<Integer> getSentiment()
	{
		int nElements, nPositive, nNeutral, nNegative;
		float fValue, fAvg;
		LinkedList<MetricValue> metricResList;
		LinkedList<Integer> lRet;
		
		nElements=0;
		fValue = fAvg = 0;
		nPositive = nNeutral = nNegative = 0;
		lRet = null;
		
		//access to the DB
		if(db != null)
		{
			metricResList = db.getIntentMetric(EMetricOperator.eIntentSentiment);
			
			for(MetricValue metricVal: metricResList)
			{
				if(metricVal != null && metricVal instanceof IntegerListMetricValue)
				{
					lRet = ((IntegerListMetricValue)metricVal).getList();
					if(lRet.size()==3)
					{
						nPositive += lRet.get(0);
						nNeutral += lRet.get(1);
						nNegative += lRet.get(2);
					}
					nElements++;
				}
			}
			if(lRet != null)
			{
				lRet.clear();
			}
			else
				lRet = new LinkedList<Integer>();
			lRet.add(nPositive);
			lRet.add(nNeutral);
			lRet.add(nNegative);
			
			if(fAvg >0 && nElements >0)
				fValue = (float)((float)fAvg/(float)nElements);
			else
				fValue =0;
					
			
		}
		return lRet;
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "SNT";
		this.strMetricDescription = "Sentiment of the bot's interactions";
	}
}
