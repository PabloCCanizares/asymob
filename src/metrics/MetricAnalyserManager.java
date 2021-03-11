package metrics;

import generator.Bot;
import generator.UserInteraction;
import metrics.base.EMetricUnit;
import metrics.base.Metric;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.FlowMetricBase;

public class MetricAnalyserManager implements IMetricAnalyser {

	@Override
	public boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps) {
		boolean bRet;
		
		bRet = true;
		try
		{
			//Analyse Bot metrics (#Intents, #entities, #languages + global flow metrics)
			analyseBotMetrics(botIn, metricOps);
			
			//Analyse Flow Metrics
			analyseFlowMetrics(botIn, metricOps);
			
			//Analyse Entity metrics
			
			//Intent metrics
			
			//Others
		}
		catch(Exception e)
		{
			bRet = false;
		}
		return bRet;
	}

	private void analyseFlowMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		EMetricUnit metricRes;
		
		while(metricOps.hasNextFlowMetric())
		{
			try
			{
				metricIn = metricOps.getNextFlowMetric();
				
				for(UserInteraction flowIn: botIn.getFlows())
				{
					//Configure the instance
					if(metricIn instanceof FlowMetricBase)
						((FlowMetricBase)metricIn).configure(flowIn);
					
					//Calculate the metric
					metricIn.calculateMetric();
					
					//Get the results
					metricRes =  metricIn.getResults();
				}
				
			}catch(Exception e)
			{
				//Exception while processing a bot metric
			}
		}
	}

	private void analyseBotMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		EMetricUnit metricRes;
		
		while(metricOps.hasNextBotMetric())
		{
			try
			{
				metricIn = metricOps.getNextBotMetric();
				
				//Configure the instance
				if(metricIn instanceof BotMetricBase)
					((BotMetricBase)metricIn).configure(botIn);
								
				//Calculates metrics
				metricIn.calculateMetric();
				
				//GEt the results
				metricRes =  metricIn.getResults();
				
				//Store the results
				
			}catch(Exception e)
			{
				//Exception while processing a bot metric
			}
		}
		
	}

	@Override
	public void getAnalysisSummary() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getMetricsReport() {
		// TODO Auto-generated method stub
		
	}

}
