package metrics.operators.bot.globalIntents;

import java.util.LinkedList;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;

public class AvgIntentReadingTime extends BotMetricBase{

	private final float WRPM = 70;
	public AvgIntentReadingTime() {
		super(EMetricOperator.eGlobalAvgReadingTime);
	}

	@Override
	public void calculateMetric() {
		float fWords, fSeconds;
		
		fSeconds = 0;
		fWords = getAvgNumWordsByOutputPhrases();
		if(fWords>0)
			fSeconds = (long) (fWords * (60/WRPM));
		
		metricRet = new FloatMetricValue(this, fSeconds);
	}

	private float getAvgNumWordsByOutputPhrases() {
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
		this.strMetricName = "READ";
		this.strMetricDescription = "Average reading time of the bot's interactions"; 
	}
}
