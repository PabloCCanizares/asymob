package main.experiments;

import java.io.File;
import java.util.LinkedList;

import core.Asymob;
import metrics.MetricOperatorsSet;
import metrics.operators.bot.BotConfusingPhrases;
import metrics.operators.bot.BotOutputSentiment;
import metrics.operators.bot.NumEntities;
import metrics.operators.bot.NumFlows;
import metrics.operators.bot.NumIntents;
import metrics.operators.bot.globalEntities.AvgEntityLiterals;
import metrics.operators.bot.globalEntities.AvgEntitySynonyms;
import metrics.operators.bot.globalEntities.AvgEntityWordLen;
import metrics.operators.bot.globalIntents.AvgIntentCharsPerOutputPhrase;
import metrics.operators.bot.globalIntents.AvgIntentNumPhrases;
import metrics.operators.bot.globalIntents.AvgIntentParameters;
import metrics.operators.bot.globalIntents.AvgIntentReadingTime;
import metrics.operators.bot.globalIntents.AvgIntentRegularExpressions;
import metrics.operators.bot.globalIntents.AvgIntentVerbPerPhrase;
import metrics.operators.bot.globalIntents.AvgIntentWordPerTrainingPhrase;
import metrics.operators.bot.globalIntents.AvgVerbsPerOutputPhrase;
import metrics.operators.bot.globalIntents.AvgWordsPerOutputPhrase;
import metrics.operators.bot.globalflow.AvgActionsPerFlow;
import metrics.operators.bot.globalflow.AvgFlowLen;
import metrics.operators.bot.globalflow.AvgPathsFlow;
import metrics.operators.bot.globalflow.NumPaths;
import metrics.operators.entity.AverageSynonyms;
import metrics.operators.entity.EntityWordLenght;
import metrics.operators.entity.NumLiterals;
import metrics.operators.flow.FlowActionsAverage;
import metrics.operators.intents.IntentAvgCharsPerTrainingPhrase;
import metrics.operators.intents.IntentAvgVerbsPerTrainingPhrase;
import metrics.operators.intents.IntentAvgWordsPerTrainingPhrase;
import metrics.operators.intents.IntentNumParameters;
import metrics.operators.intents.IntentNumPhrases;
import metrics.operators.intents.IntentNumRegularExpressions;
import metrics.operators.intents.IntentTrainingSentiment;
import metrics.reports.LatexReportGen;
import metrics.reports.MetricReportGenerator;
import metrics.reports.StringReport;

/**
 * Main method for extracting different chatbot metrics
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class asymob_metrics_ASE {

	public static void main(String[] argv)
	{
		System.out.println("Asymob empirical study for ASE");
		//testSingle("model"+File.separator+"bikeShop.xmi");
		testMulti();
	}
	private static MetricOperatorsSet createMetricOperators() {
		MetricOperatorsSet metricOps = new MetricOperatorsSet();
		
		//Global metrics
		metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumFlows());		
		metricOps.insertMetric(new NumPaths());		
		metricOps.insertMetric(new BotConfusingPhrases());
		metricOps.insertMetric(new BotOutputSentiment());
		
		//Intent metrics
		metricOps.insertMetric(new AvgIntentNumPhrases());			 //TPI
		metricOps.insertMetric(new AvgIntentRegularExpressions());	 //RPI
		metricOps.insertMetric(new AvgIntentWordPerTrainingPhrase());//WPTP
		metricOps.insertMetric(new AvgIntentVerbPerPhrase());		 //VPTP
		metricOps.insertMetric(new AvgIntentParameters());			 //PPTP
		metricOps.insertMetric(new AvgWordsPerOutputPhrase()); 		 //WPOP	
		metricOps.insertMetric(new AvgVerbsPerOutputPhrase()); 		 //VPOP
		metricOps.insertMetric(new AvgIntentCharsPerOutputPhrase()); //CPOP
		metricOps.insertMetric(new AvgIntentReadingTime());			 //READ
		
		//Entity
		metricOps.insertMetric(new AvgEntityLiterals());
		metricOps.insertMetric(new AvgEntitySynonyms());
		metricOps.insertMetric(new AvgEntityWordLen());
		
		//Flow metrics
		metricOps.insertMetric(new AvgActionsPerFlow());
		metricOps.insertMetric(new AvgPathsFlow());
		metricOps.insertMetric(new AvgFlowLen());
		
		//Base
		//TODO: Insertar dependencias y que se calculen solas (Ej: AvgEntityLiterals -> NumLiterals)
		metricOps.insertMetric(new IntentTrainingSentiment());
		metricOps.insertMetric(new IntentAvgVerbsPerTrainingPhrase());
		metricOps.insertMetric(new IntentNumPhrases());
		metricOps.insertMetric(new IntentNumParameters());
		metricOps.insertMetric(new IntentAvgWordsPerTrainingPhrase());
		metricOps.insertMetric(new IntentAvgCharsPerTrainingPhrase());
		metricOps.insertMetric(new NumLiterals());
		metricOps.insertMetric(new AverageSynonyms());
		metricOps.insertMetric(new EntityWordLenght());
		metricOps.insertMetric(new FlowActionsAverage());
		metricOps.insertMetric(new IntentNumRegularExpressions());
		
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
		
		//botList = getGithubRedList();
		//botList = getDFBotPrebuildRedList();
		//botList = getRasaPrebuildRedList();
		//botList = getRasaGithubRedList();
		botList = getRasaGithubList();
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
	public void testSingle(String strPath)
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		LinkedList<String> listReport;
		
		listReport = new LinkedList<String>();
		metReport = new StringReport();
		metricReport = new LatexReportGen();
		botTester = new Asymob();
		
		
		metricOps = createMetricOperators();
		
		if(botTester.loadChatbot(strPath))
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
	public static LinkedList<String> getGithubFullList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("model"+File.separator+"bikeShop.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"iot.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"smallTalk.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HOTEL-BOOKING-AGENT2.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HumanHandoffDAgent.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malikasinger1.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"googleChallenge.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malaynayak.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hotel-booking.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"woman.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizza.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hrc.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizzabot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"savelee-demo.zip");
		return retList;
	}
	public static LinkedList<String> getGithubRedList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hotel-booking.zip");
		retList.add("model"+File.separator+"bikeShop.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"woman.zip");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"smallTalk.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"googleChallenge.zip");
		return retList;
	}	
	public static LinkedList<String> getBotPrebuildFullList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Hotel-Booking.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Agent-Name.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"App-Management.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Coffee-Shop.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Currency-Converter.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Date.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Device.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Dining-Out.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Easter-Eggs.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Food-Delivery.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Formats.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"FAQ.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"support.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Car.zip");
		
		return retList;
	}	
	public static LinkedList<String> getDFBotPrebuildRedList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Dining-Out.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Easter-Eggs.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Coffee-Shop.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Agent-Name.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Car.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"App-Management.zip");
		
		
		return retList;
	}	
	
	public static LinkedList<String> getRasaGithubList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		/*retList.add("chatbots"+File.separator+"rasa"+File.separator+"rasa-demo-1.xmi");	
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"01_smalltalk_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"02_lead_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"03_real_estate_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"04_feedback_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"05_event_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"06_hotel_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"07_survey_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"08_travel_agency_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"09_news_api.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"10_freshdesk_customer_support_bot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"episode8.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"wall-e.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"financial-demo-rasa-1.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"small-talk-rasa-stack-master.xmi");*/
		
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"tutorial-rasa-google-assistant-master.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"rasa-demo-pydata18-master.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"RasaCustomerService-master.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"FAQ-RASA-NLU-master.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"Building-a--.xmi");
		
		return retList;
	}	
	
	public static LinkedList<String> getRasaGithubRedList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"rasa-demo-1.xmi");	
		
		return retList;
	}	
	
	public static LinkedList<String> getRasaPrebuildRedList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"prebuilt"+File.separator+"concertbot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"prebuilt"+File.separator+"formBot.xmi");
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"prebuilt"+File.separator+"moodbot.xmi");
		
		return retList;
	}	
}
