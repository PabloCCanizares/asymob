package testCases;

import java.util.LinkedList;

import org.eclipse.emf.common.util.EList;

import analyser.BotAnalyser;
import generator.Action;
import generator.Bot;
import generator.Intent;
import generator.UserInteraction;

public class TcGenBotium implements ITestCaseGenerator {


	public boolean generateTestCases(String strPath, Bot botIn) {
		
		int nIndex;
		String strIntentName;
		Intent intent;
		nIndex = 0;
		BotAnalyser botAnalyser;
		LinkedList<String> retList;
		EList<Action> actionList;
		
		botAnalyser = new BotAnalyser();
		//Lo colocamos a fuego y despues ya refactorizamos y protegemos
		for(UserInteraction flow: botIn.getFlows())
		{
			if(flow != null)
			{
				intent = flow.getIntent();
				strIntentName = flow.getIntent().getName();
				System.out.printf("===========>%s.combo.txt\n", strIntentName);
				System.out.println("#me");
				System.out.printf("%s_input\n", strIntentName);
				retList = botAnalyser.extractAllIntentPhrases(intent);
				
				if(retList != null)
				{
					for(String phrase: retList)
					{
						System.out.printf("%s\n", phrase);
					}									
				}

				System.out.println("");
				
				
				//Bot
				System.out.println("#bot");
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
							}
						}
					}					
				}


				System.out.printf("INTENT %s\n", strIntentName);
				System.out.printf("===========>EndFlow_%d\n", nIndex);
			}
			nIndex++;
		}
		
		return false;
	}

}
