package variants;

import java.util.LinkedList;

import training.PhraseVariation;

public interface IMutantsReduceMethod {

	public LinkedList<PhraseVariation> reduceMutants(LinkedList<PhraseVariation> variantTemp, int maxMutants);
}
