package metrics;

import generator.Bot;
import metrics.base.EMetricUnit;

public class MetricAnalyserManager implements IMetricAnalyser {

	@Override
	public boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps) {
		boolean bRet;
		
		bRet = true;
		try
		{
			//Analyse Bot metrics (#Intents, #entities, #languages + global flow metrics)
			analyseBotMetrics(botIn, metricOps);
			
			//Analyse Global FlowMetrics
			
			//Analyse FlowMetrics
			
			//Entity metrics
			
			//Intent metrics
			
			//Others
		}
		catch(Exception e)
		{
			bRet = false;
		}
		return bRet;
	}

	private void analyseBotMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		EMetricUnit metricRes;
		
		while(metricOps.hasNext())
		{
			try
			{
				metricIn = metricOps.getNextBotMetric();
				
				//Calculates metrics
				metricIn.calculateMetric(botIn);
				
				//GEt the results
				metricRes =  metricIn.getResults();
				
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
