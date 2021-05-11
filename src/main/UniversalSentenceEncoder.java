package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import support.tensorflow.TensorflowHandler;

/**
 * An example of inference using an universal sentence encoder model from TensorFlow Hub.
 *
 * <p>Refer to <a href="https://tfhub.dev/google/universal-sentence-encoder/4">Universal Sentence
 * Encoder</a> on TensorFlow Hub for more information.
 */
public final class UniversalSentenceEncoder {
    private static final Logger logger = LoggerFactory.getLogger(UniversalSentenceEncoder.class);

    private UniversalSentenceEncoder() {}

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        List<String> inputs = new ArrayList<>();
        //inputs.add("The quick brown fox jumps over the lazy dog.");
        //inputs.add("I am a sentence for which I would like to get its embedding");
        inputs.add("How old are you?");
        inputs.add("What is your age?");
        //inputs.add("we are sorry for the inconvenience");
        //inputs.add("we are sorry for the delay");
        //inputs.add("we regret for your inconvenience");
        //inputs.add("we don't deliver to baner region in pune");
        //inputs.add("we will get you the best possible rate");
    		
        float[][] embeddings = TensorflowHandler.getInstance().predict(inputs);
        if (embeddings == null) {
            logger.info("This example only works for TensorFlow Engine");
        } else {
            for (int i = 0; i < inputs.size(); i++) {
                logger.info(
                        "Embedding for: " + inputs.get(i) + "\n" + Arrays.toString(embeddings[i]));
            
                logger.info(
                        "Similarity: " + cosineSimilarity(embeddings[0], embeddings[i]));
                System.out.println("Similarity: " + cosineSimilarity(embeddings[0], embeddings[i]));
            }
            
        }
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
   

    
}
