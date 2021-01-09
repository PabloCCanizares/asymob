package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import aux.JavaRunCommand;

import generator.Bot;
import generator.GeneratorPackage;
import generator.Intent;
import generator.IntentLanguageInputs;
import operators.MutActiveToPassiveOp;
import operators.MutAdjectivesToSynonymsOp;
import operators.MutChangeWordToNumberOp;
import operators.MutObjectsToSynonymsOp;
import operators.MutPassiveToActiveOp;
import operators.MutTraductionChainedOp;
import operators.MutateUtteranceOp;
import operators.base.MutantOperatorBuilder;
import operators.base.MutationOperator;
import utteranceVariantCore.VariantGenByCommand;

public class test {

	private static ResourceSet resourceSet = null;
	private static Resource resource = null;
	public static void main(String[] args) {
		
		analyseBot("/localSpace/chatbots/CongaModels/bikeShop.xmi");
	}

	public static void testLoad()
	{
		ResourceSet resourceSet = new ResourceSetImpl();

        // register UML
		

		File file = new File("/home/j0hn/eclipse-workspace-dsl/org.eclipse.example.bowlingmodel/My1.bowling");		
		if (file.exists()) {
			try {
				resource = getResourceSet().getResource(URI.createURI(file.getAbsolutePath()), true);				
				resource.load(null);
				resource.getAllContents();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		
	}
	public static void analyseBot(String strPath)
	{
		ResourceSet resourceSet;	
        // register UML
		JavaRunCommand runComm;
		TrainPhraseGenerator testGen;
		OutputStream output;
		MutantOperatorBuilder opBuilder;
		MutationOperator mutOp, mutOp2, mutOp3, mutOp4, mutOp5, mutOp6, mutOp7, mutOp8, mutOp9;
		
		System.out.println("testConga - Init");
		
		//Initialise variables
		runComm = new JavaRunCommand();
		resourceSet  = new ResourceSetImpl();	
		opBuilder = new MutantOperatorBuilder();
				
		File file = new File(strPath);
		if (file.exists()) {
			try {
				output  = new FileOutputStream(strPath.replaceAll(".xmi", "_copy.xmi"));
				
				resource = getResourceSet().getResource(URI.createURI(file.getAbsolutePath()), true);
				resource.load(null);
				resource.getAllContents();
				Bot bot = (Bot) resource.getContents().get(0);
				
				//Print the bot
				BotPrinter.printBot(bot);
				
								
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
				//2-Empaquetarlo y meterlo en un proyecto nuevo
				
				/*MutOpsCfg.getInstance().insertOperator(mutOp);
				MutOpsCfg.getInstance().insertOperator(mutOp2);
				MutOpsCfg.getInstance().insertOperator(mutOp3);
				MutOpsCfg.getInstance().insertOperator(mutOp4);
				MutOpsCfg.getInstance().insertOperator(mutOp6);
				MutOpsCfg.getInstance().insertOperator(mutOp7);
				MutOpsCfg.getInstance().insertOperator(mutOp8);*/
				MutOpsCfg.getInstance().insertOperator(mutOp9);
				
				testGen = new TrainPhraseGenerator();
				
				if(testGen.generateTrainingPhraseFull(bot.getIntent("Hours"), MutOpsCfg.getInstance()))
				{
					if(testGen.generateTrainingPhraseFull(bot.getIntent("Make Appointment"), MutOpsCfg.getInstance()))
					{
						//Save a copy to disk
						resource.save(System.out, null);
						resource.save(output, null);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("testConga - The file doest not exists!!");
		
		System.out.println("testConga - End");
		
	}	
		
	private static LinkedList<String> createLangList(String string) {
		LinkedList<String> retList;		
		retList = new LinkedList<String>(Arrays.asList(string.split(","))); 
		return 	retList;	 
	}

	public static ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = new ResourceSetImpl();

			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new XMIResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",
					new XMIResourceFactoryImpl());
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bowling",
					new XMIResourceFactoryImpl());		
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bot",
					new XMIResourceFactoryImpl());	
			
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("generator",
							new XMIResourceFactoryImpl());
					
			
			if (!EPackage.Registry.INSTANCE.containsKey(GeneratorPackage.eNS_URI)) {
				EPackage.Registry.INSTANCE.put(GeneratorPackage.eNS_URI,
						GeneratorPackage.eINSTANCE);
			}
			
		}
		return resourceSet;
	}
	
	public static void extractAllIntentInputs(Bot botIn)
	{
		List<Intent> listIntent;
		List<IntentLanguageInputs> listLanguages;
		if(botIn != null)
		{
			//
			listIntent =  botIn.getIntents();
			
			for (Intent intent : listIntent) {
				listLanguages = intent.getInputs();
			}
		}
	}
}

//Nos quedamos en el punto de adaptar el test.py para permitir la ejecucion de los distintos operadores

