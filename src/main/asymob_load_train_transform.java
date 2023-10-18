package main;

import core.Asymob;
import transformation.ITransformation;
import transformation.dialogflow.BotToAgent;
import variants.operators.MutObjectsToSynonymsOp;
import variants.operators.base.MutantOperatorBuilder;
import variants.operators.base.MutationOperator;
import variants.operators.base.MutationOperatorSet;

public class asymob_load_train_transform {

	public static void main(String argv[])
	{
		Asymob botTester;
		MutationOperatorSet mutOpSet;
		ITransformation transformation;
		
		System.out.println("[asymob_load_train_transform] Begin");
		
		//Initialise
		botTester = new Asymob();
		transformation = new BotToAgent();
		if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop.xmi"))
		{
			botTester.printBotSummary();
			
			//botTester.generateTestCases("/localSpace/chatbots/CongaModels/bikeShop_test");
			
			mutOpSet = selectMutationOperators();
			//if(botTester.generateTrainingPhrasesByIntentId("Make Appointment - custom", mutOpSet))
			//if(botTester.generateTrainingPhrases(mutOpSet))
			//if(botTester.generateTrainingPhrasesByIntentId("Hours", mutOpSet))
			//if(botTester.generateTrainingPhrasesByIntentId("Make Appointment", mutOpSet))				
			{
				if(botTester.applyTrainingPhrasesToChatbot())
				{
					botTester.setTransformation(transformation);
					botTester.transform("/localSpace/chatbots/CongaModels/bikeShop/DialogFlow");
					botTester.saveToDisk("/localSpace/chatbots/CongaModels/bikeShop_copy.xmi");
				}
			}
		}
		System.out.println("[asymob_load_train_transform] End");
	}
	public static MutationOperatorSet selectMutationOperators()
	{
		MutationOperatorSet mutOpSetRet;
		MutantOperatorBuilder opBuilder;
		MutationOperator mutOp;
		
		opBuilder = new MutantOperatorBuilder();

		mutOp = opBuilder.buildMutationOperator(
				new MutObjectsToSynonymsOp());
		
		mutOpSetRet = new MutationOperatorSet(); 
		mutOpSetRet.insertOperator(mutOp);
		
		
		return mutOpSetRet;
	}
}
