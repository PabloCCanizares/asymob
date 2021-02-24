package main;

import core.Asymob;
import operators.MutActiveToPassiveOp;
import operators.MutAdjectivesToSynonymsOp;
import operators.MutChangeWordToNumberOp;
import operators.MutObjectsToSynonymsOp;
import operators.MutPassiveToActiveOp;
import operators.MutTraductionChainedOp;
import operators.MutateUtteranceOp;
import operators.base.MutantOperatorBuilder;
import operators.base.MutationOperator;
import operators.base.MutationOperatorSet;
import transformation.ITransformation;
import transformation.dialogflow.BotToAgent;
import utteranceVariantCore.VariantGenByCommand;

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
			
			//botTester.generateTestCases("/localSpace/chatbots/CongaModels/bikeShop");
			
			mutOpSet = selectMutationOperators();
			//if(botTester.generateTrainingPhrasesByIntentId("Hours", mutOpSet))
			//if(botTester.generateTrainingPhrasesByIntentId("Make Appointment", mutOpSet))
			if(botTester.generateTrainingPhrasesByIntentId("Make Appointment - custom", mutOpSet))
			//if(botTester.generateTrainingPhrases(mutOpSet))
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
