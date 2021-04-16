package main;

/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.ModelException;
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
import metrics.tensorflow.TensorflowHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
