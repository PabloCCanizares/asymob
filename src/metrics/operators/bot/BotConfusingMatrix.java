package metrics.operators.bot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import analyser.BotAnalyser;
import analyser.IntentAnalyser;
import generator.Language;
import metrics.base.ConfusingMatrixMetricValue;
import metrics.base.FloatMetricValue;
import metrics.base.IntegerMetricValue;
import metrics.base.confusingmatrix.ConfusingMatrix;
import metrics.base.confusingmatrix.ConfusingRelationPoint;
import metrics.operators.EMetricOperator;
import metrics.operators.base.BotMetricBase;
import support.tensorflow.TensorflowHandler;

public class BotConfusingMatrix extends BotMetricBase{

	HashMap<Language, LinkedList<LinkedList<String>>> hashMapPhrases;
	public BotConfusingMatrix() {
		super(EMetricOperator.eBotConfusingPhrases);
		
	}

	@Override
	public void calculateMetric() {
		HashMap<Language, LinkedList<LinkedList<String>>> lanMap;
		LinkedList<ConfusingMatrix> matrixList;
		BotAnalyser botAnalyser;
		LinkedList<LinkedList<String>> lanPhrases;
		LinkedList<String> confusingPhrases;
		Language lan;
		float fConfusing;
		int nLanguages, nConfusingPhrases, nX, nY;
		ConfusingMatrix cnfMatrix;
		
		nX=nY=0;
		nLanguages = 0;
		nConfusingPhrases = 0;
		botAnalyser = new BotAnalyser();
		matrixList = new LinkedList<ConfusingMatrix>();
		
		lanMap = botAnalyser.getPhrasesHashMap(this.botIn);

		cnfMatrix = null;
		//Iterate the map;
		for (Map.Entry me : lanMap.entrySet()) {
			lan = (Language) me.getKey();
			lanPhrases = (LinkedList<LinkedList<String>>) me.getValue();
			
			if(lanPhrases != null)
			{
				cnfMatrix = new ConfusingMatrix(calculateNumPhrases(lanPhrases));
				//Iterate intentPhrases
				for(int i=0;i<lanPhrases.size();i++)
				{
					LinkedList<String> phrasesList = lanPhrases.get(i);
					
					for(String strPhrase: phrasesList)
					{
						confusingPhrases = new LinkedList<String>();
						nY=0;
						for(int j=0;j<lanPhrases.size();j++)
						{
							LinkedList<String> phrasesList2 = lanPhrases.get(j);
							
							for(String strPhraseY: phrasesList2)
							{
								fConfusing = calculateConfusing(strPhrase, strPhraseY);
								
								cnfMatrix.addConfusingPoint(nX, nY, new ConfusingRelationPoint(strPhrase,strPhraseY, fConfusing));
								nY++;
							}							
						}
						nX++;
					}
				}
				cnfMatrix.print();
				matrixList.add(cnfMatrix);
				nLanguages++;
			}
		}
		
		metricRet = new ConfusingMatrixMetricValue(this, matrixList);
	}

	private float calculateConfusing(String strPhrase, String strPhrase2) {
		LinkedList<String> newPhraseComp, retList;
		float[][] embeddings;
		float fConfusing;
		int nConfusingPhrases;
		
		fConfusing=0;
		nConfusingPhrases = 0;
		//Create new LinkedList
		retList = new LinkedList<String>();
		newPhraseComp = new LinkedList<String>();
		newPhraseComp.add(strPhrase);
		newPhraseComp.add(strPhrase2);
		
		embeddings = TensorflowHandler.getInstance().predict(newPhraseComp);
		
		for(int i=1;i<newPhraseComp.size();i++)
		{
			fConfusing = (float) cosineSimilarity(embeddings[0], embeddings[i]);
		}
		return fConfusing;
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
	public int calculateNumPhrases(LinkedList<LinkedList<String>> lanPhrases)
	{
		int nRet;
		
		nRet = 0;
		if(lanPhrases != null)
		{
			for(LinkedList<String> lPhrases: lanPhrases)
			{
				for(String strPhrase: lPhrases)
				{
					nRet++;
				}
			}
		}
		return nRet;
	}
	@Override
	public void setMetadata() {
		this.strMetricName = "CNF";
		this.strMetricDescription = "Confusing phrases";
	}
}
