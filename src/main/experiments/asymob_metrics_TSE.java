package main.experiments;

import java.io.File;
import java.util.LinkedList;

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
public class asymob_metrics_TSE {

	public static void main(String[] argv)
	{
		System.out.println("Asymob empirical study for TSE");
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
		metricOps.insertMetric(new BotFleschReadingScore());
		
		//Intent metrics
		metricOps.insertMetric(new AvgIntentNumPhrases());			 //TPI
		//metricOps.insertMetric(new AvgIntentRegularExpressions());	 //RPI
		metricOps.insertMetric(new AvgIntentWordPerTrainingPhrase());//WPTP
		metricOps.insertMetric(new AvgIntentVerbPerPhrase());		 //VPTP
		metricOps.insertMetric(new AvgIntentParameters());			 //PPTP
		metricOps.insertMetric(new AvgWordsPerOutputPhrase()); 		 //WPOP	
		metricOps.insertMetric(new AvgVerbsPerOutputPhrase()); 		 //VPOP
		metricOps.insertMetric(new AvgIntentCharsPerOutputPhrase()); //CPOP
		metricOps.insertMetric(new AvgIntentReadingTime());			 //READ
		
		//Entity
		metricOps.insertMetric(new AvgEntityLiterals());			//LPE
		metricOps.insertMetric(new AvgEntitySynonyms());			//SPL
		metricOps.insertMetric(new AvgEntityWordLen());				//WL
		
		//Flow metrics
		metricOps.insertMetric(new AvgActionsPerFlow());			//FACT
		metricOps.insertMetric(new AvgPathsFlow());					//PATH
		metricOps.insertMetric(new AvgFlowLen());					//CL	
		
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
		
		
		//#DF G
		botList = getGithubFullList();
		//#DF P
		//botList = getBotDFPrebuildFullList();
		
		//botList =  getBotDFPrebuildFullList_loops();
		//botList = getGithubFullList_loops();
		
		//#R G
		//botList = getRasaGithubList();
		//botList = getBotPrebuildFullList();
		//botList = getDFBotPrebuildRedList();
		//botList = getRasaPrebuildRedList();
		//botList = getRasaGithubRedList();
		//botList = getRasaGithubList();
		//botList = getBotDFkomm();
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
				catch(StackOverflowError e){
		            System.err.println("Stack overflow!");
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
	public static void testSingle(String strPath)
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
		/*retList.add("model"+File.separator+"bikeShop.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"iot.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"mysteryAnimal.xmi");
		retList.add("chatbots"+File.separator+"conga"+File.separator+"smallTalk.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HOTEL-BOOKING-AGENT2.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HumanHandoffDAgent.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malikasinger1.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"googleChallenge.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"malaynayak.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hotel-booking.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"woman.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizza.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hrc.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizzabot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"savelee-demo.zip");*/
		
		//Nuevos
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"airportagent.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"airportagent_savelee.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"airportagent_savelee.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"airport_dialogflow.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Alexa.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"AlexaSkill-PAB.zip");
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ally_dialogflow.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"andreanaji007.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"anthrofy.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ASDBot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"askubuntucorpus.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"au-absence-bot-prod.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Aula_6_Pizzaria.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"avaire.zip");

		//B
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"NPI-BESTBOT.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"BankPortal.xmi");
		
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"BasicBot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"basic-slotfilling.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Bbox.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"beta.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"bitsum.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"broken-sequence-jovo.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Buddy1-G1.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Buddy-G7.zip");
		
		//#C
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Carambola-Fruteria.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Cheqin.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ChronoGG.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"city-streets-trivia.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"city-to-city.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"clinica-medica-dialogflowagent-master.xmi");
		
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"communitymeeting.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ConversAI.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"covid-19-agent.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"COVID-19 FAQ Dialogflow.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Crypt-Lender.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"customerVirtualAssistant.zip");
		
		//#D
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Developer-Buddy.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Dialogflow-Tipigee.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"drum-tunner.zip");
		
		//#E
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ekgsBot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"enoreese.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"escaperoom.zip");
		//eznet retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"eznet.zip");
		
		//#F
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"first-project-test.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"foodfinder.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fulfillment-firestone.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fulfillment-multi-locale.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fulfillment-telephony.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fulfillment-temperature-converter.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fulfillment-weather-python.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"fullfilment-translate.zip");
		
		//#G
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"G7-CinemaTicketBot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"G7-PizzaBot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"game-clock.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"gordobbot.zip");
		
		//#H
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ha-austin.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hackathon-group-3.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hackathon-group-10.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"H-F-D_training.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Homie.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"houqi-code-demo.zip");
		//Peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"HR-Bot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"hubot.zip");
		
		//#I
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"in-my-seats-jovo.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"interviewer.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"iotairblower.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"iot-medan.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"iot-minion.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ip6-corpus.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"IRA.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ISU-Jovo-v2.zip");
		
		//#J
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Jovo-BusinessApp.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Jovo-sample.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"jovo-skillrf.zip");
		
		//#K
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"keijiban.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"KIP_telegram_bot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"knowledge-graph.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Kodi-dialogflow.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kube-django-ng.xmi");
		
		//#L
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"language-flashcards.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"laptop-expert.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"libsample-advanced.zip");
		
		//#M
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"marysbikeshop.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"mattermost-chatops.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"McScraggins.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"MediCare.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Miss-Lily.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"mobile-app-shopping-assistant.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"MobileDoctor.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"mountain_humor_dialogflow.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"multimodal-food-apps.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"museum-bot.zip");
		
		//#N
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"name-that-tune.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"oilprice.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"OutOfScopeCorpusTraining.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"parcelLive.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pigmong.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pizzabot_dialogflow	.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"pln_ufabc.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"qai-salesforce-int.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Qwiklabs.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"ramenTimer.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"robomaster.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"RobotCommander.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Robo-waiter.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"russianRoulett.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"SDP-2018-Group-10.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"seiyu_mint_dialogflow.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"SGTravelBot.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"sirimiri.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"sleepy.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"smarthome.zip");
		//peta retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"SrinivasaGolla.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"stockbot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Talk2ServiceNow.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Talk-to-Spam-Robot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"techcrunch-hack-2019.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"tekbeaPrice.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"test-agent-v2-dea27.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"Today-In-History.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"todayWiseSay.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"TPCarBot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"UniRoom.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"unni-line-bot.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"videogame-language-model.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"voiceapplication-sonnar.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"VoiceClues.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"voicecommerce.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"VoiceMemo.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"voxe.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"wellness-tracker-jovo.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"your_song.zip");
		
		return retList;
	}
	public static LinkedList<String> getGithubFullList_loops()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"bucles"+File.separator+"interviewer.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"bucles"+File.separator+"MediCare.xmi");
		
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
	public static LinkedList<String> getBotDFPrebuildFullList()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Hotel-Booking.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Agent-Name.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"App-Management.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Coffee-Shop.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Currency-Converter.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Date.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Device.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Dining-Out.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Easter-Eggs.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Food-Delivery.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Formats.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Job-Interview.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"FAQ.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"support.zip");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"Car.xmi");
		
		return retList;
	}	
	
	public static LinkedList<String> getBotDFPrebuildFullList_loops()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"bucles"+File.separator+"Alarm.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"bucles"+File.separator+"Banking.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"bucles"+File.separator+"Events-Search.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"prebuilt"+File.separator+"bucles"+File.separator+"Flights.xmi");
		
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
		
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"rasa-demo-1.xmi");	
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
		retList.add("chatbots"+File.separator+"rasa"+File.separator+"small-talk-rasa-stack-master.xmi");
		
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
	public static LinkedList<String> getBotDFkomm()
	{
		LinkedList<String> retList;
		
		retList = new LinkedList<String>();
		
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Ecommerce_Bot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Education_Chatbot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Food_Ordering_Chatbot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Insurance_Bot.xmi");
		//ojo este retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"kommunicate-support-bot-sample.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Lead_Collection_Bot.xmi");
		retList.add("chatbots"+File.separator+"dialogFlow"+File.separator+"kommunicate"+File.separator+"Meeting-Booking-Bot.xmi");
		
		
		return retList;
	}
}

//Busqueda: filename:*bot extension:zip