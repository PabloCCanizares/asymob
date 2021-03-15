package metrics.operators.bot;

import generator.Bot;
import generator.Element;
import metrics.base.EMetricCategory;
import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumEntities extends BotMetricBase{

	MetricValue metricRet;
	public NumEntities() {
		super(EMetricOperator.eNumEntities);
	}

	@Override
	public void calculateMetric() {
		int nEntities;
		if(this.botIn != null)
		{
			metricRet = new MetricValue();
			System.out.println("[NumEntities::calculateMetric] - Init");
			
			nEntities = this.botIn.getEntities().size();
			metricRet.setUnit(EMetricUnit.eInt);
			metricRet.setValue(Integer.toString(nEntities));
			
			metricRet.setMetricApplied((Metric) this);
			System.out.printf("[NumEntities::calculateMetric] - Metric: %s Value: %s \n", metricRet.getUnit(), metricRet.getValue());
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MetricValue getResults() {
		return metricRet;
	}

}
