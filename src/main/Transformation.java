package main;

import transformation.ITransformation;
import transformation.Transform_Dialogflow;

public class Transformation {

	public static void main(String argv[])
	{
		ITransformation transform;
		
		transform = new Transform_Dialogflow();
		
		transform.transform("/localSpace/chatbots/CongaModels/bikeShop.xmi", "/localSpace/chatbots/CongaModels/bikeShop_dialogFlow");
	}
}
