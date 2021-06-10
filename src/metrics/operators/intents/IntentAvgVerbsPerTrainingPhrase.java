package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;

import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.TaggedWord;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import support.stanford.StandfordTagger;

public class IntentAvgVerbsPerTrainingPhrase extends IntentMetricBase{

	public IntentAvgVerbsPerTrainingPhrase() {
		super(EMetricOperator.eIntentAvgVerbsPerTPhrase);
	}

	@Override
	public void calculateMetric() {

		IntentAnalyser inAnalyser;
		int nPhrases, nNouns;
		float fAverage;
		LinkedList<String> phrasesList;
		List<List<TaggedWord>>  taggedPhraseList;
		nNouns = 0;
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		
		//Extract the phrases
		
		phrasesList = inAnalyser.extractStringTrainingPhrasesFromIntent(this.intentIn);
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
		this.strMetricName = "IVPTP";
		this.strMetricDescription = "Average number of verbs per training phrase";
	}
}