package testCases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import analyser.flowTree.TreeBranch;
import analyser.flowTree.TreeBranchList;
import analyser.flowTree.TreeInterAction;
import aux.Common;
import generator.Action;
import generator.Bot;
import generator.Intent;
import generator.UserInteraction;

public class TcGenBotium implements ITestCaseGenerator {

	String strPath;
	private final String USER_UTTER_TAG = "user";
	BotAnalyser botAnalyser;
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
				for(UserInteraction flow: botIn.getFlows())
				{
					System.out.printf("Handling flow %d\n", nIndex);
					handleFlow(nIndex, flow);
					nIndex++;
				}
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
		boolean bRet;
		
		//Initialise
		bRet = false;
		if(botIn != null)
		{
			botAnalyser = new BotAnalyser();
			
			for(UserInteraction flow: botIn.getFlows())
			{
				//Explore the flow, and extract a tree in form of a list of pairs <UserInteraction, List<Action>>
				flowActionsTemp = botAnalyser.plainActionTreeInBranches(flow);
				
				//Create the tree branch, and save into list
				treeBranch = new TreeBranch(flowActionsTemp);
				
				createFlowTestCase(treeBranch);
				/*
				System.out.printf("Handling flow %d\n", nIndex);
				handleFlow(nIndex, botAnalyser, flow);
				nIndex++;*/
			}
		}
		return bRet;
	}

	private void createFlowTestCase(TreeBranch treeBranch) {

		String strIntentName, strConvoName, strConvoBuffer;
		TreeInterAction treeIntentAction;
		Intent intent;
		
		try
		{
			//Initialise
			strConvoName ="";
			treeBranch.resetIndex();
			
			//This represents a single convo, whose name is composed by appending the names of the intents
			//We will only know the name of the convo, once the whole branch has been completely explored. 
			while(treeBranch.hasNext())
			{
				treeIntentAction = treeBranch.getNext();
				
				//Update convoName
				strConvoName = updateConvoName(strConvoName, treeIntentAction);
				
				//Update convoBuffer
				strConvoBuffer = updateConvoBuffer(treeIntentAction); 
						
				//Single intent
				exportIntentFile(treeIntentAction.getIntent());
				
				//Multiple Actions
				exportActionsFile(treeIntentAction.getActions());
				
			}
			//exportConvoFile
		}
		catch(Exception e)
		{
			
		}
		
	}

	private String updateConvoName(String strConvoName, TreeInterAction treeIntentAction) {
		String strIntentName;
		
		if(treeIntentAction != nu)
		strIntentName = treeIntentAction.getIntentName();
		if(!strConvoName.isBlank())
			strConvoName = strConvoName+" - "+ treeIntentAction.getIntentName();
		else
			strConvoName = treeIntentAction.getIntentName();
		return strConvoName;
	}

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
					retList = botAnalyser.extractAllActionPhrases(actIndex, flow.getIntent());

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
	}

	/*private BufferedWriter createBotUtteranceFile(String strActionFileName, LinkedList<String> retList)
			throws IOException {
		File utterancesActionFile;
		BufferedWriter writerUtter;
		utterancesActionFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.utterances.txt",strActionFileName));
		writerUtter = new BufferedWriter(new FileWriter(utterancesActionFile));
		
		writerUtter.write(strActionFileName.toUpperCase()+"\n");
		
		for(String phrase: retList)
		{
			if(phrase != null && !phrase.isBlank())
			{
				System.out.printf("%s\n", phrase);
				writerUtter.write(phrase+"\n");
			}
		}
		
		writerUtter.flush();
		writerUtter.close();
		return writerUtter;
	}*/

	private boolean  createUtteranceFile(String strIntentNameFile, LinkedList<String> retList){
		File utteranceUserFile;
		BufferedWriter writerUserUtter;
		boolean bRet;
		
		bRet = true;
		try {
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

}
