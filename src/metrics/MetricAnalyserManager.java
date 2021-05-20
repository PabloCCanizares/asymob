package metrics;

import auxiliar.BotResourcesManager;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.UserInteraction;
import metrics.base.Metric;
import metrics.base.MetricValue;
import metrics.db.MetricDataBase;
import metrics.db.ReadOnlyMetricDB;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.EntityMetricBase;
import metrics.operators.base.FlowMetricBase;
import metrics.operators.base.IntentMetricBase;
import metrics.reports.MetricReport;
import metrics.reports.MetricReportGenerator;

public class MetricAnalyserManager implements IMetricAnalyser {

	//MetricReport metricReport;
	MetricDataBase metricDB;
	MetricReportGenerator reportGen;
	
	public MetricAnalyserManager() {
		
		metricDB = new MetricDataBase();
	}
	public void configureReport(MetricReportGenerator genIn)
	{
		genIn.setDB(metricDB);
		this.reportGen = genIn;
	}
	@Override
	public boolean doAnalyse(Bot botIn, MetricOperatorsSet metricOps) {
		boolean bRet;
		
		bRet = true;
		try
		{
			System.out.println("doAnalyse - Init");
			
			//Reset operator set
			metricOps.resetIndex();
			
			//Insert bot in DB
			metricDB.insertNewBot(botIn.getName());
			
			//Intent metrics
			analyseIntentMetrics(botIn, metricOps);
			
			//Analyse Entity metrics
			analyseEntityMetrics(botIn, metricOps);
			
			//Analyse Flow Metrics
			analyseFlowMetrics(botIn, metricOps);
			
			//Analyse Bot metrics (#Intents, #entities, #languages + global flow metrics)
			analyseBotMetrics(botIn, metricOps);
			
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
		
		System.out.println("analyseIntentMetrics - Analysing intent metrics");
		while(metricOps.hasNextIntentMetric())
		{
			try
			{
				metricIn = metricOps.getNextIntentMetric();
				
				for(Intent intentIn: botIn.getIntents())
				{
					//Configure the instance
					if(metricIn instanceof IntentMetricBase)
						((IntentMetricBase)metricIn).configure(botIn, intentIn);
					
					//Calculate the metric
					metricIn.calculateMetric();
					
					//Get the results
					metricRes =  metricIn.getResults();

					//Store the results
					if(metricRes != null)
						metricDB.addIntentMetric(intentIn, metricRes);
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
		
		System.out.println("analyseIntentMetrics - Analysing flow metrics");
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
						metricDB.addFlowMetric(flowIn, metricRes);
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
		System.out.println("analyseIntentMetrics - Analysing bot metrics");
		while(metricOps.hasNextBotMetric())
		{
			try
			{
				metricIn = metricOps.getNextBotMetric();
				
				//Configure the instance
				if(metricIn instanceof BotMetricBase)
					((BotMetricBase)metricIn).configure(botIn, new ReadOnlyMetricDB(metricDB));
								
				//Calculates metrics
				metricIn.calculateMetric();
				
				//GEt the results
				metricRes =  metricIn.getResults();
				
				if(metricRes != null)
				{
					//Store the results
					metricDB.addBotMetric(metricRes);
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
		
		System.out.println("analyseIntentMetrics - Analysing entity metrics");
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
						metricDB.addEntityMetric(enIn, metricRes);
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
	public MetricReport getMetricsReport(MetricReportGenerator metricReport) {
		MetricReport report;
		
		configureReport(metricReport);
		
		if(reportGen != null && reportGen.generateReport())
		{
			report = reportGen.getReport();
		}
		else
			report = null;
		
		return report;
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
	public MetricDataBase getData() {
		return this.metricDB;
	}

}
