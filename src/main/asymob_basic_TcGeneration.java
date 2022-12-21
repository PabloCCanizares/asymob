package main;

import core.Asymob;

public class asymob_basic_TcGeneration {

	public static void main(String[] args) {
		
		Asymob botTester;
		botTester = new Asymob();
		
		//if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop_short.xmi"))
		if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop.xmi"))
		//if(botTester.loadChatbot("/Users/pablocc/Documents/Github/asymob/chatbots/Conga/bikeShop.xmi"))
		{
			botTester.generateTestCases("/localSpace/chatbots/bikeShop");
		}
	}
}
