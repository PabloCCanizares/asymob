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
			strConvoBuffer = updateConvoBuffer(botTc, strConvoBuffer); 

			if(!botTc.isInline())
			{
				//Update convoName
				strConvoName = updateConvoName(strConvoName, botTc.getIntentName());

				//Single intent
				exportIntentFile(botTc.getIntentName(), botTc.getTrainingPhrases());

				//Multiple Actions & prompts
				exportActionsFile(botTc.getActionName(), botTc.getActionPhrases());
			}
			else if (botTc.isStated())
			{
				strConvoName = updateConvoName(strConvoName, botTc.getIntentName());
			}

		}

		exportConvoFile(strConvoName,nFragmentId, strConvoBuffer);
	}

	private String updateConvoBuffer(BotiumTestCaseFragment botTc, String strConvoBuffer) {
		boolean bInline, bIsReverse, bStated;
		String strIntentName, strActionName;
		
		strIntentName = botTc.getIntentName();
		strActionName = botTc.getActionName();
		bInline = botTc.isInline();
		bIsReverse = botTc.isReverse();
		bInline = botTc.isInline();
		bStated = botTc.isStated();
		
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

			if( strActionName != null && !strActionName.isBlank())
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
				{
					
					if(bStated)
						strRet = strRet.concat("#me\n"+botTc.getSingleTrainingPhrase()+"\n");
					else
						strRet = strRet.concat("#me\n"+strIntentName+"\n");
						
				}
				if(strActionName != null && !strActionName.isBlank())
				{
					if(bStated)
						strRet = strRet.concat("\n#bot\n"+botTc.getSingleOutputPhrase()+"\n");
					else
						strRet = strRet.concat("\n#bot\n"+strActionName+"\n");
				}
			}
			else
			{
				if(strActionName != null && !strActionName.isEmpty())
					strRet = strRet.concat("#bot\n"+strActionName+"\n");
				if(strIntentName != null && !strIntentName.isBlank())
					strRet = strRet.concat("\n#me\n"+strIntentName+"\n");
			}
		}

		strRet = strRet.concat("\n");

		return strRet;
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
				if(strActionName != null && !strActionName.isEmpty())
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
}
