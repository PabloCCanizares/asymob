package variants.operators;

import java.util.LinkedList;

import variants.operators.base.EMutationOperators;
import variants.operators.base.MutationOperator;

public class MutAdverbsToAntonymsOp extends MutationOperator{

	int nPercentage;
	
	public MutAdverbsToAntonymsOp()
	{
		this(1,1,10);
	}

	public MutAdverbsToAntonymsOp(int nMin, int nMax, int nPercentage)
	{		
		this.eMutOp = EMutationOperators.EMutAdverbsToSynonyms;		
		this.nMax = nMax;
		this.nMin = nMin;
		this.nPercentage = nPercentage;
		bCommandLineOp = true;
	}
	
	@Override
	public String ToString() {
		return String.format("%s %d", this.eMutOp, nPercentage);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		// TODO Auto-generated method stub
		return null;
	}

}
