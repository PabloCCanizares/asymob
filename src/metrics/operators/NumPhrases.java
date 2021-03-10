package metrics.operators;

import generator.Bot;
import generator.Element;
import metrics.Metric;
import metrics.base.EMetricCategory;
import metrics.base.EMetricUnit;

public class NumPhrases extends Metric{

	public NumPhrases() {
		super(EMetricOperator.eNumPhrases, EMetricCategory.eIntent);
	}

	@Override
	public void calculateMetric(Element elemIn) {
		Bot botIn;
		if(elemIn instanceof Bot)
		{
			botIn = (Bot) elemIn;
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EMetricUnit getResults() {
		// TODO Auto-generated method stub
		return null;
	}

}
