package analyser.flowTree.conversationSplitter;

import java.util.LinkedList;

import analyser.IntentAnalyser;
import generator.Parameter;
import generator.TrainingPhrase;
import transformation.dialogflow.ConversorBotium;

public class IntentConversationGroup {

	String strIntentName;
	ParameterGroup paramGroupInfo;
	LinkedList<TrainingPhrase> trainingPhrases;
	LinkedList<String> stringTrainingPhrases;
	
	public IntentConversationGroup(String strIntentName, String strCode, LinkedList<Parameter> existingParams, LinkedList<TrainingPhrase> phrases)
	{
		this.strIntentName = strIntentName;
		paramGroupInfo = new ParameterGroup(strCode, existingParams);
		this.trainingPhrases = phrases;
		plainTrainingPhrases();
	}

	public void plainTrainingPhrases()
	{
		IntentAnalyser intentAnalyer;
		
		intentAnalyer = new IntentAnalyser(new ConversorBotium());
		stringTrainingPhrases = intentAnalyer.convertTrainingPhrasesToString(trainingPhrases, true);
	}
	public String getIntentGroupName()
	{
		String strRet, strCode;
		
		if(paramGroupInfo != null && strIntentName != null)
		{
			strCode = paramGroupInfo.getCode();
			if(strCode != null && !strCode.isBlank())
				strRet = strIntentName+"_"+strCode;
			else
				strRet = strIntentName;
		}
		else
		{
			strRet = "";
		}
		return strRet;
	}
	
	public String ToString() {
		String strRet;
		
		strRet = "Intent: "+getIntentGroupName()+"\n\n";
		for(String strPhrase: stringTrainingPhrases)
		{
			strRet = strRet.concat("· ").concat(strPhrase).concat("\n");
		}
		
		return strRet;
	}

	public LinkedList<String> getPlainTrainingPhases()
	{
		return stringTrainingPhrases;
	}
	public boolean areAllParametersPresent() {
		return paramGroupInfo != null ? paramGroupInfo.checkAllParameters(true) : false;
	}

	public LinkedList<Parameter> getNotPresentParameters() {
		return paramGroupInfo != null ? paramGroupInfo.getParametersNotPresent() : null;
		
	}

	public void reduceTo(int nPhrases) {
		LinkedList<TrainingPhrase> tpAuxList;
		
		tpAuxList = new LinkedList<TrainingPhrase>();
		
		for(int i=0;i<nPhrases&&i<trainingPhrases.size();i++)
		{
			tpAuxList.add(trainingPhrases.get(i));
		}
		
		trainingPhrases.clear();
		
		for(TrainingPhrase tp: tpAuxList)
		{
			trainingPhrases.add(tp);
		}
		
		plainTrainingPhrases();
	}

	public int getNumPresentParameters() {
		return paramGroupInfo != null ? paramGroupInfo.getNumPresentParameters() : 0;
	}

	public String getSingleTP() {
		// TODO Auto-generated method stub
		return  trainingPhrases != null ? stringTrainingPhrases.getFirst(): "";		
	}
}
