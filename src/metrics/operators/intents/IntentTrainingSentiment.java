package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import stanford.StandfordPipeline;
import stanford.StandfordTagger;

public class IntentTrainingSentiment extends IntentMetricBase{

	public IntentTrainingSentiment(EMetricOperator metric) {
		super(metric);
	}

	public IntentTrainingSentiment() {
		super(EMetricOperator.ePositiveSentiment);
	}

	@Override
	public void calculateMetric() {
		String strValue;
		Annotation annotation;
		IntentAnalyser inAnalyser = null;
		LinkedList<String> phrasesList;
		int nPositive, nNegative, nNeutral, nTotal;
		
		// Add in sentiment
		/*Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		//props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		*/
		inAnalyser = new IntentAnalyser();

		nPositive = nNegative = nNeutral = nTotal = 0;
		
		//Extract phrases from intent
		phrasesList = inAnalyser.extractStringPhrasesFromIntent(this.intentIn);
		if(phrasesList != null)
		{
			for(String strPhrase: phrasesList)
			{
				// run all the selected Annotators on this text
				annotation = new Annotation(strPhrase);
				StandfordPipeline.getInstance().annotate(annotation);
				nTotal++;
			
			
				List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
				
				for(CoreMap sentence: sentences)
				{
					strValue = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
					
					if(strValue.contains("Positive"))
						nPositive++;
					else if (strValue.contains("Negative"))
						nNegative++;
					else if (strValue.contains("Neutral"))
						nNeutral++;
				}
			}
			if(nPositive > nNegative && nPositive > nNeutral)
				strValue = String.format("Positive [%d, %d, %d]", nPositive, nNeutral, nNegative);
			else if(nNegative > nPositive && nNegative > nNeutral)
				strValue = String.format("Negative [%d, %d, %d]", nPositive, nNeutral, nNegative);
			else  
				strValue = String.format("Neutral [%d, %d, %d]", nPositive, nNeutral, nNegative);
				
			this.metricRet = new MetricValue(this);
			this.metricRet.setValue(strValue); 
		}

	}

}
