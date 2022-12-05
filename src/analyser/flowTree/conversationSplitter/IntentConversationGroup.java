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
		String strRet;
		
		if(paramGroupInfo != null && strIntentName != null)
		{
			strRet = strIntentName+"_"+paramGroupInfo.getCode();
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
}
