package main.experiments;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import auxiliar.Common;
import core.Asymob;
import metrics.MetricOperatorsSet;
import metrics.operators.bot.BotConfusingPhrases;
import metrics.operators.bot.BotFleschReadingScore;
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
import metrics.operators.bot.globalIntents.MinIntentNumPhrases;
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
import metrics.operators.intents.IntentNumPhrasesNFWI;
import metrics.operators.intents.IntentNumRegularExpressions;
import metrics.operators.intents.IntentTrainingSentiment;
import metrics.reports.CsvReportGen;
import metrics.reports.LatexReportGen;
import metrics.reports.MetricReportGenerator;
import metrics.reports.StringReport;

/**
 * Main method for extracting different chatbot metrics
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class asymob_metrics_TSE_extended {

	public static void main(String[] argv)
	{
		System.out.println("Asymob empirical study for TSE");
		//testSingle("/home/j0hn/Documents/GitHub/asymob/TSE_dataset/Dialogflow/"+"your_song.xmi");
		testMulti();
	}
	private static MetricOperatorsSet createMetricOperators() {
		MetricOperatorsSet metricOps = new MetricOperatorsSet();
		
		//Global metrics
		/*metricOps.insertMetric(new NumIntents());
		metricOps.insertMetric(new NumEntities());
		metricOps.insertMetric(new NumFlows());		
		metricOps.insertMetric(new NumPaths());	*/	
		//metricOps.insertMetric(new BotConfusingPhrases());
		//metricOps.insertMetric(new BotOutputSentiment());
		//metricOps.insertMetric(new BotFleschReadingScore());
		
		//Intent metrics
		//metricOps.insertMetric(new AvgIntentNumPhrases());			 //TPI
		//metricOps.insertMetric(new AvgIntentRegularExpressions());	 //RPI
		//metricOps.insertMetric(new AvgIntentWordPerTrainingPhrase());//WPTP
		//metricOps.insertMetric(new AvgIntentVerbPerPhrase());		 //VPTP
		//metricOps.insertMetric(new AvgIntentParameters());			 //PPTP
		//metricOps.insertMetric(new AvgWordsPerOutputPhrase()); 		 //WPOP	
		//metricOps.insertMetric(new AvgVerbsPerOutputPhrase()); 		 //VPOP
		//metricOps.insertMetric(new AvgIntentCharsPerOutputPhrase()); //CPOP
		//metricOps.insertMetric(new AvgIntentReadingTime());			 //READ
		metricOps.insertMetric(new MinIntentNumPhrases());
		/*
		//Entity
		metricOps.insertMetric(new AvgEntityLiterals());			//LPE
		metricOps.insertMetric(new AvgEntitySynonyms());			//SPL
		metricOps.insertMetric(new AvgEntityWordLen());				//WL
		
		//Flow metrics
		metricOps.insertMetric(new AvgActionsPerFlow());			//FACT
		metricOps.insertMetric(new AvgPathsFlow());					//PATH
		metricOps.insertMetric(new AvgFlowLen());					//CL	
		*/
		metricOps.insertMetric(new IntentNumPhrasesNFWI());
		//Base
		//TODO: Insertar dependencias y que se calculen solas (Ej: AvgEntityLiterals -> NumLiterals)
		/*metricOps.insertMetric(new IntentTrainingSentiment());
		metricOps.insertMetric(new IntentAvgVerbsPerTrainingPhrase());
		metricOps.insertMetric(new IntentNumPhrases());
		metricOps.insertMetric(new IntentNumParameters());
		metricOps.insertMetric(new IntentAvgWordsPerTrainingPhrase());
		metricOps.insertMetric(new IntentAvgCharsPerTrainingPhrase());
		metricOps.insertMetric(new NumLiterals());
		metricOps.insertMetric(new AverageSynonyms());
		metricOps.insertMetric(new EntityWordLenght());
		metricOps.insertMetric(new FlowActionsAverage());
		metricOps.insertMetric(new IntentNumRegularExpressions());*/
		
		return metricOps;
	}
	public static void testMulti()
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		LinkedList<String> listReport, botList, botList1, botList2;
		int nIndex;
		
		listReport = new LinkedList<String>();
		metReport = new StringReport();
	//	metricReport = new LatexReportGen();
		metricReport = new CsvReportGen();
		botTester = new Asymob();
		
		
		metricOps = createMetricOperators();
		nIndex = 0;
		
		//Single bot		
		//botList = getSingleBot("/home/j0hn/Documents/GitHub/asymob/TSE_dataset/Dialogflow/hotel-booking-jawwadturabi.xmi");
		
		//#DF G
		//botList1 = getGithubFullList("/home/j0hn/Documents/GitHub/asymob/TSE_dataset/Dialogflow");
		
		//Rasa
		botList = getGithubFullList("/home/j0hn/Documents/GitHub/asymob/TSE_dataset/Rasa/full");
		
		//botList = Common.mergeLists(botList1, botList2);
		
		//botList = getGithubFullList();
		if(botList != null)
		{
			for(String strBotPath: botList)
			{
				try
				{
					System.out.printf("Analysing bot %d in path: %s\n", nIndex, strBotPath);
					if(botTester.loadChatbot(strBotPath))
					{
						botTester.measureMetrics(metricOps);
						
						
						metReport = (StringReport) botTester.getMetricReport(metricReport);
						if(metReport != null)
						{
							listReport.add(metReport.getReport());
							System.out.println(metReport.getReport());
						}
						
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
				catch(StackOverflowError e){
		            System.err.println("Stack overflow!");
		        }
				nIndex++;
			}
			metReport = (StringReport) botTester.getMetricReport(metricReport);
			if(metReport != null)
			{
				listReport.add(metReport.getReport());
				System.out.println(metReport.getReport());
			}
		}
		
		
	}
	private static LinkedList<String> getGithubFullList(String strDir) {
		
		LinkedList<String> lRet;
		//Read from disk
		System.out.println("Loading the chatbots of the directory: "+strDir);
		lRet = listFilesUsingDirectoryStream(strDir);
		return lRet;
	}
	private static LinkedList<String> getSingleBot(String strDir) {
			
			LinkedList<String> lRet;

			lRet = new LinkedList<String>();
			lRet.add(strDir);

			return lRet;
		}

	
	public static void testSingle(String strPath)
	{
		Asymob botTester;
		MetricOperatorsSet metricOps;
		StringReport metReport;
		MetricReportGenerator metricReport;
		LinkedList<String> listReport;
		
		listReport = new LinkedList<String>();
		metReport = new StringReport();
		//metricReport = new LatexReportGen();
		metricReport = new CsvReportGen();
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

	public static LinkedList<String> listFilesUsingDirectoryStream(String dir) {
		LinkedList<String> fileList = new LinkedList<String>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileList.add(dir+File.separator+ path.getFileName()
	                    .toString());
	            }
	        }
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fileList= null;
		}
	    return fileList;
	}
	
	public static LinkedList<String> getGithubFullList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("model"+File.separator+"bikeShop.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"iot.xmi");
		return retList;
	}
}

//Busqueda: filename:*bot extension:zip