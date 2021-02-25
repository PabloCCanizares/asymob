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
				//depthExport(botIn);
				for(UserInteraction flow: botIn.getFlows())
				{
					System.out.printf("Handling flow %d\n", nIndex);
					handleFlow(nIndex, botAnalyser, flow);
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

	private void depthExport(Bot botIn) {

		List<Pair<UserInteraction, Action>> flowActions;
		if(botIn != null)
		{
			botAnalyser = new BotAnalyser();
			
			for(UserInteraction flow: botIn.getFlows())
			{
				flowActions = botAnalyser.plainActionTree(flow);
				/*
				System.out.printf("Handling flow %d\n", nIndex);
				handleFlow(nIndex, botAnalyser, flow);
				nIndex++;*/
			}
		}
	}

	private void handleFlow(int nIndex, BotAnalyser botAnalyser, UserInteraction flow) throws IOException {
		String strIntentName, strActionName, strIntentNameFile, strActionFileName;
		Intent intent;
		LinkedList<String> retList;
		EList<Action> actionList;
		File intentFile, utterancesActionFile, utteranceUserFile; 
		BufferedWriter writer, writerUtter;
		
		//TODO:: Creo que no hace falta un arbol, se puede ir generando de manera secuencial. 
		//	· desenrollamos el arbol
		//	· partimos este metodo en dos, y bien ordenadito
		//	· dejamos los TCs bien definidos y se comprueba si el archivo existe o no antes de generarlo de nuevo..
			
		writer = writerUtter = null;
		utterancesActionFile = intentFile = utteranceUserFile = null;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			bRet = false;
		}
		return bRet;
	}

}
