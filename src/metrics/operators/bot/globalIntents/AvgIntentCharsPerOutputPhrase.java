package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgIntentCharsPerOutputPhrase extends BotMetricBase{

	private final String METRIC_NAME = "CPOP";
	private final String METRIC_DESCRIPTION = "Average characters per bot's output phrase";
	
	public AvgIntentCharsPerOutputPhrase() {
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
		int nAux, nPhrases, nChars;
		
		fValue=0;
		botAnalyser = new BotAnalyser();
		intentAnalyser = new IntentAnalyser();
		
		phrasesList = botAnalyser.extractAllBotOutputPhrases(botIn);
		
		if(phrasesList != null)
		{
			nPhrases=phrasesList.size();
			nChars = intentAnalyser.getTotalCharsFromList(phrasesList);
			
			fValue = (float)((float)nChars / (float)nPhrases);
		}
		
		return fValue;
	}

	@Override
	public void setMetadata() {
		this.strMetricName = METRIC_NAME;
		this.strMetricDescription = METRIC_DESCRIPTION;
	}

}
