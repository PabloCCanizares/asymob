package metrics.operators.intents;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import metrics.base.MetricValue;
import metrics.operators.EMetricOperator;
import metrics.operators.base.IntentMetricBase;

public class IntentPositiveSentiment extends IntentMetricBase{

	public IntentPositiveSentiment(EMetricOperator metric) {
		super(metric);
	}

	public IntentPositiveSentiment() {
		super(EMetricOperator.ePositiveSentiment);
	}

	@Override
	public void calculateMetric() {
		String strValue;
		Annotation annotation;

		// Add in sentiment
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		//props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");

		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// Initialize an Annotation with some text to be annotated. The text is the argument to the constructor.

		//Extract phrases from intent

		annotation = new Annotation(" I am very happy. Can I setup an appointment to service my bike tomorrow at 2pm?");

		// run all the selected Annotators on this text
		pipeline.annotate(annotation);

		// An Annotation is a Map with Class keys for the linguistic analysis types.
		// You can get and use the various analyses individually.
		// For instance, this gets the parse tree of the first sentence in the text.
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		if (sentences != null && ! sentences.isEmpty()) {
			CoreMap sentence = sentences.get(0);

			strValue = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

			this.metricRet = new MetricValue();
			this.metricRet.setMetricApplied(this);
			this.metricRet.setValue(strValue); 
		}
	}

}
