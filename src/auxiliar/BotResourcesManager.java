package auxiliar;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import generator.Bot;
import generator.GeneratorPackage;
/**
 * Manages the resources of the chatbot
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class BotResourcesManager {

	private Resource botResource = null;
	private ResourceSet botResourceSet = null;	
	private Bot currentBot = null;
	
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
		currentBot = null;
		
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

	public Bot getCurrentBot() {
		return currentBot;
	}

}
