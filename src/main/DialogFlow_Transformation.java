package main;

import transformation.ITransformation;
import transformation.BotToAgent;

public class DialogFlow_Transformation {

	public static void main(String argv[])
	{
		ITransformation transform;
		
		transform = new BotToAgent();
		
		transform.transform("/localSpace/chatbots/CongaModels/bikeShop.xmi", "/localSpace/chatbots/CongaModels/bikeShop_dialogFlow");
	}
}
