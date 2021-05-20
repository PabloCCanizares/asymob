package metrics.operators.flow;

import analyser.BotAnalyser;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.FlowMetricBase;

public class FlowNumPaths extends FlowMetricBase{

	public FlowNumPaths() {
		super(EMetricOperator.eFlowNumPaths);
	}

	@Override
	public void calculateMetric() {
		BotAnalyser botAnalyser;
		int nPaths;
		
		botAnalyser = new BotAnalyser();
		nPaths = botAnalyser.analyseNumPaths(flow);
		
		nPaths++;
		
		metricRet = new IntegerMetricValue(this, nPaths);
		
		//Set applied metric
		metricRet.setMetricApplied(this);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "FPATH";
		this.strMetricDescription = "Number of exit points";
	}
}
