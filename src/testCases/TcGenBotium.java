package testCases;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import aux.Common;
import generator.Action;
import generator.Bot;
import generator.Intent;
import generator.UserInteraction;

public class TcGenBotium implements ITestCaseGenerator {

	String strPath;
	
	public boolean generateTestCases(String strPath, Bot botIn) {
		
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

	private void handleFlow(int nIndex, BotAnalyser botAnalyser, UserInteraction flow) throws IOException {
		String strIntentName, strActionName;
		Intent intent;
		LinkedList<String> retList;
		EList<Action> actionList;
		File intentFile, utterancesActionFile; 
		BufferedWriter writer, writerUtter;
		
		//TODO:: 
		//	· desenrollamos el arbol
		//	· partimos este metodo en dos, y bien ordenadito
		//	· dejamos los TCs bien definidos y se comprueba si el archivo existe o no antes de generarlo de nuevo..
			
		writer = writerUtter = null;
		try
		{
			if(flow == null)
				throw new Exception ("Null flow");
			
			intent = flow.getIntent();
			strIntentName = flow.getIntent().getName();
			strIntentName = strIntentName.replace(" ", "_");
			intentFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.combo.txt",strIntentName));
			
			writer = new BufferedWriter(new FileWriter(intentFile));
			
			
			System.out.printf("===========>%s.combo.txt\n", strIntentName);
			writer.write(strIntentName+"-combo\n");
			System.out.println("#me");
			writer.write("\n");
			writer.write("#me\n");
			System.out.printf("%s_input\n", strIntentName);
			
			//TODO: If nested flows are allowed, it is necessary to return a map/list of pairs <intent, list<actions>>. [check outcomming]
			retList = botAnalyser.extractAllIntentPhrases(intent);
			
			if(retList == null)
				throw new Exception("Null phrases extrated from: "+intent.getName());
			
			for(String phrase: retList)
			{
				System.out.printf("%s\n", phrase);
				writer.write(phrase+"\n");
			}									
	
			System.out.println("");
			writer.write("\n");
			
			
			//Bot
			System.out.println("#bot");
			writer.write("#bot\n");
			System.out.printf("%s_output", strIntentName);
			System.out.println("");		
			
			//Here we extract a plained tree: <Intent, List<Actions>>
			actionList = botAnalyser.extractActionList(flow);
			
			if(actionList!=null && actionList.size()>0)
			{
				for(Action actIndex: actionList)
				{
					strActionName = actIndex.getName();
					strActionName = strActionName.replace(" ", "_");					
					utterancesActionFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.utterances.txt",strActionName));
					writerUtter = new BufferedWriter(new FileWriter(utterancesActionFile));
					
					//TODO: Este intent tiene que salir del arbol aplanado
					retList = botAnalyser.extractAllActionPhrases(actIndex, flow.getIntent());
					writer.write(strActionName.toUpperCase()+"\n");
					writerUtter.write(strActionName.toUpperCase()+"\n");
					if(retList != null)
					{
						for(String phrase: retList)
						{
							System.out.printf("%s\n", phrase);
							writerUtter.write(phrase+"\n");
						}
					}
				}					
			}
	
			writer.write("\n");
			System.out.printf("INTENT %s\n", strIntentName);
			//writer.write("INTENT "+strIntentName+"\n");
			System.out.printf("===========>EndFlow_%d\n", nIndex);
			
			writer.flush();
			writer.close();
			writerUtter.flush();
			writerUtter.close();
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

}
