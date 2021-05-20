package metrics.operators.intents;

import java.util.LinkedList;
import java.util.List;
import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import metrics.base.IntegerListMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import support.stanford.StandfordPipeline;

/**
 * Implements Socher et al’s sentiment model
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class IntentTrainingSentiment extends IntentMetricBase{

	public IntentTrainingSentiment(EMetricOperator metric) {
		super(metric);
	}

	public IntentTrainingSentiment() {
		super(EMetricOperator.eIntentTrainingSentiment);
	}

	@Override
	public void calculateMetric() {
		int nPositive, nNegative, nNeutral;
		String strValue;
		Annotation annotation;
		IntentAnalyser inAnalyser = null;
		LinkedList<String> phrasesList;
		LinkedList<Integer> intList;
		
		inAnalyser = new IntentAnalyser();
		nPositive = nNegative = nNeutral = 0;
		
		//Extract phrases from intent
		phrasesList = inAnalyser.extractStringTrainingPhrasesFromIntent(this.intentIn);
		if(phrasesList != null)
		{
			for(String strPhrase: phrasesList)
			{
				// run all the selected Annotators on this text
				annotation = new Annotation(strPhrase);
				StandfordPipeline.getInstance().annotate(annotation);
			
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
			
			intList = new LinkedList<Integer>();
			intList.add(nPositive);
			intList.add(nNeutral);
			intList.add(nNegative);
			
			this.metricRet = new IntegerListMetricValue(this, intList);
			this.metricRet.setValue(strValue); 
		}
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "ITS";
		this.strMetricDescription = "Intent training sentiment";
	}	
}
