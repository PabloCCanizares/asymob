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
		int nPositive, nNegative, nNeutral, nTotal, nAux;	
		float fPositive, fNegative, fNeutral;
		String strValue;
		Annotation annotation;
		BotAnalyser botAnalyser = null;
		LinkedList<String> phrasesList;
		LinkedList<Integer> intList, auxSortList;

		
		intList = null;
		botAnalyser = new BotAnalyser();

		fPositive = fNeutral = fNegative = (float) 0.0;
		nPositive = nNegative = nNeutral = nTotal = 0;
		

		//Extract phrases from intent
		phrasesList = botAnalyser.extractAllBotOutputPhrases(this.botIn);
		
		if(phrasesList != null/* && phrasesList.size()>0*/)
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
					{
						System.out.println("Negative phrase:"+strPhrase);
						nNegative++;
					}
					else if (strValue.contains("Neutral"))
						nNeutral++;
					
					nTotal++;
				}
			}
			System.out.printf(String.format("[%d, %d, %d]", nPositive, nNeutral, nNegative));
			
			
			if(nPositive>0)
				fPositive = (float)100.0*((float)nPositive/(float)nTotal);
			
			if(nNeutral>0)
				fNeutral = (float)100*((float)nNeutral/(float)nTotal);		
			
			if(nNegative>0)
				fNegative =  (float)100*((float)nNegative/(float)nTotal);				
			
			intList = ConvertFloatToPercentages(fPositive, fNeutral, fNegative);
			
			nPositive = intList.get(0);
			nNeutral = intList.get(1);
			nNegative = intList.get(2);
			
			if(nPositive > nNegative && nPositive > nNeutral)
				strValue = String.format("Positive [%d, %d, %d]", nPositive, nNeutral, nNegative);
			else if(nNegative > nPositive && nNegative > nNeutral)
				strValue = String.format("Negative [%d, %d, %d]", nPositive, nNeutral, nNegative);
			else  
				strValue = String.format("Neutral [%d, %d, %d]", nPositive, nNeutral, nNegative);
			
		}
		else
		{
			intList = ConvertFloatToPercentages(0, 0, 0);
		}
		return intList;
	}
	
	//This method could be generalised by using the '...' operator
	private LinkedList<Integer> ConvertFloatToPercentages(float fPositive, float fNeutral, float fNegative) {
		
		LinkedList<Integer> intRet;
		int[] intOrderArray, intSentiments;
		float[] flArray;
		int nPartialSum, nAdjustIndex;
		
		intRet = new LinkedList<Integer>();
		nPartialSum=0;
		intSentiments = new int[3];

		//intSentiments[0] = (int) (Math.floor((float)100*((float)nNeutral/(float)nTotal)));
		//Store the non-decimal part
		intSentiments[0] = (int)fPositive;
		intSentiments[1] = (int)fNeutral;
		intSentiments[2] = (int)fNegative;
		
		for(int i=0;i<intSentiments.length;i++)
		{
			nPartialSum += intSentiments[i];
		}
		nAdjustIndex = 100 - nPartialSum;
		
		if(nPartialSum > 0 && nAdjustIndex>0)
		{
			flArray = new float[3];
			intOrderArray = new int[3];
			
			//Initialise positions
			for(int i=0;i<intOrderArray.length;i++)
			{
				intOrderArray[i]=i;
			}
			
			//store the decimal part
			flArray[0] = (float)fPositive-intSentiments[0];
			flArray[1] = (float)fNeutral-intSentiments[1];
			flArray[2] = (float)fNegative-intSentiments[2];
			
			//sort the decimal part
			for(int i=0;i<flArray.length;i++)
			{
				for(int j=i+1;j>0&&i+1<flArray.length;j--)
				{
					//swap
					if(flArray[j]>flArray[i])
					{
						float flAux;
						int nAux;
						
						flAux = flArray[i];
						flArray[i]=flArray[j];
						flArray[j] = flAux;
						
						nAux = intOrderArray[i];
						intOrderArray[i] = intOrderArray[j];
						intOrderArray[j]=nAux;
					}
				}
			}
			
			for(int i=0;i<nAdjustIndex;i++)
			{
				intSentiments[i%intSentiments.length]++;
			}
		}
		for(int i=0;i<intSentiments.length;i++)
		{
			intRet.add(intSentiments[i]);
		}
		
		return intRet;
	}

	@Override
	public void setMetadata() {
		this.strMetricName = "SNT";
		this.strMetricDescription = "Sentiment of the bot's interactions";
	}
}
