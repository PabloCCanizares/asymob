package main;

import java.io.File;

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
import metrics.operators.intents.IntentAvgCharsPerPhrase;
import metrics.operators.intents.IntentAvgNounsPerPhrase;
import metrics.operators.intents.IntentAvgVerbsPerPhrase;
import metrics.operators.intents.IntentAvgWordsPerPhrase;
import metrics.operators.intents.IntentMaxWordLen;
import metrics.operators.intents.IntentNumParameters;
import metrics.operators.intents.IntentNumPhrases;
import metrics.operators.intents.IntentReadabilityMetrics;
import metrics.operators.intents.IntentTrainingSentiment;

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
		metricOps.insertMetric(new IntentAvgCharsPerPhrase());
		metricOps.insertMetric(new IntentNumParameters());
		metricOps.insertMetric(new IntentAvgNounsPerPhrase());
		metricOps.insertMetric(new IntentAvgVerbsPerPhrase());
		metricOps.insertMetric(new IntentTrainingSentiment());
		metricOps.insertMetric(new IntentMaxWordLen());
		metricOps.insertMetric(new IntentReadabilityMetrics());
		
		//if(botTester.loadChatbot("model\\bikeShop.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Flights.zip")) -> Flights no carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Support.zip")) -> Support no carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"MediCare.zip")) -> No carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"interviewer.zip")) -> No carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"andreanaji007.zip")) -> No carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"avaire.zip")) -> No carga
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Date.zip")) OK
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Car.zip")) OK
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"pizza.zip")) OK		
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"hotel-booking.zip")) OK
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"googleChallenge.zip")) OK
		if(botTester.loadChatbot("chatbots"+File.separator+"conga"+File.separator+"bikeShop.xmi"))
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
