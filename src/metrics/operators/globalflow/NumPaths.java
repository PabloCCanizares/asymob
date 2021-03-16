package metrics.operators.globalflow;

import analyser.BotAnalyser;
import analyser.FlowAnalyser;
import generator.UserInteraction;
import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class NumPaths extends BotMetricBase{

	public NumPaths() {
		super(EMetricOperator.eNumPaths);
	}

	@Override
	public void calculateMetric() {
		int nPaths;
		metricRet = new MetricValue();
		System.out.println("[NumFlows::calculateMetric] - Init");
		
		
		nPaths = getNumPaths();
		metricRet.setUnit(EMetricUnit.eInt);
		metricRet.setValue(Integer.toString(nPaths));
		
		metricRet.setMetricApplied((Metric) this);
		
	}

	private int getNumPaths() {
		int numPaths;
		BotAnalyser botAnalyser;
		
		botAnalyser = new BotAnalyser();
		numPaths = 0;
		if(botIn!= null)
		{
			for(UserInteraction userIn: botIn.getFlows())
			{
				numPaths += botAnalyser.analyseNumPaths(userIn);
				
				//+1 for each element
				numPaths++;
			}
		}
		return numPaths;
	}



}
