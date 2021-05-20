package metrics.operators.flow;

import analyser.BotAnalyser;
import analyser.FlowAnalyser;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.FlowMetricBase;

public class FlowActionsAverage extends FlowMetricBase{

	public FlowActionsAverage() {
		super(EMetricOperator.eFlowActionsAverage);
	}

	@Override
	public void calculateMetric() {
		FlowAnalyser flowAnalyser;
		int nEdges, nActions;
		float fAverage;
		
		fAverage=0;
		flowAnalyser = new FlowAnalyser();
		nEdges = flowAnalyser.getTotalEdges(flow);
		nActions = flowAnalyser.getTotalActions(flow);
		
		if(nEdges >0)
			fAverage = (float) nActions/ (float) nEdges;
		
		metricRet = new FloatMetricValue(this, fAverage);
		
		//Set applied metric
		metricRet.setMetricApplied(this);
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "FFACT";
		this.strMetricDescription = "Actions per flow";
	}
}
