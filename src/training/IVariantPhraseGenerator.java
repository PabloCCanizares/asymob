package training;

import generator.Bot;
import generator.Intent;
import operators.base.MutationOperatorSet;

public interface IVariantPhraseGenerator {

	public boolean generateTrainingPhraseFull(Bot botIn, MutationOperatorSet cfgIn);
	public boolean generateTrainingPhrase(Intent intentIn, MutationOperatorSet cfgIn);
	public boolean applyTrainingPhrasesToChatbot();
	public VariationsCollectionText getVariationsCollectionTxt();
	
}
