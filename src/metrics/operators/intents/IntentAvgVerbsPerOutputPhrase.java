package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.TaggedWord;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import support.stanford.StandfordTagger;

public class IntentAvgVerbsPerOutputPhrase extends IntentMetricBase{

	public IntentAvgVerbsPerOutputPhrase() {
		super(EMetricOperator.eIntentAvgVerbsPerOutputPhrase);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		BotAnalyser botAnalyser;
		int nPhrases, nNouns;
		float fAverage;
		LinkedList<String> phrasesList;
		List<List<TaggedWord>>  taggedPhraseList;
		nNouns = 0;
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		botAnalyser = new BotAnalyser();
		//Extract the phrases
		
		phrasesList = botAnalyser.extractStringOutputPhrasesFromIntent(this.botIn, this.intentIn);
		
		if(phrasesList != null)
		{
			for(String strPhrase: phrasesList)
			{
				taggedPhraseList = StandfordTagger.getInstance().getTaggedWordList(strPhrase);
				
				nNouns += countVerbs(taggedPhraseList);
			}
			
			nPhrases = phrasesList.size();
			
			if(nNouns >0 && nPhrases >0)
				fAverage = (float)((float)nNouns/(float)nPhrases);
		}

		
		metricRet = new FloatMetricValue(this, fAverage);
	}

	private int countVerbs(List<List<TaggedWord>> taggedPhraseList) {
		int nRet;
		String strTag;
		nRet = 0;
		
		if(taggedPhraseList != null)
		{
			for(List<TaggedWord> tagList: taggedPhraseList)
			{
				for(TaggedWord tagWord: tagList)
				{
					strTag = tagWord.tag();
					if(strTag != null && strTag.indexOf("VB") != -1)
						nRet++;
				}
			}
		}

		return nRet;
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "IVPOP";
		this.strMetricDescription = "Average number of verbs per output phrase";
	}
}