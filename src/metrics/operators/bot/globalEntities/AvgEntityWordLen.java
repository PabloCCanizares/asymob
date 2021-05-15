package metrics.operators.bot.globalEntities;

import metrics.base.EMetricCategory;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgEntityWordLen extends BotMetricBase{

	public AvgEntityWordLen() {
		super(EMetricOperator.eGlobalEntityWordLen);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricCategory.eEntity, EMetricOperator.eEntityWordLen);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}
	
	@Override
	public void setMetadata() {
		this.strMetricName = "WL";
		this.strMetricDescription = "Average entity word length";
	}
}
