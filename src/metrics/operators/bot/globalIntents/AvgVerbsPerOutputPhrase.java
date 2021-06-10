package metrics.operators.bot.globalIntents;

import java.util.LinkedList;
import java.util.List;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.TaggedWord;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import metrics.operators.base.DBOperations;
import support.stanford.StandfordTagger;

public class AvgVerbsPerOutputPhrase extends BotMetricBase{

	private final boolean VPOP_PRINT = false;
	private final String METRIC_NAME = "VPOP";
	private final String METRIC_DESCRIPTION = "Average verbs per bot's output phrase";
	
	public AvgVerbsPerOutputPhrase() {
		super(EMetricOperator.eGlobalAvgIntentVerbsPerOutputPhrase);
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
		List<List<TaggedWord>>  taggedPhraseList;
		
		fValue=0;
		nWords=0;
		botAnalyser = new BotAnalyser();
		intentAnalyser = new IntentAnalyser();
		
		fValue = DBOperations.getAverage(db, EMetricOperator.eIntentAvgVerbsPerOutputPhrase);
		
		if(fValue == -1)
		{
			//First: search it in the database
			phrasesList = botAnalyser.extractAllBotOutputPhrases(botIn);
			
			if(phrasesList != null)
			{
				for(String strPhrase: phrasesList)
				{
					try
					{
						if(strPhrase.length()<500)
						{
							if(VPOP_PRINT)
								System.out.println(METRIC_NAME+" - Analysing phrase ["+String.format("%d]",strPhrase.length())+" "+strPhrase);
							
							taggedPhraseList = StandfordTagger.getInstance().getTaggedWordList(strPhrase);
							nWords +=  StandfordTagger.getInstance().countElements(taggedPhraseList, StandfordTagger.VERB_TAG);
						}

					}catch(Exception e)
					{
						System.out.println("Exception: "+e.getMessage());
					}
				}
				
				nPhrases = phrasesList.size();
				
				if(nWords >0 && nPhrases >0)
					fValue = (float)((float)nWords/(float)nPhrases);
				else 
					fValue =0;								
			}
		}
		
		
		return fValue;
	}

	@Override
	public void setMetadata() {
		this.strMetricName = METRIC_NAME;
		this.strMetricDescription = METRIC_DESCRIPTION;
	}

}
