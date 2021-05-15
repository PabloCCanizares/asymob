package metrics.operators.bot.globalEntities;

import metrics.base.EMetricCategory;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;

public class AvgEntitySynonyms extends BotMetricBase{

	public AvgEntitySynonyms() {
		super(EMetricOperator.eGlobalAvgEntitySynonyms);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;
		
		fLiteralsAvg = DBOperations.getAverage(db, EMetricCategory.eEntity, EMetricOperator.eEntityAvgSynonyms);
		metricRet = new FloatMetricValue(this, fLiteralsAvg);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "SPL";
		this.strMetricDescription = "Average synonyms per literal";
	}
}
