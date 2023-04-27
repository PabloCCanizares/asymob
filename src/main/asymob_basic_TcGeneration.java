package main;

import core.Asymob;

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
			strPathIn = "/localSpace/chatbots/CongaModels/bikeShop.xmi";
			strPathOut = "/localSpace/chatbots/bikeShop";			
		}
		
		if(botTester.loadChatbot(strPathIn))
		{
			botTester.printBotSummary();
			botTester.generateTestCases(strPathOut);
		}
	}
}
