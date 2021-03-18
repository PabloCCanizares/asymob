package main;

import core.Asymob;
import metrics.MetricOperatorsSet;
import metrics.operators.bot.NumEntities;
import metrics.operators.bot.NumFlows;
import metrics.operators.bot.NumIntents;
import metrics.operators.bot.NumLanguages;
import metrics.operators.entity.AverageSynonyms;
import metrics.operators.entity.EntityWordLenght;
import metrics.operators.entity.NumLiterals;
import metrics.operators.flow.FlowActionsAverage;
import metrics.operators.flow.FlowLength;
import metrics.operators.flow.FlowNumPaths;
import metrics.operators.globalflow.AveragePathsFlow;
import metrics.operators.globalflow.FlowAverageLen;
import metrics.operators.globalflow.NumPaths;
import metrics.operators.intents.IntentAvgWordsPerPhrase;
import metrics.operators.intents.IntentNumPhrases;

public class asymob_metrics {

	public static void main(String[] argv)
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		String strReport;
		
		botTester = new Asymob();
		metricOps = new MetricOperatorsSet();
		
		//Bot metrics
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumLanguages());
		metricOps.insertMetric(new NumFlows());
		metricOps.insertMetric(new NumPaths());
		metricOps.insertMetric(new FlowAverageLen());
		metricOps.insertMetric(new AveragePathsFlow());
		
		//Entity metrics
		metricOps.insertMetric(new NumLiterals());
		metricOps.insertMetric(new AverageSynonyms());
		metricOps.insertMetric(new EntityWordLenght());
		
		//Flow metrics
		metricOps.insertMetric(new FlowNumPaths());
		metricOps.insertMetric(new FlowLength());
		metricOps.insertMetric(new FlowActionsAverage());
		
		//Intent metrics
		metricOps.insertMetric(new IntentNumPhrases());
		metricOps.insertMetric(new IntentAvgWordsPerPhrase());
		
		//if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop_short.xmi"))
		if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop.xmi"))
		//if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/mysteryAnimal.xmi"))
		{
			if(botTester.measureMetrics(metricOps))
			{
				strReport = botTester.getMetricReport();
				System.out.println(strReport);
			}
		}
		else
		{
			System.out.println("The file does not exists!");
		}
	}
}
