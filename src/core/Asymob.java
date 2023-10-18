package core;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import analyser.BotAnalyser;
import auxiliar.BotPrinter;
import auxiliar.BotResourcesManager;
import auxiliar.Common;
import generator.Bot;
import generator.Intent;
import metrics.IMetricAnalyser;
import metrics.MetricAnalyserManager;
import metrics.MetricOperatorsSet;
import metrics.db.MetricDataBase;
import metrics.reports.MetricReport;
import metrics.reports.MetricReportGenerator;
import testCases.BasicTestCaseGenerator;
import testCases.ExhaustiveTestCaseGenerator;
import testCases.ITestCaseGenerator;
import testCases.ITestSelectionStrategy;
import testCases.MediumTestCaseGenerator;
import testCases.botium.TcGenBotium;
import training.IVariantPhraseGenerator;
import training.VariationsCollectionText;
import training.simple.SimplePhraseGenerator;
import transformation.ITransformation;
import validation.BotValidation_General;
import validation.BotValidatorManager;
import variants.operators.base.MutationOperatorSet;

public class Asymob {

	private Bot currentBot = null;
	private BotValidatorManager botValidator = null;
	private IVariantPhraseGenerator trainPhraseGen = null;
	private BotAnalyser botAnalyser = null;
	private ITransformation botTransformation = null;
	private IMetricAnalyser botMetrics = null;
	private BotResourcesManager botResourceManager = null;
	
	public Asymob()
	{
		trainPhraseGen = new SimplePhraseGenerator();
		botValidator = new BotValidation_General();
		botMetrics = new MetricAnalyserManager();
		botResourceManager = new BotResourcesManager();
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
		boolean bRet;
		
		bRet = false;
		
		if(botResourceManager != null)
			bRet = botResourceManager.loadChatbot(strPath);
		
		if(bRet)
		{
			currentBot = botResourceManager.getCurrentBot();
		}
		/* PAblo: Paso la carga a botResourceManager, si teneis problemas al cargar, comentadme.
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
			System.out.println("[loadChatbot] - The file does not exists!!");	*/	
		
		return bRet;
	}
	
	public boolean saveToDisk(String strPath)
	{
		OutputStream output;
		Resource botResource;
		ResourceSet botResourceSet;
		boolean bRet;
		
		bRet = true;
		try
		{
			//TODO: Beware! Debug line, once the project has been finished, delete it (replace part)!
			output  = new FileOutputStream(strPath.replaceAll(".xmi", "_copy.xmi"));
			
			botResource = botResourceManager.getBotResource();
			botResourceSet = botResourceManager.getBotResourceSet();
			//Save a copy to disk
			if(botResource == null)
			{
				if(botResourceSet == null)
					botResourceSet = botResourceManager.getBotResourceSet();
				
				Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
				java.util.Map<String, Object> m = reg.getExtensionToFactoryMap();
				m.put("xmi", new XMIResourceFactoryImpl());
				botResource = botResourceSet.createResource(URI.createFileURI(strPath));
				botResource.getContents().add(currentBot);
				botResource.save(Collections.EMPTY_MAP);
			}
			else
			{
				botResource.save(System.out, null);
				botResource.save(output, null);			

			}
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

	/* Este metodo ha pasado a la clase BotResourcesManager
	 * private ResourceSet getResourceSet() {
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
	}*/
	
	public boolean validateBot()
	{
		return false;
	}
	
	public boolean generateTestCases(String strPath, String method)
	{
		boolean bRet;
		
		bRet = false;
		ITestSelectionStrategy selectionStrategy = null;
		if(currentBot != null)
		{
			switch(method) {
			case "Basic":
				selectionStrategy = new BasicTestCaseGenerator();
				break;
			case "Medium":
				selectionStrategy = new MediumTestCaseGenerator();
				break;
			default:
				selectionStrategy = new ExhaustiveTestCaseGenerator();
				break;
			}			
			ITestCaseGenerator tcGen = new TcGenBotium(selectionStrategy);
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
		BotPrinter.printBot(currentBot);
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
	public MetricReport getMetricReport(MetricReportGenerator metricReport) {
		MetricReport metricRep;
		
		metricRep=null;
		if(botMetrics != null)
			metricRep =  botMetrics.getMetricsReport(metricReport);
		
		return metricRep;
	}

	public MetricDataBase getData() {
		MetricDataBase ret=null;
		
		if(botMetrics != null)
			ret = ((MetricAnalyserManager) botMetrics).getData();
		
		return ret;
	}

}
