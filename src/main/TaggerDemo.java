package main;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.SentenceUtils;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
@SuppressWarnings("unused")
/**
 * Program for trying different features of tagger
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class TaggerDemo  {

	/** A logger for this class */
	private static Redwood.RedwoodChannels log = Redwood.channels(TaggerDemo.class);

	private TaggerDemo() {}

	public static void main(String[] args) throws Exception {
		/*if (args.length != 2) {
      log.info("usage: java TaggerDemo modelFile fileToTag");
      return;
    }
    MaxentTagger tagger = new MaxentTagger(args[0]);
    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(args[1])));
    for (List<HasWord> sentence : sentences) {
      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
      System.out.println(SentenceUtils.listToString(tSentence, false));
    }*/

		String str = "Hello-World:How\nAre You&doing";
		String[] inputs = str.split("(?!^)\\b");
		for (int i=0; i<inputs.length; i++) {
			System.out.println("a[" + i + "] = \"" + inputs[i] + '"');
		}

		System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));

		MaxentTagger tagger = new MaxentTagger(System.getProperty("user.dir")+"/tagmodels/english-bidirectional-distsim.tagger");
		// print the adjectives in one more sentence. This shows how to get at words and tags in a tagged sentence.

		extractNouns(tagger, "Can I set up an appointment to service my bike tomorrow at 2pm?");
		String tagged = tagger.tagString("The. slimy slug. ");
		System.out.println(tagged);
		//List<HasWord> sent = SentenceUtils.toWordList("The slimy slug. ");
		List<HasWord> sent = SentenceUtils.toWordList("The", "slimy", "slug", "crawled", "over", "the", "long", ",", "green", "grass", ".");
		List<TaggedWord> taggedSent = tagger.tagSentence(sent);
		for (TaggedWord tw : taggedSent) {
			if (tw.tag().startsWith("NN")) {
				System.out.println(tw.word());
			}
		}
	}
	public static LinkedList<String> extractNouns(MaxentTagger tagger, String strPhrase)
	{
		LinkedList<String> retList;		
		String taggedPhrase;
		String[] lines;

		retList = null;

		if(strPhrase != null && !strPhrase.isBlank())
		{
			retList = new LinkedList<String>();
			taggedPhrase = tagger.tagString(strPhrase);
			lines = taggedPhrase.split(" ");


			for (String line : lines) {
				if(line.endsWith("_NN"))
				{
					retList.add(line.replace("_NN", ""));
					System.out.println(">"+line);
				}

			}
		}


		//List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader("The slimy slug crawled over the long , green grass."));
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new StringReader("Can I set up an appointment to service my bike tomorrow at 2pm?"));
		for (List<HasWord> sentence : sentences) {

			/*List<TaggedWord> tSentence = tagger.tagSentence(sentence);

			System.out.println(SentenceUtils.listToString(tSentence, false));*/
			for(HasWord hw : sentence)
			{
				System.out.println(hw.word());
			}

		}


		return retList;
	}

	

}