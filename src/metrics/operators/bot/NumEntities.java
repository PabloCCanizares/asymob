package metrics.operators.bot;

import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumEntities extends BotMetricBase{

	public NumEntities() {
		super(EMetricOperator.eNumEntities);
	}

	@Override
	public void calculateMetric() {
		int nEntities;
		if(this.botIn != null)
		{
			metricRet = new MetricValue(this);
			nEntities = this.botIn.getEntities().size();
			metricRet.setUnit(EMetricUnit.eInt);
			metricRet.setValue(Integer.toString(nEntities));
		}
	}
}
