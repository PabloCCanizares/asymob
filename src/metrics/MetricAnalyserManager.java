package metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import auxiliar.BotResourcesManager;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.EntityMetricBase;
import metrics.operators.base.FlowMetricBase;
import metrics.operators.base.IntentMetricBase;

public class MetricAnalyserManager implements IMetricAnalyser {

	MetricReport metricReport;
	
	public MetricAnalyserManager() {
		
		metricReport = new MetricReport();
	}
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
			analyseEntityMetrics(botIn, metricOps);
			
			//Intent metrics
			analyseIntentMetrics(botIn, metricOps);
			
			//Others
		}
		catch(Exception e)
		{
			bRet = false;
		}
		return bRet;
	}


	private void analyseIntentMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		MetricValue metricRes;
		
		while(metricOps.hasNextIntentMetric())
		{
			try
			{
				metricIn = metricOps.getNextIntentMetric();
				
				for(Intent intentIn: botIn.getIntents())
				{
					//Configure the instance
					if(metricIn instanceof IntentMetricBase)
						((IntentMetricBase)metricIn).configure(intentIn);
					
					//Calculate the metric
					metricIn.calculateMetric();
					
					//Get the results
					metricRes =  metricIn.getResults();

					//Store the results
					if(metricRes != null)
						metricReport.addIntentMetric(intentIn, metricRes);
				}
				
			}catch(Exception e)
			{
				//Exception while processing a bot metric
				System.out.println("[analyseIntentMetrics] Exception catched: "+e.getMessage());
			}
		}
	}
	private void analyseFlowMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		MetricValue metricRes;
		
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

					//Store the results
					if(metricRes != null)
						metricReport.addFlowMetric(flowIn, metricRes);
				}
				
			}catch(Exception e)
			{
				//Exception while processing a bot metric
			}
		}
	}

	private boolean analyseBotMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		MetricValue metricRes;
		boolean bRet;
		
		bRet = true;
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
				
				if(metricRes != null)
				{
					//Store the results
					metricReport.addBotMetric(metricRes);
				}
				
			}catch(Exception e)
			{
				//Exception while processing a bot metric
				System.out.println("Exception while calculating a bot metric");
			}
		}
		return bRet;
	}
	private void analyseEntityMetrics(Bot botIn, MetricOperatorsSet metricOps) {
		Metric metricIn;
		MetricValue metricRes;
		
		while(metricOps.hasNextEntityMetric())
		{
			try
			{
				metricIn = metricOps.getNextEntityMetric();
				
				for(Entity enIn: botIn.getEntities())
				{
					//Configure the instance
					if(metricIn instanceof EntityMetricBase)
						((EntityMetricBase)metricIn).configure(enIn);
					
					//Calculate the metric
					metricIn.calculateMetric();
					
					//Get the results
					metricRes =  metricIn.getResults();
					
					if(metricRes != null)
						//Store the results
						metricReport.addEntityMetric(enIn, metricRes);
				}
				
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
	public String getMetricsReport() {
	
		return metricReport.getStringReport();
	}
	
	public String logMetricReport() {
		String strRet;
		
		strRet = "";
		/*if(botMetrics != null)
		{
			System.out.println("============================");
			System.out.println("BOT METRICS");
			for(MetricValue met: botMetrics)
			{
				System.out.printf("%s = %s [%s]\n", met.getMetricApplied(), met.getValue(), met.getUnit());
			}
			
		}*/
		return strRet;
	}
	@Override
	public boolean doAnalyse(String strPathIn, MetricOperatorsSet metricOps) {
		boolean bRet;
		BotResourcesManager botLoader;
		Bot botIn;
		
		bRet = false;
		botLoader = new BotResourcesManager();
		if(botLoader.loadChatbot(strPathIn))
		{
			botIn = botLoader.getCurrentBot();
			bRet = this.analyseBotMetrics(botIn, metricOps);
		}
		return bRet;
	}

}
