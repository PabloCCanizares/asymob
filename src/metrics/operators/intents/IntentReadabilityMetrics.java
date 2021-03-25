package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;

import analyser.IntentAnalyser;
import auxiliar.Common;
import auxiliar.JavaRunCommand;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentReadabilityMetrics extends IntentMetricBase{
	
	private final String textStatPath= "/scripts/textstat_adapter.py";
	private final String command= "/scripts/textstat_adapter.py";
	public IntentReadabilityMetrics() {
		super(EMetricOperator.eReadabilityMetrics);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		LinkedList<String> phrasesList, listRet = null;
		String strCompleteText, strMetrics;
		JavaRunCommand commandRunner;
		
		//Initialise
		inAnalyser = new IntentAnalyser();
		commandRunner = new JavaRunCommand();
		strCompleteText="";
		//Extract the phrases
		
		phrasesList = inAnalyser.extractStringPhrasesFromIntent(this.intentIn);
		if(phrasesList != null)
		{
			for(String strPhrase: phrasesList)
			{
				strCompleteText+=" "+strPhrase;
			}
			//commandRunner.setProgram(strProgramId);
			if(!strCompleteText.isBlank())
			{
				commandRunner.setInputPhrase(strCompleteText);
				commandRunner.setProgramPath(System.getProperty("user.dir")+textStatPath);
				commandRunner.resetLastResults();
				if (commandRunner.runCommand(""))
					listRet = commandRunner.getLastResults();			
			
				strMetrics = Common.listToString(listRet);
				metricRet = new MetricValue(this);
				metricRet.setValue(strMetrics);
			}
		}
	}
}
