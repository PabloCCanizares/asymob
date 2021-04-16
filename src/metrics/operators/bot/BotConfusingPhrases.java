package metrics.operators.bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import analyser.IntentAnalyser;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;
import metrics.tensorflow.TensorflowHandler;

public class BotConfusingPhrases extends IntentMetricBase{

	public BotConfusingPhrases() {
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
		float[][] embeddings;
		List<String> inputs = new ArrayList<>();
	        
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
	/*
    public static float[][] predict(List<String> inputs)
            throws MalformedModelException, ModelNotFoundException, IOException,
                    TranslateException {
        if (!"TensorFlow".equals(Engine.getInstance().getEngineName())) {
            return null;
        }

        String modelUrl =
                "https://storage.googleapis.com/tfhub-modules/google/universal-sentence-encoder/4.tar.gz";

        Criteria<String[], float[][]> criteria =
                Criteria.builder()
                        .optApplication(Application.NLP.TEXT_EMBEDDING)
                        .setTypes(String[].class, float[][].class)
                        .optModelUrls(modelUrl)
                        .optTranslator(new MyTranslator())
                        .optProgress(new ProgressBar())
                        .build();
        try (ZooModel<String[], float[][]> model = ModelZoo.loadModel(criteria);
                Predictor<String[], float[][]> predictor = model.newPredictor()) {
            return predictor.predict(inputs.toArray(new String[0]));
        }
    }

    private static final class MyTranslator implements Translator<String[], float[][]> {

        MyTranslator() {}

        @Override
        public NDList processInput(TranslatorContext ctx, String[] inputs) {
            // manually stack for faster batch inference
            NDManager manager = ctx.getNDManager();
            NDList inputsList =
                    new NDList(
                            Arrays.stream(inputs)
                                    .map(manager::create)
                                    .collect(Collectors.toList()));
            return new NDList(NDArrays.stack(inputsList));
        }

        @Override
        public float[][] processOutput(TranslatorContext ctx, NDList list) {
            NDList result = new NDList();
            long numOutputs = list.singletonOrThrow().getShape().get(0);
            for (int i = 0; i < numOutputs; i++) {
                result.add(list.singletonOrThrow().get(i));
            }
            return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
        }

        @Override
        public Batchifier getBatchifier() {
            return null;
        }
    }*/
}
