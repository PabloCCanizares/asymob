package training;

import operators.base.EMutationOperators;

/**
 * Base-class for storing the information related with training phrase variations
 * @author Pablo C. Cañizares
 *
 */
public abstract class TrainingPhraseVariation {

	protected EMutationOperators mutOp;
	protected abstract boolean isEmpty();
}
