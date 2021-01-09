package core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import aux.BotPrinter;
import generator.Bot;
import generator.GeneratorPackage;
import generator.Intent;
import main.MutationOperatorSet;

public class Asymob {

	private Resource botResource = null;
	private ResourceSet botResourceSet = null;	
	private Bot currentBot = null;
	
	private TrainPhraseGenerator trainPhraseGen = null;
	/*Crear una api, dando una funcionalidad comun:
		
	- LoadChatbot?
	- GetAllIntents(LinkedList<String>)
	- GenerateTrainingSet (Esto es con los operadores que generan 'equivalentes')
	- GenerateTestSuite (Fijarnos en el formato de charm)
	- Validate*/
	
	public Asymob()
	{
		trainPhraseGen = new TrainPhraseGenerator();
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

	public boolean generateTrainingPhraseByIntentId(String string, MutationOperatorSet mutOpSet) {
		Intent botIntent;
		boolean bRet;

		bRet = false;
		botIntent = currentBot.getIntent("Hours");
		
		if(botIntent != null)
			bRet = trainPhraseGen.generateTrainingPhraseFull(botIntent, mutOpSet);
		
		return bRet;
		
	}	
}
