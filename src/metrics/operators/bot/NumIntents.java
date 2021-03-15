package metrics.operators.bot;

import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumIntents extends BotMetricBase{

	public NumIntents() {
		super(EMetricOperator.eNumIntents);
	}

	@Override
	public void calculateMetric() {
		int nIntents;
		if(this.botIn != null)
		{
			metricRet = new MetricValue();
			System.out.println("[NumIntents::calculateMetric] - Init");
			
			nIntents = this.botIn.getIntents().size();
			metricRet.setUnit(EMetricUnit.eInt);
			metricRet.setValue(Integer.toString(nIntents));
			
			metricRet.setMetricApplied((Metric) this);
			System.out.printf("[NumIntents::calculateMetric] - Metric: %s Value: %s \n", metricRet.getUnit(), metricRet.getValue());
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
