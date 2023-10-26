package main;

import core.Asymob;
import testCases.strategies.ETestCaseGenerationStrategy;

public class asymob_basic_TcGeneration {

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
			//strPathIn = "/PERSONAL/TRABAJO/MISO/asymob/chatbots/conga/bikeShop.xmi";
			//strPathOut = "/PERSONAL/TRABAJO/MISO/asymob-output";
			strPathIn = "/localSpace/chatbots/CongaModels/bikeShop.xmi";
			strPathOut = "/localSpace/chatbots/CongaModels/bikeShop_test";	
		}
		
		if(botTester.loadChatbot(strPathIn))
		{
			
			// Medium
			// Exhaustive
			// Basic			
			String method = "Exhaustive";
			botTester.printBotSummary();
			botTester.generateTestCases(strPathOut, ETestCaseGenerationStrategy.ePARAMEXHAUSTIVE);
		}
	}
}
