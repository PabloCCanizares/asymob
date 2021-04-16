package stanford;

import java.util.Properties;

import analyser.IntentAnalyser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StandfordPipeline {

	private static StandfordPipeline instanceStanford;
	private StanfordCoreNLP pipeline;
	
	public static StandfordPipeline getInstance() {

		if (instanceStanford==null) {

			instanceStanford=new StandfordPipeline();
		}
		return instanceStanford;
	}
	private StandfordPipeline()
	{
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}
	public void annotate(Annotation annotation) {
				
		pipeline.annotate(annotation);
	}
}