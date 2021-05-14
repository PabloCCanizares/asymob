package metrics.operators.bot;

import java.util.LinkedList;
import java.util.List;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import generator.Bot;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerListMetricValue;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import support.stanford.StandfordPipeline;

public class BotOutputSentiment extends BotMetricBase{

	public BotOutputSentiment() {
		super(EMetricOperator.eBotOutputSentiment);
	}

	@Override
	public void calculateMetric() {
		LinkedList<Integer> intList;
		
		intList = getSentiment();
		metricRet = new IntegerListMetricValue(this, intList);
	}
	
	public LinkedList<Integer> getSentiment(){
		int nPositive, nNegative, nNeutral;
		String strValue;
		Annotation annotation;
		BotAnalyser botAnalyser = null;
		LinkedList<String> phrasesList;
		LinkedList<Integer> intList;
		
		intList = null;
		botAnalyser = new BotAnalyser();
		nPositive = nNegative = nNeutral = 0;
		
		//Extract phrases from intent
		phrasesList = botAnalyser.extractAllBotOutputPhrases(this.botIn);
		
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
			
		}
		return intList;
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "SNT";
		this.strMetricDescription = "Sentiment of the bot's interactions";
	}
}
