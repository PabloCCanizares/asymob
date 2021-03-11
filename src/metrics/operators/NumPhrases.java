package metrics.operators;

import generator.Bot;
import generator.Element;
import metrics.base.EMetricCategory;
import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.operators.base.BotMetricBase;

public class NumPhrases extends BotMetricBase{

	public NumPhrases() {
		super(EMetricOperator.eNumPhrases);
	}

	@Override
	public void calculateMetric() {
		Bot botIn;
		if(this.botIn != null)
		{
			//TODO: Calculamos metrica
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
