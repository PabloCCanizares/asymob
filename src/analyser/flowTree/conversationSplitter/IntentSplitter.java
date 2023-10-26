package analyser.flowTree.conversationSplitter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import analyser.IntentAnalyser;
import generator.IntentInput;
import generator.Language;
import generator.Parameter;
import generator.TrainingPhrase;

/**
 * This class splits an Intent into different groups by considering the required parameters of the training phrases
 * @author Pablo C. Ca&ntildeizares
 *
 */
//TODO: Change name to IntentParamAggregator

public class IntentSplitter {
	private int LOG = 0;
	float fParamCoverage;
	String strIntentName;
	
	IntentAnalyser intentAnalyser;
	LinkedList<Boolean> coveredParameters;
	LinkedList<Parameter> intentParameters;
	HashMap<String, LinkedList<TrainingPhrase>> mapIntentGroups;
	
	public IntentSplitter(String strIntentName, LinkedList<Parameter> reqParameters)
	{
		this.strIntentName = strIntentName;
		this.intentParameters = reqParameters;
		intentAnalyser = new IntentAnalyser();
		mapIntentGroups = new HashMap<String, LinkedList<TrainingPhrase>> ();
		
		fParamCoverage = 0;
		coveredParameters = new LinkedList<Boolean>();
		
		for(Parameter param: reqParameters)
			coveredParameters.add(false);
	}
	/**
	 * Places the input intent in its corresponding group. This group will be determined by its code
	 * @param intentInput: Input intent
	 */
	public void matchParameters(IntentInput intentInput) {
		LinkedList<Parameter> localParamList;
		String strCode;
		
		
		if(intentInput instanceof TrainingPhrase)
		{
			//Extract a list of required parameters
			localParamList = intentAnalyser.getParametersFromIntentInput(intentInput);
			
			//Generate the code, i.e: "01" that represent that the parameter 0 is not present but parameter 1 is present.
			strCode = generateHashCode(localParamList);
			
			//Update the coverage with the new input phrases
			fParamCoverage = updateParamCoverage(strCode);
			
			//Manage group: given a code, insert the intentInput into its corresponding group
			manageEntry(strCode, (TrainingPhrase) intentInput);
		}

	}

	/**
	 * Given a code, insert the input Training phrase into a group (or creates a new one if it not exists)
	 * @param strCode: Code corresponding with the group
	 * @param trainingIn: Input training phrase
	 */
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
			if(LOG>0)
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
		
		if(this.intentParameters != null)
		{
			//Iterate the 
			for(Parameter param: this.intentParameters)
			{
				if(checkParamInList(paramListIn, param))
					strRet = strRet.concat("1");
				else
					strRet = strRet.concat("0");
			}
		}
		
		return strRet;
	}
	/**
	 * Check if a parameter is present in the list
	 * @param paramListIn: List of parameters where the search is performed
	 * @param param: Parameter to be searched
	 * @return True if the parameter is present in the list
	 */
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

	private float updateParamCoverage(String strCode) {
		float fRet, fTotal, fCovered;
		
		if (this.fParamCoverage < 1)
		{
			fCovered = 0;
			fTotal = strCode.length();
			
			
			for (int i = 0; i < strCode.length(); i++) {
			    if (strCode.charAt(i) == '1') 
			    {
			    	fCovered++;
			    	coveredParameters.set(i, true);
			    }
			}
			if(LOG>0)
				System.out.println("Partial: "+ fCovered / fTotal+ " | Acc: "+countCoverage(coveredParameters)/fTotal);
			
			fCovered = countCoverage(coveredParameters);
			fRet = fCovered / fTotal;
			
			if(fRet < fParamCoverage)
				fRet = fParamCoverage;
		}
		else
			fRet = fParamCoverage;
		
		return fRet;
	}
	private float countCoverage(LinkedList<Boolean> coveredParameters) {
		
		int nElements;
		
		nElements = 0;
		for(Boolean elem: coveredParameters)
			if(elem)
				nElements++;
		
		return nElements;
	}
	public int getNumParameters()
	{
		return intentParameters != null ? intentParameters.size() : 0;
	}
	public float getMaxParamCoverage()
	{
		return fParamCoverage;
	}
}
