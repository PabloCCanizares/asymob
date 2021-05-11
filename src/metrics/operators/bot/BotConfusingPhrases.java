package metrics.operators.bot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import generator.Language;
import metrics.base.FloatMetricValue;
import metrics.base.IntMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import support.tensorflow.TensorflowHandler;

public class BotConfusingPhrases extends BotMetricBase{

	private final float fThresold = (float) 0.60;
	HashMap<Language, LinkedList<LinkedList<String>>> hashMapPhrases;
	public BotConfusingPhrases() {
		super(EMetricOperator.eBotConfusingPhrases);
		
	}

	@Override
	public void calculateMetric() {
		HashMap<Language, LinkedList<LinkedList<String>>> lanMap;
		BotAnalyser botAnalyser;
		LinkedList<LinkedList<String>> lanPhrases;
		LinkedList<String> confusingPhrases, confusingAux;
		Language lan;
		int nLanguages, nConfusingPhrases;
		
		nLanguages = 0;
		nConfusingPhrases = 0;
		botAnalyser = new BotAnalyser();
		
		lanMap = botAnalyser.getPhrasesHashMap(this.botIn);

		//Iterate the map;
		for (Map.Entry me : lanMap.entrySet()) {
			lan = (Language) me.getKey();
			lanPhrases = (LinkedList<LinkedList<String>>) me.getValue();
			
			//Iterate intentPhrases
			for(int i=0;i<lanPhrases.size();i++)
			{
				LinkedList<String> phrasesList = lanPhrases.get(i);
				
				for(String strPhrase: phrasesList)
				{
					confusingPhrases = new LinkedList<String>();
					for(int j=i+1;j<lanPhrases.size();j++)
					{
						LinkedList<String> phrasesList2 = lanPhrases.get(j);
						
						confusingAux = calculateConfusing(strPhrase, phrasesList2);
						
						if(confusingAux.size()>0)
						{
							confusingPhrases.addAll(confusingAux);
							nConfusingPhrases+= confusingAux.size()-1;
						}
					}
				}
			}
			//fAverage += calculateIntentSimilarity(phrasesList);
			nLanguages++;
		}
			
		
		metricRet = new IntegerMetricValue(this, nConfusingPhrases);
	}

	private LinkedList<String> calculateConfusing(String strPhrase, LinkedList<String> phrasesList2) {
		LinkedList<String> newPhraseComp, retList;
		float[][] embeddings;
		float fConfusing;
		int nConfusingPhrases;
		
		nConfusingPhrases = 0;
		//Create new LinkedList
		retList = new LinkedList<String>();
		newPhraseComp = new LinkedList<String>();
		newPhraseComp.add(strPhrase);
		newPhraseComp.addAll(phrasesList2);
		
		embeddings = TensorflowHandler.getInstance().predict(newPhraseComp);
		
		for(int i=1;i<newPhraseComp.size();i++)
		{
			fConfusing = (float) cosineSimilarity(embeddings[0], embeddings[i]);
			
			if(fConfusing > fThresold)
			{
				if(retList.size() ==0)
					retList.add(strPhrase);
				
				retList.add(newPhraseComp.get(i));
				nConfusingPhrases++;
			}
		}
		return retList;
	}

	
	public static double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }   
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
	@Override
	public void setMetadata() {
		this.strMetricName = "CNF";
		this.strMetricDescription = "Confusing phrases";
	}
}
