package testCases.botium;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import auxiliar.Common;
import testCases.botium.testcase.BotiumTestCase;
import testCases.botium.testcase.BotiumTestCaseFragment;

public class TcExportBotium {

	String strPath;
	private final String USER_UTTER_TAG = "user";
	private final String BOT_UTTER_TAG = "actions";
	private final String SCRIPT_TAG = "scriptingMemory";
	
	public TcExportBotium(String strPath) {
		this.strPath = strPath;
	}
	public boolean checkDirectory() {
		return Common.checkDirectory(strPath);
	}

	void createScriptingMemoryFile(String strEntityName, String strConvoBuffer) {
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
	
	private void exportTestCase(BotiumTestCase bTestCase, int nFragmentId) {
		String strConvoName, strConvoBuffer;
		LinkedList<BotiumTestCaseFragment> tcList;

		tcList = bTestCase.getFragments();

		strConvoName = strConvoBuffer = "";
		//Salvamos a disco la lista de fragmentos de conversacion
		for(BotiumTestCaseFragment botTc: tcList)
		{
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
	/*private void exportActionsFile(Intent intent, Action action) {		

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
	}*/
	private void exportActionsFile(String strActionName, LinkedList<String> botResponses) {		
		String strActionFileName;

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
			strConvoName = strConvoName+"-!"+ strIntentName;
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
	public void exportTestCases(LinkedList<BotiumTestCase> tcList) {
		//Para cada testcase completo
		int nFragmentId;

		if(tcList != null)
		{
			nFragmentId=0;
			for(BotiumTestCase bTc: tcList)
			{				
				exportTestCase(bTc, nFragmentId);
				nFragmentId++;
			}
		}

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
