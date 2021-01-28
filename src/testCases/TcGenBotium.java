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
		String strIntentName;
		Intent intent;
		LinkedList<String> retList;
		EList<Action> actionList;
		File intentFile;
		BufferedWriter writer;
		
		if(flow != null)
		{
			intent = flow.getIntent();
			strIntentName = flow.getIntent().getName();
			intentFile = Common.fileWithDirectoryAssurance(this.strPath, String.format("%s.combo.txt",strIntentName));
			
			writer = new BufferedWriter(new FileWriter(intentFile));
			
			System.out.printf("===========>%s.combo.txt\n", strIntentName);
			writer.write(strIntentName+"-combo\n");
			System.out.println("#me");
			writer.write("\n");
			writer.write("#me\n");
			System.out.printf("%s_input\n", strIntentName);
			
			retList = botAnalyser.extractAllIntentPhrases(intent);
			
			if(retList != null)
			{
				for(String phrase: retList)
				{
					System.out.printf("%s\n", phrase);
					writer.write(phrase+"\n");
				}									
			}

			System.out.println("");
			writer.write("\n");
			
			
			//Bot
			System.out.println("#bot");
			writer.write("#bot\n");
			System.out.printf("%s_output", strIntentName);
			System.out.println("");			
			actionList = botAnalyser.extractActionList(flow);
			
			if(actionList!=null && actionList.size()>0)
			{
				for(Action actIndex: actionList)
				{
					retList = botAnalyser.extractAllActionPhrases(actIndex);
					if(retList != null)
					{
						for(String phrase: retList)
						{
							System.out.printf("%s\n", phrase);
							writer.write(phrase+"\n");
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
		}
	}

}
