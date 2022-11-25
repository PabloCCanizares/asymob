package support.tensorflow;

import java.io.IOException;
import java.util.Arrays;
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

public class TensorflowHandler {

	private static TensorflowHandler instance;
	private Predictor<String[], float[][]> predictor=null;
	
	public static TensorflowHandler getInstance()
	{
		if(instance == null)
		{
			instance = new TensorflowHandler();
		}
		
		return instance;
	}
	
	private TensorflowHandler()
	{
		if(predictor == null)
			loadModel();
	}
	
	public void loadModel()
	{
		
		if ("TensorFlow".equals(Engine.getInstance().getEngineName())) {
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
	        
            ZooModel<String[], float[][]> model;
			try {
				model = ModelZoo.loadModel(criteria);
				 predictor = model.newPredictor();
			} catch (ModelNotFoundException e) {
				System.out.println("Model not found");
				e.printStackTrace();
			} catch (MalformedModelException e) {
				System.out.println("Malformed model");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	public float[][] predict(List<String> inputs)
	{
		float[][] fRet;
		
		fRet = null;
		try {
			if(predictor != null)
				fRet = predictor.predict(inputs.toArray(new String[0])) ;
		} catch (TranslateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fRet;
	}
	
	/*
	 public  float[][] predict(List<String> inputs)
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
	    }*/
	 
		public static final class MyTranslator implements Translator<String[], float[][]> {

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
	            try (NDList result = new NDList()) {
					long numOutputs = list.singletonOrThrow().getShape().get(0);
					for (int i = 0; i < numOutputs; i++) {
					    result.add(list.singletonOrThrow().get(i));
					}
					return result.stream().map(NDArray::toFloatArray).toArray(float[][]::new);
				}
	        }

	        @Override
	        public Batchifier getBatchifier() {
	            return null;
	        }
	    }
}
