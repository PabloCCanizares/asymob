package metrics.standalone_operations;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import support.tensorflow.TensorflowHandler;

public class IntentAvgCosineSimilarity2 extends IntentMetricBase{

	public IntentAvgCosineSimilarity2() {
		super(EMetricOperator.eIntentAvgCosineSimilarity);
		
	}

	@Override
	public void calculateMetric() {
		float fAverage;
		IntentAnalyser inAnalyser;
		LinkedList<LinkedList<String>> lanList;
		int nLanguages;
		
		nLanguages = 0;
		fAverage = 0;
		inAnalyser = new IntentAnalyser();
		
		lanList = inAnalyser.getAllPhrases(intentIn);
		
		for(LinkedList<String> phrasesList: lanList)
		{
			fAverage += calculateIntentSimilarity(phrasesList);
			nLanguages++;
		}
		
		fAverage = (float)fAverage / (float)nLanguages;
		metricRet = new FloatMetricValue(this, fAverage);
	}

	private float calculateIntentSimilarity(LinkedList<String> phrasesList) {
		
		double dAcc;
		int nElements;
		float fReturn;
		List<String> inputs = new ArrayList<>();
		float[][] embeddings;
		
		fReturn = 0;
		for(String strPhrase: phrasesList)
		{
			 inputs.add(strPhrase);
		}
		

		embeddings = TensorflowHandler.getInstance().predict(inputs);
		
		if(embeddings != null)
		{
			dAcc = 0;
			nElements = 0;
			for(int i=0;i<phrasesList.size();i++)
			{
				for(int j=0;j<phrasesList.size();j++)
				{
					if(i!=j)
					{
						dAcc += cosineSimilarity(embeddings[i], embeddings[j]);
						nElements++;
					}
				}
			}
			fReturn = (float) ((float)dAcc/(float)nElements);
		}
		else
			fReturn = 0;
		
		return fReturn;
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
		this.strMetricName = "ICS";
		this.strMetricDescription = "Average cosine similarity";
	}
}
