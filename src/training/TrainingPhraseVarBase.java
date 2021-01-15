package training;

import generator.Token;

/**
 * Base-class for storing the information related with training phrase variations
 * @author Pablo C. Cañizares
 *
 */
public abstract class TrainingPhraseVarBase extends TrainingPhraseVariation {

	Token originalToken;
	
	public Token getToken() {
		return originalToken;
	}
	
	public TrainingPhraseVarBase(Token originalToken)
	{
		this.originalToken = originalToken;
	}

	protected abstract boolean isEmpty();
	
}
