package testCases.botium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
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
import analyser.flowTree.conversationSplitter.ConversationGroup;
import auxiliar.Common;
import generator.Action;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.Language;
import generator.Parameter;
import generator.Text;
import generator.UserInteraction;
import testCases.ITestCaseGenerator;
import transformation.dialogflow.ConversorBotium;

public class TcGenBotium implements ITestCaseGenerator {

	BotAnalyser botAnalyser;
	
	String strPath;
	private final String USER_UTTER_TAG = "user";
	private final String BOT_UTTER_TAG = "actions";
	private final String SCRIPT_TAG = "scriptingMemory";
	
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

			}
			//Extract the entities
			for(Entity ent: botIn.getEntities())
			{
				strEntityName = ent.getName();
				entityMap = botAnalyser.getEntityMap(ent);
				
				createEntityTestCase(strEntityName, entityMap);
			}
			
			//In addition, search an specifi parameter, to create a new scripting file
			entityMap = extractStandardEntity(botIn, "date", "dateIn");
			createEntityTestCase("date", entityMap);
			
			entityMap = extractStandardEntity(botIn, "time", "timeIn");
			createEntityTestCase("date", entityMap);
			
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
			convoFile = Common.fileWithDirectoryAssurance(this.strPath+File.separator+SCRIPT_TAG, String.format("%s.scriptingmemory.txt",strEntityName));
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

		TreeInterAction treeIntentAction;
		BotiumTestCaseFragment botTestCaseFragment;
		BotiumTestCase botTestCase;
		LinkedList<BotiumTestCaseFragment> tcFragmentList;
		LinkedList<ConversationGroup> conversationList, auxConversation;
		LinkedList<BotiumTestCase> testcaseList, auxTcList;
		BotiumTestCase auxTc;
		try
		{
			//Initialise
			treeBranch.resetIndex();
					
			testcaseList = new LinkedList<BotiumTestCase>();
			conversationList = new LinkedList<ConversationGroup>();
			
			//We iterate the all the tree branches (conversation paths)
			while(treeBranch.hasNext())
			{
				treeIntentAction = treeBranch.getNext();
				
				//En este punto se divide el conjunto de frases de entrenamiento teniendo en cuenta sus parametros
				//Split the training phrases considering the parameters of each phrase and categorise them in groups
				//called conversations groups.
				conversationList = botAnalyser.splitConversationByParam(treeIntentAction);
			
				
				//TODO: Aqui deberiamos poder trazar estrategias. Esta divide los grupos en archivos.
				//Otra podría ser incluirlo todo en un archivo como estaba antes
				//Acto seguido, se recorren todos los grupos y
				auxTcList = new LinkedList<BotiumTestCase>();
				for(ConversationGroup conversation: conversationList)
				{
					//Check wheter the conversation group is complete
					
					//It may have a single or multiple fragments per group: If the phrase is not complete
					//lacks some of the parameters, we need to complement it.

					botTestCaseFragment = convertConvGroupToTcFragment(conversation);
					
					tcFragmentList = complementConversation(conversation);
					
					if(tcFragmentList == null)
						tcFragmentList = new LinkedList<BotiumTestCaseFragment>();
					
					tcFragmentList.addFirst(botTestCaseFragment);
					
					//Iterate the testcase list, and add this fragment to all of them
					if(!testcaseList.isEmpty())
					{
						//Para cada uno de los test cases que hay, hay que añadirles los fragmentos nuevos
						for(BotiumTestCase botTcIn: testcaseList)
						{
							//Para cada TC que tenga la lista original, 
							auxTc = botTcIn.dup();
							
							//Add all the existing fragments
							for(BotiumTestCaseFragment tcFragment: tcFragmentList)
							{
								auxTc.addFragment(tcFragment);
							}
							
							auxTcList.add(auxTc);
						}
					}
					else
					{
						//Create a new test case
						auxTc = new BotiumTestCase();
						//Add all the existing fragments
						for(BotiumTestCaseFragment tcFragment: tcFragmentList)
						{
							auxTc.addFragment(tcFragment);
						}
						auxTcList.add(auxTc);
					}
				}
				testcaseList = auxTcList;
			}
			
			//Para cada testcase completo
			int nFragmentId;
			
			nFragmentId=0;
			for(BotiumTestCase bTc: testcaseList)
			{
				exportTestCase(bTc, nFragmentId);
				nFragmentId++;
			}
		}
		catch(Exception e)
		{
			System.out.println("[TcGenBotium::createFlowTestCase] Exception while creating a flow");
		}
		
	}

	private LinkedList<BotiumTestCaseFragment> complementConversation(ConversationGroup conversation) {
		LinkedList<Parameter> paramList;
		LinkedList<BotiumTestCaseFragment> tcFragmentList;
		BotiumTestCaseFragment newFragment;
		String strPrompt;
		LinkedList<String> defaultResp, defaultPrompts, finishingResponse;
		
		tcFragmentList = null;
		//If it is not present. Obtain all the parameters 
		if(!conversation.allParametersPresent())
		{
			//disable the action
			conversation.disableActions();
			
			tcFragmentList = new LinkedList<BotiumTestCaseFragment>();
			//Si no estan presentes, hay que complementar el fragmento
			paramList = conversation.getNotPresentParams();
			
			//Iterate the non present parameters and create fragments
			for(Parameter param: paramList)
			{
				defaultPrompts = botAnalyser.extractParameterPrompt(param);
				
				defaultResp = conversation.getDefaultResponsesForParam(param);
				
				System.out.println("Param: "+param.getName()+" | bot: "+defaultPrompts.getFirst()+" | user: "+defaultResp.getFirst());
				
				//Create the fragment, and select the reverse mode
				newFragment = new BotiumTestCaseFragment(defaultResp.getFirst(), defaultPrompts.getFirst(), true);
				//Ya tenemos el par - prompt/respuesta
				tcFragmentList.add(newFragment);
			}
			
			//Finally, insert the complete response
			finishingResponse = conversation.getFinishingResponse();
			newFragment = new BotiumTestCaseFragment(null, finishingResponse.getFirst(), true);
			tcFragmentList.add(newFragment);
		}
		
		return tcFragmentList;
	}

	private BotiumTestCaseFragment convertConvGroupToTcFragment(ConversationGroup conversation) {
		String strActionGroupName;
		BotiumIntent botIntent;
		BotiumAction botAction;
		BotiumTestCaseFragment botTestCase;
		String strGroupName;
		LinkedList<String> trainingPhrases;
		LinkedList<String> responses;
		
		botAction = null;
		botIntent = null;
		
		if(!conversation.allParametersPresent())
			conversation.disableActions();

		//Intent
		if(!conversation.isDisabledIntent())
		{
			strGroupName = conversation.getIntentGroupName();
			System.out.println("IntentGroup: "+strGroupName);
			trainingPhrases = conversation.getPlainTrainingPhases();
			System.out.println("Training phrases:\n"+Common.listToStringWithBreak(trainingPhrases));
			botIntent = new BotiumIntent(strGroupName, trainingPhrases);
		}
		
		//Action
		if(!conversation.isDisabledAction())
		{
			strActionGroupName = conversation.getActionGroupName();
			responses = conversation.getActionResponses();
			System.out.println("Responses:\n"+Common.listToStringWithBreak(responses));
			botAction = new BotiumAction(strActionGroupName, responses);
		}

		
		botTestCase = new BotiumTestCaseFragment(botIntent, botAction, false);
		return botTestCase;
	}

	private void exportTestCase(BotiumTestCase bTestCase, int nFragmentId) {
		String strConvoName, strConvoBuffer;
		String strIntentName, strActionName;
		LinkedList<BotiumTestCaseFragment> tcList;

		tcList = bTestCase.getFragments();
		
		strConvoName = strConvoBuffer = "";
		//Salvamos a disco la lista de fragmentos de conversacion
		for(BotiumTestCaseFragment botTc: tcList)
		{
			strIntentName = strActionName = "";
			
			//Update convoBuffer: includes the #me and #bot calls
			strConvoBuffer = updateConvoBuffer(botTc.getIntentName(), botTc.getActionName(), botTc.isInline(), botTc.isReverse(), strConvoBuffer); 
				
			if(!botTc.isInline())
			{
				//Update convoName
				strConvoName = updateConvoName(strConvoName, botTc.getIntentName());
				
				//Single intent
				exportIntentFile(botTc.getIntentName(), botTc.getTrainingPhrases());
				
				//Multiple Actions & prompts
				exportActionsFile(botTc.getActionName(), botTc.getActionPhrases());
			}

		}
		
		exportConvoFile(strConvoName,nFragmentId, strConvoBuffer);
	}

	private void exportConvoFile(String strConvoName, int nId, String strConvoBuffer) {
		File convoFile;
		BufferedWriter writer;

		try {
			convoFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.convo.txt",strConvoName));
			writer = new BufferedWriter(new FileWriter(convoFile));
			
			if(nId >0)
				writer.write("TC-"+strConvoName+"-"+nId+"-convo\n\n");
			else
				writer.write("TC-"+strConvoName+"-convo\n\n");
			writer.write(strConvoBuffer);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//deprecated
	private void exportActionsFile(Intent intent, Action action) {		
		
		String strActionFileName;
		LinkedList<String> retList, auxList;
			
		retList = (LinkedList<String>) botAnalyser.extractAllActionPhrasesByRef(action);

		auxList = botAnalyser.extractAllIntentParameterPrompts(intent);
		
		if(auxList!=null)
		{
			if(retList == null)
				retList = auxList;
			else
				retList.addAll(auxList);
		}
		
		exportActionsFile(action.getName(), retList);
	}
	private void exportActionsFile(String strActionName, LinkedList<String> botResponses) {		
		String strActionFileName;
		LinkedList<String> retList, auxList;
		
		strActionFileName = constructActionFileName(strActionName);
		
		if(botResponses != null)
		{
			//Bot utterance fil
			exportUtterancesFile(strActionFileName, BOT_UTTER_TAG, botResponses);
		}
		
	}

	private void exportIntentFile(String strIntentName, LinkedList<String> userPhrases) {
		String strIntentFileName;
		
		//Construct intent name
		strIntentFileName = constructIntentFileName(strIntentName);
		
		//User utterance file
		exportUtterancesFile(strIntentFileName, USER_UTTER_TAG, userPhrases);	
		
	}

	private String updateConvoBuffer(String strIntentName, String strActionName, boolean bInline, boolean bIsReverse, String strConvoBuffer) {
		String strRet, strActionTagName, strIntentTagName;
		List<Action> actionList;
		strRet = strConvoBuffer;
		
		//#me+intentName
		if(!bInline)
		{
			if(strIntentName != null && !strIntentName.isBlank())
			{
				strIntentTagName = constructIntentFileName(strIntentName);
				strIntentTagName = strIntentTagName.toUpperCase();
				strRet = strRet.concat("#me\n"+strIntentTagName+"\n");
			}
			
			if(strActionName != null && !strActionName.isBlank())
			{
				strRet = strRet.concat("\n#bot\n");
				strActionTagName =  constructActionFileName(strActionName);
				strActionTagName = strActionTagName.toUpperCase();
				strRet = strRet.concat(strActionTagName+"\n");
			}
		}
		else
		{
			if(!bIsReverse)
			{
				if(strIntentName != null && !strIntentName.isBlank())
					strRet = strRet.concat("#me\n"+strIntentName+"\n");
				if(strActionName != null && !strActionName.isBlank())
					strRet = strRet.concat("\n#bot\n"+strActionName+"\n");
			}
			else
			{
				if(strActionName != null && !strActionName.isBlank())
					strRet = strRet.concat("#bot\n"+strActionName+"\n");
				if(strIntentName != null && !strIntentName.isBlank())
					strRet = strRet.concat("\n#me\n"+strIntentName+"\n");
			}
		}
	
		strRet = strRet.concat("\n");
		
		return strRet;
	}


	String updateConvoName(String strConvoName, String strIntentName) {

		strIntentName = strIntentName.replace(" ", "_");
		if(!strConvoName.isBlank())
			strConvoName = strConvoName+"->"+ strIntentName;
		else
			strConvoName = strIntentName;
		
		return strConvoName;
	}
	
	private boolean  exportUtterancesFile(String strIntentNameFile, String strFolder, LinkedList<String> retList){
		File utteranceUserFile;
		BufferedWriter writerUserUtter;
		boolean bRet;
		
		bRet = true;
		try {
			
			//Check if the file exists and has content
			
			//Configure File
			utteranceUserFile = Common.fileWithDirectoryAssurance(this.strPath+File.separator+strFolder, String.format("%s.utterances.txt",strIntentNameFile));
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
	private Map<String, LinkedList<String>> extractStandardEntity(Bot botIn, String strParamName, String newName) {
		
		Map<String, LinkedList<String>> mapRet;
		
		mapRet = new HashMap<String, LinkedList<String>>();
		//Extract all the sentences with a
		botAnalyser.extractAllBotParameterValues(botIn, strParamName);
		
		return mapRet;
	}
	private String constructIntentFileName(String strIntentName)
	{
		String strIntentNameFile;
		
		strIntentName = strIntentName.replace(" ", "_");
		strIntentNameFile = strIntentName+"_"+USER_UTTER_TAG;
		
		return strIntentNameFile;
	}

	private String constructActionFileName(String strActionName) {
		
		strActionName = strActionName.replace(" ", "_");
		strActionName = strActionName+"_"+BOT_UTTER_TAG;
		
		return strActionName;
	}
	//deprecated
	/*private String updateConvoName(String strConvoName, TreeInterAction treeIntentAction) {
		return updateConvoName(strConvoName, treeIntentAction.getIntentName());
	}*/
	//Deprecated
/*	private String constructActionFileName(Action action) {
		return constructActionFileName(action.getName());
	}
	*/
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

	//TODO: Deprecated
	/*private String updateConvoBuffer(TreeInterAction treeIntentAction, String strConvoBuffer) {
	
		return updateConvoBuffer(treeIntentAction.getIntentName(), strConvoBuffer);
	}*/
	//Deprecated
	/*private void exportIntentFile(Intent intent) {
		LinkedList<String> userPhrases;
		//Intent phrase
		botAnalyser.setParameterMode(true);
		userPhrases = botAnalyser.extractAllIntentPhrases(intent);
		
		exportIntentFile(intent.getName(), userPhrases);
	}
	*/
}
