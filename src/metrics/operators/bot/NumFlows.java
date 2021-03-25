package metrics.operators.bot;

import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumFlows extends BotMetricBase{

	public NumFlows() {
		super(EMetricOperator.eNumFlows);
	}
	@Override
	public void calculateMetric() {
		int nFlows;
		metricRet = new MetricValue(this);

		nFlows = getFlowsSize();
		metricRet.setUnit(EMetricUnit.eInt);
		metricRet.setValue(Integer.toString(nFlows));
		
		metricRet.setMetricApplied((Metric) this);
	}

	private int getFlowsSize() {
		int nSize;
		
		nSize=0;
		if(botIn != null && this.botIn.getFlows() != null)
			nSize = this.botIn.getFlows().size();
		
		return nSize;
	}
}
