package auxiliar;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import generator.Bot;
import generator.GeneratorPackage;
import transformation.dialogflow.ReadAgent;
import transformation.dialogflow.agent.Agent;
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
		String strExtension;
		boolean bRet;
		
		file = new File(strPath);
		bRet = false;
		
		if (file.exists()) {
			strExtension = Common.getExtension(strPath);
			if(isXmiFile(strExtension))
			{
				bRet = loadFromXmi(file);
			}
			else if(isZipFile(strExtension))
			{
				bRet = loadFromZip(file);
			}
		}
		else
			System.out.println("[loadChatbot] - The file does not exists!!");		
		
		return bRet;
	}

	private boolean loadFromZip(File file) {
		
		ReadAgent agentReader;
		Agent agent;
		boolean bRet;
		
		botResourceSet = getResourceSet();
		//La duda aqui, es como gestionar despues el botResource. 
		//En este punto del desarrollo no se podria salvar a disco sin el.
		//Ver como solventar esto
		//botResource ??
				
		/*
		 * resource.getContents().clear();
		   resource.getContents().add(bot);
		   resource.save(null);
		 */
		
		//Initialise
		agentReader = new ReadAgent();
		agent = null;
		bRet = false;
		
		try {
			agent = agentReader.getAgent(file);
			currentBot = agent.getBot();
			bRet = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return bRet;
	}

	private boolean isZipFile(String strExtension) {
			boolean bRet;
			
			bRet = false;
			if(strExtension != null && !strExtension.isBlank())
			{
				bRet = (strExtension.equals("zip"));
			}
			
			return bRet;
	}

	private boolean loadFromXmi(File file) {
		boolean bRet;
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
			bRet = false;
		}
		return bRet;
	}
	
	private boolean isXmiFile(String strExtension) {

		boolean bRet;
		
		bRet = false;
		if(strExtension != null && !strExtension.isBlank())
		{
			bRet = (strExtension.equals("xmi"));
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

	public Bot getCurrentBot() {
		return currentBot;
	}

}
