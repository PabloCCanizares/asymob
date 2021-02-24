package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import analyser.BotAnalyser;
import aux.BotPrinter;
import generator.Bot;
import generator.GeneratorPackage;
import generator.Intent;
import operators.base.MutationOperatorSet;
import testCases.ITestCaseGenerator;
import testCases.TcGenBotium;
import training.TrainPhraseGenerator;
import transformation.ITransformation;
import validation.BotValidation_General;
import validation.BotValidator;

public class Asymob {

	private Resource botResource = null;
	private ResourceSet botResourceSet = null;	
	private Bot currentBot = null;
	private BotValidator botValidator = null;
	private TrainPhraseGenerator trainPhraseGen = null;
	private BotAnalyser botAnalyser = null;
	private ITransformation botTransformation = null;	
	public Asymob()
	{
		trainPhraseGen = new TrainPhraseGenerator();
		botValidator = new BotValidation_General();
	}
	public void setTransformation(ITransformation botTransformation)
	{
		this.botTransformation = botTransformation;
	}
	/**
	 * Loads a chatbot, given its path.
	 * @param strPath
	 * @return
	 */
	public boolean loadChatbot(String strPath)
	{
		File file;
		boolean bRet;
		
		file = new File(strPath);
		bRet = false;
		
		if (file.exists()) {
			try {
				botResource = getResourceSet().getResource(URI.createURI(file.getAbsolutePath()), true);
				botResource.load(null);
				botResource.getAllContents();
				currentBot = (Bot) botResource.getContents().get(0);
				bRet = true;
				
				//Print the bot
				BotPrinter.printBot(currentBot);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("testConga - The file doest not exists!!");		
		
		return bRet;
	}
	
	public boolean saveToDisk(String strPath)
	{
		OutputStream output;
		boolean bRet;
		
		bRet = true;
		try
		{
			output  = new FileOutputStream(strPath.replaceAll(".xmi", "_copy.xmi"));
			
			//Save a copy to disk
			botResource.save(System.out, null);
			botResource.save(output, null);			
		}
		catch(Exception e)
		{
			System.out.println("[saveChatbot] Exception while saving chatbot to file: "+strPath);
			bRet = false;
		}
		
		return bRet;
	}
	
	/**
	 * This method generates all the training phrases of a chatbot.
	 * @param mutOpSet: Mutation operator set, used to generate the training phrases.
	 * @return
	 */
	public boolean generateTrainingPhrases(MutationOperatorSet mutOpSet) {
		List<Intent> listIntent;
		boolean bRet;
		
		bRet = true;
		
		try
		{
			listIntent =  currentBot.getIntents();
			
			for (Intent intent : listIntent) {
				bRet &= generateTrainingPhrasesByIntentId(intent.getName(), mutOpSet);
			}
		}
		catch(Exception e)
		{
			System.out.println("[generateTrainingPhrases] Exception while creating training phrases: "+e.getMessage());
		}
		return bRet;
		
	}	
	
	/**
	 * This method generates the training phrases of an intent, which is selected given its name.
	 * @param strIntentName: Identifier of the intent, which is used as basis to create the training phrases.
	 * @param mutOpSet: Mutation operator set, used to generate the training phrases.
	 * @return
	 */
	public boolean generateTrainingPhrasesByIntentId(String strIntentName, MutationOperatorSet mutOpSet) {
		Intent botIntent;
		boolean bRet;

		bRet = false;
		try
		{
			botIntent = currentBot.getIntent(strIntentName);
			
			if(botIntent != null)
				bRet = trainPhraseGen.generateTrainingPhraseFull(botIntent, mutOpSet);
		}
		catch(Exception e)
		{
			System.out.println("[generateTrainingPhrasesByIntentId] Exception while creating training phrases: "+e.getMessage());
		}
		return bRet;
		
	}	
	
	public LinkedList<String> getAllIntentsName()
	{
		List<Intent> listIntent;
		LinkedList<String> retList;
		
		retList = null;
		if(currentBot != null)
		{
			retList = new LinkedList<String>();
			listIntent =  currentBot.getIntents();
			
			for (Intent intent : listIntent) {
				retList.add(intent.getName());				
			}
		}
		
		return retList;
	}

	private ResourceSet getResourceSet() {
		if (botResourceSet == null) {
			botResourceSet = new ResourceSetImpl();

			botResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
					new XMIResourceFactoryImpl());
			botResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi",
					new XMIResourceFactoryImpl());
			botResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("bot",
					new XMIResourceFactoryImpl());	
			
			botResourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("generator",
							new XMIResourceFactoryImpl());
					
			//Registration
			if (!EPackage.Registry.INSTANCE.containsKey(GeneratorPackage.eNS_URI)) {
				EPackage.Registry.INSTANCE.put(GeneratorPackage.eNS_URI,
						GeneratorPackage.eINSTANCE);
			}
			
		}
		return botResourceSet;
	}
	
	public boolean validateBot()
	{
		//TODO: Create a new package of validation, and include:
		/*
		 * Validaciones de Intents (todos ellos warnings):
			- No debería haber dos frases de entrenamiento iguales
			- Las frases de entrenamiento deberían tener algo más que parametros 
			de tipo textual (si solo tiene parametros textuales pueden emparejarse 
			con cualquier interaccion del usuario)
			- Los intents deberían tener al menos tres frases de entrenamiento o 
			una expresion regular
			- Valido las expresiones regulares
			- Los intents deberían definir tantos lenguajes como los que tiene el bot
			
		 * Validaciones de Flows (todos ellos errores):
			- Dos flujos de conversacion no pueden empezar por el mismo intent
			- Para usar una accion que referencie parametros en el flujo de 
			conversacion, previamente debe estar el intent que contenga el parametro
			- En el fujo de conversación, Antes de una HttpResponse debe ir la 
			HttpRequest que hace referencia
		 */
		return false;
	}
	
	public boolean generateTestCases(String strPath)
	{
		boolean bRet;
		
		bRet = false;
		
		if(currentBot != null)
		{
			ITestCaseGenerator tcGen = new TcGenBotium();
			tcGen.generateTestCases(strPath, currentBot);
		}
				
		return bRet;
	}
	public boolean applyTrainingPhrasesToChatbot() {
		boolean bRet;
		
		try
		{
			bRet = trainPhraseGen.applyTrainingPhrasesToChatbot();
		}
		catch(Exception e)
		{
			bRet = false;
		}

		return bRet;
	}
	public void printBotSummary() {
		BotPrinter botPrinter = new BotPrinter();
		
		botPrinter.printBot(currentBot);
	}
	public boolean transform(String strPathOut) {
		boolean bRet;
		
		try
		{
			bRet = botTransformation.transform(this.currentBot, strPathOut);
		}
		catch(Exception e)
		{
			bRet = false;
			System.out.print("[transform] Exception while transforming bot: ");
			if(botTransformation == null)
				System.out.print("<transformation module is null>");
			if(currentBot == null)
				System.out.print("<the chatbot is null>");
			
			System.out.print("\n");
		}
		
		return bRet;
		
	}
}
