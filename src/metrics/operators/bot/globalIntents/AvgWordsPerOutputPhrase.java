package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgWordsPerOutputPhrase extends BotMetricBase{

	private final String METRIC_NAME = "WPOP";
	private final String METRIC_DESCRIPTION = "Average words per bot's output phrase";
	
	public AvgWordsPerOutputPhrase() {
		super(EMetricOperator.eGlobalAvgIntentCharsPerOutputPhrase);
	}

	@Override
	public void calculateMetric() {
		float fLiteralsAvg;

		if(db != null)
		{
			fLiteralsAvg = getAvgNumCharsByOutputPhrases();
			metricRet = new FloatMetricValue(this, fLiteralsAvg);
		}
	}


	private float getAvgNumCharsByOutputPhrases() {
		BotAnalyser botAnalyser;
		IntentAnalyser intentAnalyser;
		LinkedList<String> phrasesList;
		float fValue;
		int nAux, nPhrases, nWords;
		
		fValue=0;
		botAnalyser = new BotAnalyser();
		intentAnalyser = new IntentAnalyser();
		
		phrasesList = botAnalyser.extractAllBotOutputPhrases(botIn);
		
		if(phrasesList != null)
		{
			nPhrases=phrasesList.size();
			nWords = intentAnalyser.getTotalWordsFromList(phrasesList);
			
			if(nWords != 0 && nPhrases != 0)
				fValue = (float)((float)nWords / (float)nPhrases);
			else fValue =0;
		}
		
		return fValue;
	}

	@Override
	public void setMetadata() {
		this.strMetricName = METRIC_NAME;
		this.strMetricDescription = METRIC_DESCRIPTION;
	}

}
