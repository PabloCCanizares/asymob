package main;

import java.util.Arrays;
import java.util.LinkedList;

import core.Asymob;
import variants.VariantGenByCommand;
import variants.operators.MutActiveToPassiveOp;
import variants.operators.MutAdjectivesToSynonymsOp;
import variants.operators.MutChangeWordToNumberOp;
import variants.operators.MutObjectsToSynonymsOp;
import variants.operators.MutPassiveToActiveOp;
import variants.operators.MutTraductionChainedOp;
import variants.operators.MutateUtteranceOp;
import variants.operators.base.MutantOperatorBuilder;
import variants.operators.base.MutationOperator;
import variants.operators.base.MutationOperatorSet;

public class asymob_test {

	public static void main(String[] args) {
		
		Asymob botTester;
		MutationOperatorSet mutOpSet;
		botTester = new Asymob();
		
		//if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop_short.xmi"))
		if(botTester.loadChatbot("/localSpace/chatbots/CongaModels/bikeShop.xmi"))
		{
			botTester.printBotSummary();
			
			//botTester.generateTestCases("/localSpace/chatbots/CongaModels/bikeShop");
			
			mutOpSet = selectMutationOperators();
			//if(botTester.generateTrainingPhrasesByIntentId("Hours", mutOpSet))
			if(botTester.generateTrainingPhrasesByIntentId("Make Appointment", mutOpSet))
			//if(botTester.generateTrainingPhrases(mutOpSet))
			{
				if(botTester.applyTrainingPhrasesToChatbot())
				{
					botTester.saveToDisk("/localSpace/chatbots/CongaModels/bikeShop_copy.xmi");
				}
			}
		}
	}
		
	private static LinkedList<String> createLangList(String strLangs) {
		LinkedList<String> retList;		
		retList = new LinkedList<String>(Arrays.asList(strLangs.split(","))); 
		return 	retList;	 
	}

	@SuppressWarnings("unused")
	public static MutationOperatorSet selectMutationOperators()
	{
		MutationOperatorSet mutOpSetRet;
		MutantOperatorBuilder opBuilder;
		MutationOperator mutOp, mutOp2, mutOp3, mutOp4, mutOp5, mutOp6, mutOp7, mutOp8, mutOp9, mutOp10;
		
		opBuilder = new MutantOperatorBuilder();
		
		mutOp = opBuilder.buildMutationOperator(
				new MutateUtteranceOp(1,1,10,0), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));
		
		mutOp2 = opBuilder.buildMutationOperator(new MutateUtteranceOp(1,1,10,0));
		
		mutOp3 = opBuilder.buildMutationOperator(
				new MutateUtteranceOp(1,1,35,0), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));
		
		mutOp4 = opBuilder.buildMutationOperator(
				new MutChangeWordToNumberOp(), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));
		
		mutOp5 = opBuilder.buildMutationOperator(
				new MutObjectsToSynonymsOp(), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));
		
		mutOp6 = opBuilder.buildMutationOperator(
				new MutAdjectivesToSynonymsOp(1,1,100), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));

		mutOp7 = opBuilder.buildMutationOperator(
				new MutTraductionChainedOp(1,1, createLangList("en,spa,fr,spa,en")), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/ap_adapter.py"));
		
		mutOp8 = opBuilder.buildMutationOperator(
				new MutActiveToPassiveOp(), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/charm_adapter.py"));
		
		mutOp9 = opBuilder.buildMutationOperator(
				new MutPassiveToActiveOp(), 
				new VariantGenByCommand("/localSpace/chatbots/python_tests/spa_adapter.py"));

		mutOp10 = opBuilder.buildMutationOperator(
				new MutObjectsToSynonymsOp());
		
		mutOpSetRet = new MutationOperatorSet(); 
		mutOpSetRet.insertOperator(mutOp10);
		/*mutOpSetRet.insertOperator(mutOp2);
		mutOpSetRet.insertOperator(mutOp3);
		mutOpSetRet.insertOperator(mutOp4);
		mutOpSetRet.insertOperator(mutOp5);
		mutOpSetRet.insertOperator(mutOp6);
		mutOpSetRet.insertOperator(mutOp7);
		mutOpSetRet.insertOperator(mutOp8);
		mutOpSetRet.insertOperator(mutOp9);*/
		
		return mutOpSetRet;
	}
}
