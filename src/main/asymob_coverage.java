package main;

import core.Asymob;
import coverage.CoverageMethod;

public class asymob_coverage {
	
	public static void main(String[] args) {

		Asymob botTester;
		String strPathIn, strPathOut;
		botTester = new Asymob();

		if(args.length>2)
		{
			strPathIn = args[0];
			strPathOut = args[1];
		}
		else
		{
			// /PERSONAL/TRABAJO/MISO/asymob/chatbots/conga/bikeShop.xmi
			//strPathIn = "/localSpace/chatbots/CongaModels/bikeShop.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/Dining-Out.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/googleChallenge.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/Hotel-Booking.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/Car.xmi";
			strPathIn = "/localSpace/chatbots/CongaModels/Education_Chatbot.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/iLearn_Dialogflow2ServiceNow.xmi";
			//strPathIn = "/localSpace/chatbots/CongaModels/bikeShop.xmi";
			
			strPathOut = "/localSpace/chatbots/CongaModels/";			
		}

		if(botTester.loadChatbot(strPathIn))
		{

			// Medium
			// Exhaustive
			// Basic			
			String method = "Exhaustive";
			botTester.printBotSummary();
			System.out.printf("Intent coverage: %.02f \n",botTester.measureCoverage(CoverageMethod.eINTENT));
			System.out.printf("Parameter coverage: %.02f \n",botTester.measureCoverage(CoverageMethod.ePARAM));
			System.out.printf("Entity coverage: %.02f \n",botTester.measureCoverage(CoverageMethod.eENTITY));
			System.out.printf("Entity coverage: %.02f \n",botTester.measureCoverage(CoverageMethod.eLITERAL));
		}
	}
}
