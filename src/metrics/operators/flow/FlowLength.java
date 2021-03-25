package metrics.operators.flow;

import analyser.BotAnalyser;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.FlowMetricBase;

public class FlowLength extends FlowMetricBase{

	public FlowLength() {
		super(EMetricOperator.eFlowLength);
	}

	@Override
	public void calculateMetric() {
		BotAnalyser botAnalyser;
		int nPaths;
		
		botAnalyser = new BotAnalyser();
		nPaths = botAnalyser.analyseMaxLenght(flow);
		
		metricRet = new IntegerMetricValue(this, nPaths);
		
		//Set applied metric
		metricRet.setMetricApplied(this);
	}

}
