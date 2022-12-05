package analyser.flowTree.conversationSplitter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import analyser.IntentAnalyser;
import generator.IntentInput;
import generator.Parameter;
import generator.TrainingPhrase;
import metrics.db.MetricDbSingleEntry;

public class IntentSplitter {
	String strIntentName;
	IntentAnalyser intentAnalyser;
	LinkedList<Parameter> intentParameters;
	HashMap<String, LinkedList<TrainingPhrase>> mapIntentGroups;
	
	public IntentSplitter(String strIntentName, LinkedList<Parameter> reqParameters)
	{
		this.strIntentName = strIntentName;
		this.intentParameters = reqParameters;
		intentAnalyser = new IntentAnalyser();
		mapIntentGroups = new HashMap<String, LinkedList<TrainingPhrase>> ();
	}
	public void matchParameters(IntentInput intentInput) {
		LinkedList<Parameter> localParamList;
		String strCode;
		
		
		if(intentInput instanceof TrainingPhrase)
		{
			localParamList = intentAnalyser.getParametersFromIntentInput(intentInput);
			
			//Generate the code, i.e: "01" that represent that the parameter 0 is not present but parameter 1 is present.
			strCode = generateHashCode(localParamList);
			
			//manage group
			manageEntry(strCode, (TrainingPhrase) intentInput);
		}

	}

	public void manageEntry(String strCode, TrainingPhrase trainingIn) {

		LinkedList<TrainingPhrase> traininphraseList;
		//System.out.println("manageEntry - Init");
		
		if(mapIntentGroups.containsKey(strCode))
		{
			//Extract the list, and re-insert
			traininphraseList = mapIntentGroups.get(strCode);
			traininphraseList.add(trainingIn);
			mapIntentGroups.put(strCode, traininphraseList);
		}
		else
		{
			System.out.println("manageEntry - Created new group with code: "+strCode);
			//create list and insert into the key
			traininphraseList = new LinkedList<TrainingPhrase>();
			traininphraseList.add(trainingIn);
			mapIntentGroups.put(strCode, traininphraseList);
		}
		//System.out.println("manageEntry - End");
		
	}
	
	public String generateHashCode(LinkedList<Parameter> paramListIn)
	{
		String strRet;
		
		strRet="";
		
		//Iterate the 
		for(Parameter param: this.intentParameters)
		{
			if(checkParamInList(paramListIn, param))
				strRet = strRet.concat("1");
			else
				strRet = strRet.concat("0");
		}
		
		return strRet;
	}
	private boolean checkParamInList(LinkedList<Parameter> paramListIn, Parameter param) {
		boolean bRet;
		int nIndex;
		Parameter localParam;
		
		try
		{
			nIndex = 0;
			bRet = false;
			while(!bRet && nIndex < paramListIn.size())
			{
				localParam = paramListIn.get(nIndex);
				
				if(localParam.getName().equals(param.getName()))
					bRet = true;
				
				nIndex++;
			}
		}
		catch(Exception e)
		{
			bRet = false;
		}
		
		return bRet;
	}
	//TODO: Se puede detectar que los prompts no estan probados.
	public LinkedList<IntentConversationGroup> processGroups()
	{
		String strHashCode;
		LinkedList<TrainingPhrase> phraseList;
		IntentConversationGroup groupInfo;
		LinkedList<IntentConversationGroup> retList;
		Iterator<Entry<String, LinkedList<TrainingPhrase>>> it;
		retList = new LinkedList<IntentConversationGroup>();
		it = this.mapIntentGroups.entrySet().iterator();
		
		while(it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			phraseList = (LinkedList<TrainingPhrase>) pair.getValue();
			strHashCode = (String) pair.getKey();
			groupInfo = new IntentConversationGroup(strIntentName, strHashCode, intentParameters, phraseList);
			retList.add(groupInfo);
		}
		
		return retList;
	}
}
