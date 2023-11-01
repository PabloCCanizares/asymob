package main;

import core.Asymob;
import testCases.strategies.ETestCaseGenerationStrategy;

public class asymob_basic_TcGeneration {

	public static void main(String[] args) {

		Asymob botTester;
		String strPathIn, strPathOut;
		botTester = new Asymob();

		if (args.length > 2) {
			strPathIn = args[0];
			strPathOut = args[1];
		} else {
			
			// /PERSONAL/TRABAJO/MISO/asymob/chatbots/conga/bikeShop.xmi
			strPathIn = "/Users/rdavi/Desktop/Test-Botium/Rasa/Email-WhatsApp-Integration-Chatbot/Email-WhatsApp-Integration-Chatbot.xmi";
			strPathOut = "/PERSONAL/TRABAJO/MISO/asymob-output";

			// DIALOGFLOW
			// /PERSONAL/TRABAJO/MISO/botTest/AST.2024/temp/chatbots/Dialogflow/BikeShopAgent.zip
			// /PERSONAL/TRABAJO/MISO/botTest/AST.2024/temp/chatbots/Dialogflow/Education_Chatbot.zip 
			// /PERSONAL/TRABAJO/MISO/botTest/AST.2024/temp/chatbots/Dialogflow/iLearn_Dialogflow2ServiceNow.zip

			// RASA
			// /Users/rdavi/Desktop/Test-Botium/Rasa/Data-Mining-Chatbot/Data-Mining-Chatbot.xmi
			// /Users/rdavi/Desktop/Test-Botium/Rasa/Email-WhatsApp-Integration-Chatbot/Email-WhatsApp-Integration-Chatbot.xmi
			// /Users/rdavi/Desktop/Test-Botium/Rasa/yassinelamarti/yassinelamarti.xmi
			
			//strPathIn = "/localSpace/chatbots/CongaModels/bikeShop.xmi";
			//strPathOut = "/localSpace/chatbots/CongaModels/bikeShop_test";
		}

		if (botTester.loadChatbot(strPathIn)) {

			// ePARAMEXHAUSTIVE
			// ePARAMMEDIUM	
			// ePARAMBASIC
			// ePATHLOCATION
			// ePATHSTATELOCATOR
			botTester.printBotSummary();
			botTester.generateTestCases(strPathOut, ETestCaseGenerationStrategy.ePARAMBASIC);
		}
	}
}
