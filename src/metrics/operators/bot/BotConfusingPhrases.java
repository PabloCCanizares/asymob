package metrics.operators.bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.Batchifier;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import analyser.IntentAnalyser;
import main.UniversalSentenceEncoder;
import main.UniversalSentenceEncoder.MyTranslator;
import metrics.base.FloatMetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

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
		List<String> inputs = new ArrayList<>();
	        
		fReturn = 0;
		for(String strPhrase: phrasesList)
		{
			 inputs.add(strPhrase);
		}
		
		try {
			float[][] embeddings = UniversalSentenceEncoder.predict(inputs);
			
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
		} catch (MalformedModelException e) {
			e.printStackTrace();
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TranslateException e) {
			e.printStackTrace();
		}
		
		
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
    }
}
