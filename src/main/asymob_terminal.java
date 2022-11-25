package main;

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
import metrics.operators.bot.globalIntents.AvgIntentWordPerTrainingPhrase;
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
import metrics.operators.intents.IntentAvgVerbsPerTrainingPhrase;
import metrics.operators.intents.IntentAvgWordsPerTrainingPhrase;
import metrics.operators.intents.IntentMaxWordLen;
import metrics.operators.intents.IntentNumParameters;
import metrics.operators.intents.IntentNumPhrases;
import metrics.operators.intents.IntentReadabilityMetrics;
import metrics.operators.intents.IntentTrainingSentiment;
import metrics.reports.MetricReportGenerator;
import metrics.reports.StringReport;
import metrics.reports.StringReportGen;

public class asymob_terminal {

	public static void main(String[] args) {
		Asymob botTester;
		String strPath;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		
		System.out.println("This is Asym0b 1.0");
		
		if (args.length > 0) {
		    try {
		    	strPath = args[0];
				metReport = new StringReport();
				metricReport = new StringReportGen();
				
				metricOps = generateMetricOps();
				botTester = new Asymob();
				
				System.out.println("Measuring the chatbot: "+strPath);
				
				if(botTester.loadChatbot(strPath))
				{
					System.out.println("Chatbot loaded Ok");
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
				
		    } catch (NumberFormatException e) {
		        System.err.println("Argument" + args[0] + " must be an integer.");
		        System.exit(1);
		    }
		}
	}

	private static MetricOperatorsSet generateMetricOps() {
		MetricOperatorsSet metricOps;
		
		metricOps = new MetricOperatorsSet();
		metricOps.insertMetric(new BotOutputSentiment());
		metricOps.insertMetric(new BotConfusingPhrases());
		//Bot metrics
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumLanguages());
		metricOps.insertMetric(new NumFlows());
		metricOps.insertMetric(new NumPaths());
		metricOps.insertMetric(new AvgEntityLiterals());
		metricOps.insertMetric(new AvgEntitySynonyms());
		metricOps.insertMetric(new AvgEntityWordLen());
		metricOps.insertMetric(new AvgFlowLen());
		metricOps.insertMetric(new AvgPathsFlow());
		metricOps.insertMetric(new AvgActionsPerFlow());
		metricOps.insertMetric(new AvgIntentNumPhrases());
		metricOps.insertMetric(new AvgIntentWordPerTrainingPhrase());
		metricOps.insertMetric(new AvgIntentParameters());
		metricOps.insertMetric(new AvgIntentReqParameters());
		metricOps.insertMetric(new AvgIntentCharsPerOutputPhrase());
		metricOps.insertMetric(new AvgIntentReadingTime());
		metricOps.insertMetric(new BotTrainingSentiment());
		metricOps.insertMetric(new BotConfusingPhrases());
		
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
		metricOps.insertMetric(new IntentAvgWordsPerTrainingPhrase());
		metricOps.insertMetric(new IntentAvgCharsPerTrainingPhrase());
		metricOps.insertMetric(new IntentNumParameters());
		metricOps.insertMetric(new IntentAvgNounsPerPhrase());
		metricOps.insertMetric(new IntentAvgVerbsPerTrainingPhrase());
		metricOps.insertMetric(new IntentTrainingSentiment());
		metricOps.insertMetric(new IntentMaxWordLen());
		metricOps.insertMetric(new IntentReadabilityMetrics());
		metricOps.insertMetric(new IntentAvgCosineSimilarity());
		return metricOps;
	}
}
