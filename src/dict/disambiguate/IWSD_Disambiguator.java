package dict.disambiguate;

import java.util.LinkedList;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public interface IWSD_Disambiguator {

	public boolean disambiguatePhrase(String strPhrase);
	LinkedList<DisTaggedWord> getDisambiguatedPhrase();
	public int getSense(List<TaggedWord> tagList, TaggedWord tag, int nOrder);
}
