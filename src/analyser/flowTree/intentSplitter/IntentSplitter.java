package analyser.flowTree.intentSplitter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import analyser.IntentAnalyser;
import generator.IntentInput;
import generator.Parameter;
import generator.TrainingPhrase;

public class IntentSplitter {
	String strIntentName;
	IntentAnalyser intentAnalyser;
	LinkedList<Parameter> intentParameters;
	HashMap<String, LinkedList<TrainingPhrase>> mapIntentGroups;
	
	public IntentSplitter(LinkedList<Parameter> reqParameters)
	{
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
		System.out.println("manageEntry - Init");
		
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
		System.out.println("manageEntry - End");
		
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
	public void processGroups(String strIntentName)
	{
		//Esto devuelve una lista de pares: grupo: -> lista de frases de entrenamiento
	}
}
