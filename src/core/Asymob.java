package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import analyser.BotAnalyser;
import auxiliar.BotPrinter;
import auxiliar.Common;
import generator.Bot;
import generator.GeneratorPackage;
import generator.Intent;
import metrics.IMetricAnalyser;
import metrics.MetricAnalyserManager;
import metrics.MetricOperatorsSet;
import testCases.ITestCaseGenerator;
import testCases.TcGenBotium;
import training.IVariantPhraseGenerator;
import training.VariationsCollectionText;
import training.chaos.ChaosPhraseGenerator;
import training.simple.SimplePhraseGenerator;
import transformation.ITransformation;
import validation.BotValidation_General;
import validation.BotValidatorManager;
import variants.operators.base.MutationOperatorSet;

public class Asymob {

	private Resource botResource = null;
	private ResourceSet botResourceSet = null;	
	private Bot currentBot = null;
	private BotValidatorManager botValidator = null;
	private IVariantPhraseGenerator trainPhraseGen = null;
	private BotAnalyser botAnalyser = null;
	private ITransformation botTransformation = null;
	private IMetricAnalyser botMetrics = null;
	
	public Asymob()
	{
		trainPhraseGen = new SimplePhraseGenerator();
		botValidator = new BotValidation_General();
		botMetrics = new MetricAnalyserManager();
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
				botResource = getResourceSet().getResource(URI.createFileURI(file.getAbsolutePath()), true);
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
			System.out.println("[loadChatbot] - The file does not exists!!");		
		
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
		int nIndex;
		bRet = true;
		
		try
		{
			nIndex = 0;
			listIntent =  currentBot.getIntents();
			
			System.out.println("Total progress "+Common.progressBar(0, listIntent.size()));
			
			for (Intent intent : listIntent) {
				bRet &= generateTrainingPhrasesByIntentId(intent.getName(), mutOpSet);
				
				nIndex++;
				System.out.printf("[generateTrainingPhrases] Total progress [%d/%d] %s>", nIndex, listIntent.size(), Common.progressBar(nIndex, listIntent.size()));
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
				bRet = trainPhraseGen.generateTrainingPhrase(botIntent, mutOpSet);
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
			tcGen.generateTestCases(currentBot, strPath);
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
	public VariationsCollectionText getGeneratedTrainingPhrases() {
		boolean bRet;
		VariationsCollectionText mapRet;
		try
		{
			mapRet = trainPhraseGen.getVariationsCollectionTxt();
		}
		catch(Exception e)
		{
			mapRet = null;
		}

		return mapRet;
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
	public boolean measureMetrics(MetricOperatorsSet metricOps) {
		boolean bRet;
		
		bRet =  false;
		if(botMetrics != null)
			bRet =  botMetrics.doAnalyse(currentBot, metricOps);
		
		return bRet;
	}
	public String getMetricReport() {
		String strRet;
		
		strRet="";
		if(botMetrics != null)
			strRet =  botMetrics.getMetricsReport();
		
		return strRet;
	}
}
