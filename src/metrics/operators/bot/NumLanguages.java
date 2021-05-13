package metrics.operators.bot;

import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumLanguages extends BotMetricBase{

	public NumLanguages() {
		super(EMetricOperator.eNumLanguages);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateMetric() {
		int nLanguages;
		
		nLanguages = 0;
		if(this.botIn != null)
		{
			metricRet = new MetricValue(this);
			nLanguages = botIn.getLanguages().size();
			metricRet.setUnit(EMetricUnit.eInt);
			metricRet.setValue(Integer.toString(nLanguages));
			metricRet.setMetricApplied((Metric) this);
		}
		
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "NL";
		this.strMetricDescription = "Number of languages";
	}
}
