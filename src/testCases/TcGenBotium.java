package testCases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import analyser.flowTree.TreeBranch;
import analyser.flowTree.TreeBranchList;
import analyser.flowTree.TreeInterAction;
import auxiliar.Common;
import generator.Action;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.Text;
import generator.UserInteraction;
import transformation.dialogflow.ConversorBotium;

public class TcGenBotium implements ITestCaseGenerator {

	BotAnalyser botAnalyser;
	
	String strPath;
	private final String USER_UTTER_TAG = "user";
	private final String BOT_UTTER_TAG = "actions";
	
	public boolean generateTestCases(Bot botIn, String strPath) {
		
		int nIndex;
		boolean bRet;
		BotAnalyser botAnalyser;
		
		//Initialise
		bRet = true;
		nIndex = 0;
		
		try
		{
			this.strPath = strPath; 
			botAnalyser = new BotAnalyser();
			
			//Check if the path exists, if not -> create it
			if(Common.checkDirectory(strPath))
			{
				//Export the flows
				depthExport(botIn);

				//Export the AsUnits (Asymob Unit Tests)
				/*for(UserInteraction flow: botIn.getFlows())
				{
					System.out.printf("Handling flow %d\n", nIndex);
					handleFlow(nIndex, flow);
					nIndex++;
				}*/
			}
			else
			{
				bRet = false;
				System.out.println("[generateTestCases] - A problem with the directory has ocurred: "+strPath);
			}
			
			
		}
		catch(Exception e)
		{
			System.out.println("[generateTestCases] - Exception while generating TCs: "+e.getMessage());
		}

		
		return bRet;
	}

	private boolean depthExport(Bot botIn) {

		List<Pair<UserInteraction, List<Action>>> flowActionsTemp;
		TreeBranch treeBranch;
		String strEntityName;
		Map<String, LinkedList<String>> entityMap;
		boolean bRet;
		
		//Initialise
		bRet = false;
		if(botIn != null)
		{
			botAnalyser = new BotAnalyser(new ConversorBotium());
			//Extract the parameter type, not the text referece
			botAnalyser.setCompactRefPhrasesMode(true);
			for(UserInteraction flow: botIn.getFlows())
			{
				//TODO: Aqui para empezar, hay que extraer un arbol al uso. Extraer N ramas.
				//La variable flowActionsTemp tiene que ser una lista.
				//Explore the flow, and extract multiple trees in form of a list of pairs <UserInteraction, List<Action>>
				flowActionsTemp = botAnalyser.plainActionTreeInBranches(flow);
				
				//Create the tree branch, and save into list
				treeBranch = new TreeBranch(flowActionsTemp);
				
				createFlowTestCase(treeBranch);
								
				/*
				System.out.printf("Handling flow %d\n", nIndex);
				handleFlow(nIndex, botAnalyser, flow);
				nIndex++;*/
			}
			//Extract the entities
			for(Entity ent: botIn.getEntities())
			{
				strEntityName = ent.getName();
				entityMap = botAnalyser.getEntityMap(ent);
				
				createEntityTestCase(strEntityName, entityMap);
			}
			
		}
		return bRet;
	}

	private void createEntityTestCase(String strEntityName, Map<String, LinkedList<String>> entityMap) {
		String literalName, strConvoBuffer;
		LinkedList<String> synList;
		int nElement;
		literalName = strConvoBuffer = "";
		nElement =0;
		try
		{
			strConvoBuffer = strConvoBuffer.concat("     |$"+strEntityName+"\n");
			//Loop the entityMap updating the buffer
		    for (Entry<String, LinkedList<String>> entry : entityMap.entrySet()) {
		    	literalName = entry.getKey();
		    	synList = entry.getValue();
		        System.out.println(literalName + "=" + synList.toString());
		        
		        for(String syn: synList)
		        {
		        	strConvoBuffer =  strConvoBuffer.concat(String.format("case%d|%s\n", nElement, syn));
		        	nElement++;
		        }
		    }
		    
		    //Create the file and save to disk
		    createScriptingMemory(strEntityName, strConvoBuffer);
		}
		catch(Exception e)
		{
			System.out.println("[TcGenBotium::createEntityTestCase] Exception while creating a entity script memory");
		}
	}

	private void createScriptingMemory(String strEntityName, String strConvoBuffer) {
		File convoFile;
		BufferedWriter writer;

		try {
			convoFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.scriptingmemory.txt",strEntityName));
			writer = new BufferedWriter(new FileWriter(convoFile));
			
			writer.write(strConvoBuffer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createFlowTestCase(TreeBranch treeBranch) {

		String strIntentName, strConvoName, strConvoBuffer;
		TreeInterAction treeIntentAction;
		Intent intent;
		
		try
		{
			//Initialise
			strConvoName = strConvoBuffer = "";
			treeBranch.resetIndex();
			
			//This represents a single convo, whose name is composed by appending the names of the intents
			//We will only know the name of the convo, once the whole branch has been completely explored. 
			
			//TODO: La idea es, dividir los intents y las respuestas. Podemos hacer un first approach conteniendo todas
			//las combinaciones aqui, para no sobrecargar de convos.
			
			//Igualmente, todo se reduce a:
			//Intent: nombre + frases entrenamiento.
			//Actions: nombre + respuestas
			
			while(treeBranch.hasNext())
			{
				treeIntentAction = treeBranch.getNext();
				
				//TODO: Mover de aqui, mientras vamos probando
				//Aqui sacamos el nombre de los intents y sus acciones
				botAnalyser.splitByParameterConvenion(treeIntentAction);
								
				Aqui tendriamos una lista de pares de la conversacion
				La idea seria pre-procesarlo, para obtener ramas completas
				Es decir, unalista de conversationgroups, e ir añadiendolos
				
				//Update convoName
				strConvoName = updateConvoName(strConvoName, treeIntentAction);
				
				//Update convoBuffer: includes the #me and #bot calls
				strConvoBuffer = updateConvoBuffer(treeIntentAction, strConvoBuffer); 
						
				//Single intent
				exportIntentFile(treeIntentAction.getIntent());
				
				//Multiple Actions & prompts
				exportActionsFile(treeIntentAction.getIntent(), treeIntentAction.getActions());
				
			}
			exportConvoFile(strConvoName, strConvoBuffer);
		}
		catch(Exception e)
		{
			System.out.println("[TcGenBotium::createFlowTestCase] Exception while creating a flow");
		}
		
	}

	private void exportConvoFile(String strConvoName, String strConvoBuffer) {
		File convoFile;
		BufferedWriter writer;

		try {
			convoFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.convo.txt",strConvoName));
			writer = new BufferedWriter(new FileWriter(convoFile));
			
			writer.write("TC-"+strConvoName+"-convo\n\n");
			writer.write(strConvoBuffer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void exportActionsFile(Intent intent, List<Action> actions) {		
		String strActionFileName;
		LinkedList<String> retList, auxList;
		
		for(Action actIndex: actions)
		{
			strActionFileName = constructActionFileName(actIndex);
			
			retList = (LinkedList<String>) botAnalyser.extractAllActionPhrasesByRef(actIndex);

			auxList = botAnalyser.extractAllIntentParameterPrompts(intent);
			
			if(auxList!=null)
			{
				if(retList == null)
					retList = auxList;
				else
					retList.addAll(auxList);
			}
			
			if(retList != null)
			{
				//Bot utterance fil
				createUtteranceFile(strActionFileName, retList);
			}
		}
		
	}

	private void exportIntentFile(Intent intent) {
		String strIntentFileName;
		LinkedList<String> userPhrases;
		
		//Construct intent name
		strIntentFileName = constructIntentFileName(intent);
		
		//Intent phrase
		botAnalyser.setParameterMode(true);
		userPhrases = botAnalyser.extractAllIntentPhrases(intent);
		
		//User utterance file
		createUtteranceFile(strIntentFileName, userPhrases);	
		
	}

	private String updateConvoBuffer(TreeInterAction treeIntentAction, String strConvoBuffer) {
		String strRet, strIntentName, strActionName;
		List<Action> actionList;
		strRet = strConvoBuffer;
		
		//#me+intentName
		strIntentName = constructIntentFileName(treeIntentAction.getIntent());
		strIntentName = strIntentName.toUpperCase();
		strRet = strRet.concat("#me\n"+strIntentName+"\n");
		
		//#bot+actions
		strRet = strRet.concat("\n#bot\n");
		actionList = treeIntentAction.getActions();
		for(Action actionIndex: actionList)
		{
			//TODO: En botium no se como funcionan los HTTPRequest y Response.
			if(actionIndex instanceof Text)
			{
				strActionName =  constructActionFileName(actionIndex);
				strActionName = strActionName.toUpperCase();
				strRet = strRet.concat(strActionName+"\n");
			}
		}
		strRet = strRet.concat("\n");
		
		return strRet;
	}
	private String updateConvoName(String strConvoName, TreeInterAction treeIntentAction) {
		String strIntentName;
		
		strIntentName = treeIntentAction.getIntentName();
		strIntentName = strIntentName.replace(" ", "_");
		if(!strConvoName.isBlank())
			strConvoName = strConvoName+"->"+ strIntentName;
		else
			strConvoName = strIntentName;
		
		return strConvoName;
	}

	private boolean  createUtteranceFile(String strIntentNameFile, LinkedList<String> retList){
		File utteranceUserFile;
		BufferedWriter writerUserUtter;
		boolean bRet;
		
		bRet = true;
		try {
			
			//Check if the file exists and has content
			
			//Configure File
			utteranceUserFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.utterances.txt",strIntentNameFile));
			writerUserUtter = new BufferedWriter(new FileWriter(utteranceUserFile));
			writerUserUtter.write(strIntentNameFile.toUpperCase()+"\n");
			
			//Save files
			for(String phrase: retList)
			{
				System.out.printf("%s\n", phrase);
				writerUserUtter.write(phrase+"\n");
			}
			
			//Flush and close
			writerUserUtter.flush();
			writerUserUtter.close();
		} catch (IOException e) {
			e.printStackTrace();
			bRet = false;
		}
		return bRet;
	}
	private String constructIntentFileName(Intent intent)
	{
		String strIntentName, strIntentNameFile;
		
		strIntentName = intent.getName();
		strIntentName = strIntentName.replace(" ", "_");
		strIntentNameFile = strIntentName+"_"+USER_UTTER_TAG;
		
		return strIntentNameFile;
	}
	private String constructActionFileName(Action action) {
		String strActionName, strActionNameFile;
		
		strActionName = action.getName();
		strActionName = strActionName.replace(" ", "_");
		strActionName = strActionName+"_"+BOT_UTTER_TAG;
		
		return strActionName;
	}
	
	/*
	private void handleFlow(int nIndex, UserInteraction flow) throws IOException {
		String strIntentName, strActionName, strIntentNameFile, strActionFileName;
		Intent intent;
		LinkedList<String> retList;
		EList<Action> actionList;
		File intentFile; 
		BufferedWriter writer;
		
		//TODO:: Creo que no hace falta un arbol, se puede ir generando de manera secuencial. 
		//	· desenrollamos el arbol
		//	· partimos este metodo en dos, y bien ordenadito
		//	· dejamos los TCs bien definidos y se comprueba si el archivo existe o no antes de generarlo de nuevo..
			
		writer = null;
		intentFile = null;
		try
		{
			if(flow == null)
				throw new Exception ("Null flow");
			
			intent = flow.getIntent();
			strIntentName = flow.getIntent().getName();
			strIntentName = strIntentName.replace(" ", "_");
			strIntentNameFile = strIntentName+"_"+USER_UTTER_TAG;
			intentFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.convo.txt",strIntentName));			
			writer = new BufferedWriter(new FileWriter(intentFile));
			
			
			writer.write("TC-"+strIntentName+"-convo\n");
			writer.write("\n");
			writer.write("#me\n");
			
			System.out.printf("===========>%s.convo.txt\n", strIntentName);
			System.out.println("#me");
			System.out.printf("%s_input\n", strIntentName);
			
			//TODO: If nested flows are allowed, it is necessary to return a map/list of pairs <intent, list<actions>>. [check outcomming]
			retList = botAnalyser.extractAllIntentPhrases(intent);
			
			if(retList == null)
			{
				writer.close();
				throw new Exception("Null phrases extracted from: "+intent.getName());
			}
			
			writer.write(strIntentNameFile.toUpperCase()+"\n");
			
			//User utterance file
			createUtteranceFile(strIntentNameFile, retList);									
	
			System.out.println("");
			writer.write("\n");
			
			//Bot
			System.out.println("#bot");
			System.out.printf("%s_output", strIntentName);
			System.out.println("");		
			
			writer.write("#bot\n");

			//Here we extract a plained tree: <Intent, List<Actions>>
			actionList = botAnalyser.extractActionList(flow);
			
			if(actionList == null)
			{
				writer.close();
				throw new Exception("Null phrases extracted from: "+intent.getName());
			}			
			
			if(actionList!=null && actionList.size()>0)
			{
				
				strActionFileName = strIntentName+"_actions";
				writer.write(strActionFileName.toUpperCase()+"\n");
				
				//TODO: Temporal, lo sacamos todo junto
				for(Action actIndex: actionList)
				{
					strActionName = actIndex.getName();
					strActionName = strActionName.replace(" ", "_");					
					
					//TODO: Este intent tiene que salir del arbol aplanado
					retList = botAnalyser.extractAllActionPhrases(actIndex);

					if(retList != null)
					{
						//Bot utterance fil
						createUtteranceFile(strActionFileName, retList);
					}
				}					
			}
	
			writer.write("\n");
			System.out.printf("INTENT %s\n", strIntentName);
			//writer.write("INTENT "+strIntentName+"\n");
			System.out.printf("===========>EndFlow_%d\n", nIndex);
			
			writer.flush();
			writer.close();

		}
		catch(Exception e)
		{
			System.out.println("[handleFlow] Exception catched: "+e.getMessage());
		}
		finally
		{
			if(writer != null)
			{
				writer.close();
			}
		}
	}*/
}
