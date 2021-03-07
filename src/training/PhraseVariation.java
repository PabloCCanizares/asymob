package training;

import edu.stanford.nlp.patterns.PatternsAnnotations.LongestMatchedPhraseForEachLabel;
import generator.TrainingPhrase;
import operators.base.EMutationOperators;
import operators.base.MutationOperator;

/**
 * Base-class for storing the information related with training phrase variations
 * @author Pablo C. Cañizares
 *
 */
public class PhraseVariation {

	protected String strPhrase;
	protected EMutationOperators mutOp;
	
	public PhraseVariation(String strIn, EMutationOperators mutOp) {
		this.strPhrase = strIn;
		this.mutOp = mutOp;
	}
	public PhraseVariation()
	{
		this.strPhrase = null;
		this.mutOp = null;
	}
	protected boolean isEmpty()
	{
		return strPhrase==null||(strPhrase!=null&&strPhrase.isEmpty());
	}
	public String getPhrase() {
		return strPhrase;
	}
	public void setPhrase(String strPhrase) {
		this.strPhrase = strPhrase;
	}
	public EMutationOperators getMutOp() {
		return mutOp;
	}
	public void setMutOp(EMutationOperators mutOp) {
		this.mutOp = mutOp;
	}

	
	
}
