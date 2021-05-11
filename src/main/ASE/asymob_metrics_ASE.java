package main.ASE;

import java.io.File;

import core.Asymob;
import metrics.MetricOperatorsSet;
import metrics.operators.bot.BotConfusingPhrases;
import metrics.operators.bot.BotSentiment;
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
import metrics.operators.bot.globalIntents.AvgIntentWordsPerOutputPhrase;
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
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		
		metReport = new StringReport();
		metricReport = new LatexReportGen();
		botTester = new Asymob();
		metricOps = new MetricOperatorsSet();
		
		//Global metrics
		metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumFlows());		
		metricOps.insertMetric(new NumPaths());
		metricOps.insertMetric(new AvgIntentReadingTime());
		metricOps.insertMetric(new BotConfusingPhrases());
		metricOps.insertMetric(new BotSentiment());
		
		//Intent metrics
		metricOps.insertMetric(new AvgIntentNumPhrases());
		metricOps.insertMetric(new AvgIntentWordPerPhrase());
		metricOps.insertMetric(new AvgIntentVerbPerPhrase());
		metricOps.insertMetric(new AvgIntentParameters());
		metricOps.insertMetric(new AvgIntentWordsPerOutputPhrase()); //WPOP			
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
		metricOps.insertMetric(new IntentAvgWordsPerPhrase());
		metricOps.insertMetric(new IntentAvgCharsPerTrainingPhrase());
		metricOps.insertMetric(new NumLiterals());
		metricOps.insertMetric(new AverageSynonyms());
		metricOps.insertMetric(new EntityWordLenght());
		
		if(botTester.loadChatbot("model"+File.separator+"bikeShop.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"conga"+File.separator+"bikeShop.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip"))
		//if(botTester.loadChatbot("chatbots"+File.separator+"dialogFlow"+File.separator+"HOTEL-BOOKING-AGENT2.zip"))
		{
			if(botTester.measureMetrics(metricOps))
			{
				metReport = (StringReport) botTester.getMetricReport(metricReport);
				System.out.println(metReport.getReport());
			}
		}
		else
		{
			System.out.println("The file does not exists!");
		}
	}
}
