package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;

import analyser.IntentAnalyser;
import auxiliar.Common;
import auxiliar.JavaRunCommand;
import edu.stanford.nlp.ling.TaggedWord;
import metrics.base.FloatMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import stanford.StandfordTagger;

public class IntentReadabilityMetrics extends IntentMetricBase{
	
	private final String textStatPath= "/scripts/textstat_adapter.py";
	private final String command= "/scripts/textstat_adapter.py";
	public IntentReadabilityMetrics() {
		super(EMetricOperator.eReadabilityMetrics);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases, nNouns;
		float fAverage;
		LinkedList<String> phrasesList, listRet = null;
		List<List<TaggedWord>>  taggedPhraseList;
		String strCompleteText, strMetrics;
		JavaRunCommand commandRunner;
		
		//Initialise
		nNouns = 0;
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		commandRunner = new JavaRunCommand();
		strCompleteText="";
		//Extract the phrases
		
		phrasesList = inAnalyser.extractStringPhrasesFromIntent(this.intentIn);
		if(phrasesList != null)
		{
			for(String strPhrase: phrasesList)
			{
				strCompleteText+=strPhrase;
			}
			//commandRunner.setProgram(strProgramId);
			commandRunner.setInputPhrase(strCompleteText);
			
			commandRunner.resetLastResults();
			if (commandRunner.runCommand(""))
				listRet = commandRunner.getLastResults();			
		
			strMetrics = Common.listToString(listRet);
			metricRet = new MetricValue(this);
			metricRet.setValue(strMetrics);
		}
	}

	
}
