package variants.operators;

import java.util.LinkedList;

import variants.operators.base.EMutationOperators;
import variants.operators.base.MutationOperator;

public class MutRandomTraductionChainedOp extends MutationOperator{

	int nPercentage;
	int nLangs;
	
	public MutRandomTraductionChainedOp()
	{
		this(1,1,10, 2);
	}

	public MutRandomTraductionChainedOp(int nMin, int nMax, int nPercentage, int nLangs)
	{		
		this.eMutOp = EMutationOperators.ETraductionChained;		
		this.nMax = nMax;
		this.nMin = nMin;
		this.nPercentage = nPercentage;
		this.nLangs = nLangs;
		
		bCommandLineOp = true;
	}

	@Override
	public String ToString() {
		
		return String.format("%s %d %d", this.eMutOp, nPercentage, nLangs);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		
	
		return null;
	}	
}
