package training.chaos;

import generator.Token;
import training.PhraseVariation;

/**
 * Base-class for storing the information related with training phrase variations
 * @author Pablo C. Cañizares
 *
 */
public abstract class PhraseVarChaosBase extends PhraseVariation {

	Token originalToken;
	
	public Token getToken() {
		return originalToken;
	}
	
	public PhraseVarChaosBase(Token originalToken)
	{
		
		this.originalToken = originalToken;
	}

	protected abstract boolean isEmpty();
	
}
