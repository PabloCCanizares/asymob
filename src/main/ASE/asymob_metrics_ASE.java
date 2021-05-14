package main.ASE;

import java.io.File;
import java.util.LinkedList;

import core.Asymob;
import metrics.MetricOperatorsSet;
import metrics.operators.bot.BotConfusingPhrases;
import metrics.operators.bot.BotOutputSentiment;
import metrics.operators.bot.BotTrainingSentiment;
import metrics.operators.bot.NumEntities;
import metrics.operators.bot.NumFlows;
import metrics.operators.bot.NumIntents;
import metrics.operators.bot.NumLanguages;
import metrics.operators.bot.globalEntities.AvgEntityLiterals;
import metrics.operators.bot.globalEntities.AvgEntitySynonyms;
import metrics.operators.bot.globalEntities.AvgEntityWordLen;
import metrics.operators.bot.globalIntents.AvgIntentCharsPerOutputPhrase;
import metrics.operators.bot.globalIntents.AvgIntentNumPhrases;
import metrics.operators.bot.globalIntents.AvgIntentParameters;
import metrics.operators.bot.globalIntents.AvgIntentReadingTime;
import metrics.operators.bot.globalIntents.AvgIntentReqParameters;
import metrics.operators.bot.globalIntents.AvgIntentVerbPerPhrase;
import metrics.operators.bot.globalIntents.AvgIntentWordPerPhrase;
import metrics.operators.bot.globalIntents.AvgWordsPerOutputPhrase;
import metrics.operators.bot.globalflow.AvgPathsFlow;
import metrics.operators.bot.globalflow.AvgActionsPerFlow;
import metrics.operators.bot.globalflow.AvgFlowLen;
import metrics.operators.bot.globalflow.NumPaths;
import metrics.operators.entity.AverageSynonyms;
import metrics.operators.entity.EntityWordLenght;
import metrics.operators.entity.NumLiterals;
import metrics.operators.flow.FlowActionsAverage;
import metrics.operators.flow.FlowLength;
import metrics.operators.flow.FlowNumPaths;
import metrics.operators.intents.IntentAvgCharsPerTrainingPhrase;
import metrics.operators.intents.IntentAvgCosineSimilarity;
import metrics.operators.intents.IntentAvgNounsPerPhrase;
import metrics.operators.intents.IntentAvgVerbsPerPhrase;
import metrics.operators.intents.IntentAvgWordsPerPhrase;
import metrics.operators.intents.IntentMaxWordLen;
import metrics.operators.intents.IntentNumParameters;
import metrics.operators.intents.IntentNumPhrases;
import metrics.operators.intents.IntentReadabilityMetrics;
import metrics.operators.intents.IntentTrainingSentiment;
import metrics.reports.LatexReportGen;
import metrics.reports.MetricReportGenerator;
import metrics.reports.StringReport;
import metrics.reports.StringReportGen;

/**
 * Main method for extracting different chatbot metrics
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class asymob_metrics_ASE {

	public static void main(String[] argv)
	{
		System.out.println("Asymob empirical study for ASE");
		//testSingle();
		testMulti();
	}
	private static MetricOperatorsSet createMetricOperators() {
		MetricOperatorsSet metricOps = new MetricOperatorsSet();
		//Global metrics
		metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumFlows());		
		metricOps.insertMetric(new NumPaths());
		metricOps.insertMetric(new AvgIntentReadingTime());
		metricOps.insertMetric(new BotConfusingPhrases());
		metricOps.insertMetric(new BotOutputSentiment());
		
		//Intent metrics
		metricOps.insertMetric(new AvgIntentNumPhrases());
		metricOps.insertMetric(new AvgIntentWordPerPhrase());
		metricOps.insertMetric(new AvgIntentVerbPerPhrase());
		metricOps.insertMetric(new AvgIntentParameters());
		metricOps.insertMetric(new AvgWordsPerOutputPhrase()); //WPOP			
		metricOps.insertMetric(new AvgIntentCharsPerOutputPhrase()); //CPOP
		
		//Entity
		metricOps.insertMetric(new AvgEntityLiterals());
		metricOps.insertMetric(new AvgEntitySynonyms());
		metricOps.insertMetric(new AvgEntityWordLen());
		
		//Flow metrics
		metricOps.insertMetric(new AvgFlowLen());
		metricOps.insertMetric(new AvgPathsFlow());
		metricOps.insertMetric(new AvgActionsPerFlow());
		
		//Base
		//TODO: Insertar dependencias y que se calculen solas (Ej: AvgEntityLiterals -> NumLiterals)
		metricOps.insertMetric(new IntentTrainingSentiment());
		metricOps.insertMetric(new IntentAvgVerbsPerPhrase());
		metricOps.insertMetric(new IntentNumPhrases());
		metricOps.insertMetric(new IntentNumParameters());
		metricOps.insertMetric(new IntentAvgWordsPerPhrase());
		metricOps.insertMetric(new IntentAvgCharsPerTrainingPhrase());
		metricOps.insertMetric(new NumLiterals());
		metricOps.insertMetric(new AverageSynonyms());
		metricOps.insertMetric(new EntityWordLenght());
		
		return metricOps;
	}
	public static void testMulti()
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		LinkedList<String> listReport, botList;
		
		listReport = new LinkedList<String>();
		metReport = new StringReport();
		metricReport = new LatexReportGen();
		botTester = new Asymob();
		
		
		metricOps = createMetricOperators();
		
		botList = getBotList();
		//botList = getBotPrebuildList();

		if(botList != null)
		{
			for(String strBotPath: botList)
			{
				try
				{
					System.out.printf("Analysing bot in path: %s\n", strBotPath);
					if(botTester.loadChatbot(strBotPath))
					{
						botTester.measureMetrics(metricOps);
					}
					else
					{
						System.out.println("The file does not exists!");
					}
				}
				catch(Exception e)
				{
					System.out.println("Exception catched: "+e.getMessage());
				}
			}
			metReport = (StringReport) botTester.getMetricReport(metricReport);
			if(metReport != null)
			{
				listReport.add(metReport.getReport());
				System.out.println(metReport.getReport());
			}
		}
		
		
	}
	public void testSingle()
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		LinkedList<String> listReport, botList;
		
		listReport = new LinkedList<String>();
		metReport = new StringReport();
		metricReport = new LatexReportGen();
		botTester = new Asymob();
		
		
		metricOps = createMetricOperators();
		
		//if(botTester.loadChatbot("model"+File.separator+"bikeShop.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"conga"+File.separator+"bikeShop.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip"))
		if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"HOTEL-BOOKING-AGENT2.zip"))
		{
			if(botTester.measureMetrics(metricOps))
			{
				metReport = (StringReport) botTester.getMetricReport(metricReport);
				if(metReport != null)
				{
					listReport.add(metReport.getReport());
					System.out.println(metReport.getReport());
				}
				
			}
		}
		else
		{
			System.out.println("The file does not exists!");
		}
	}
	public static LinkedList<String> getBotList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("model"+File.separator+"bikeShop.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"iot.xmi");
		/*retList.add("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"smallTalk.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HOTEL-BOOKING-AGENT2.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HumanHandoffDemonstrationAgent.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malikasinger1.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"googleChallenge.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malaynayak.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hotel-booking.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"woman.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizza.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hrc.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizzabot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"savelee_demo.zip");*/
		return retList;
	}
	public static LinkedList<String> getBotPrebuildList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Agent-Name.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"App-Management.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Car.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Coffee-Shop.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Currency-Converter.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Date.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Device.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Dining-Out.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Easter-Eggs.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"FAQ.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Food-Delivery.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Formats.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Hotel-Booking.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"support.zip");
		
		
		return retList;
	}	
}
