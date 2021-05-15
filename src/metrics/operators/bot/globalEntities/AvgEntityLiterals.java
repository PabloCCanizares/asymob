package metrics.operators.bot.globalEntities;

import metrics.base.EMetricCategory;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgEntityLiterals extends BotMetricBase{

	public AvgEntityLiterals() {
		super(EMetricOperator.eGlobalAvgEntityLiterals);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricCategory.eEntity, EMetricOperator.eEntityNumLiterals);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}
	
	@Override
	public void setMetadata() {
		this.strMetricName = "LPE";
		this.strMetricDescription = "Average literal per entity";
	}
}
