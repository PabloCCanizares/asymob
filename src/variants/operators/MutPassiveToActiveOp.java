package variants.operators;

import java.util.LinkedList;

import variants.operators.base.EMutationOperators;
import variants.operators.base.MutationOperator;

public class MutPassiveToActiveOp extends MutationOperator{

	
	public MutPassiveToActiveOp()
	{
		this(1,1,10,0);
	}

	public MutPassiveToActiveOp(int nMin, int nMax, int nPercentage, int nVariability)
	{		
		this.eMutOp = EMutationOperators.EMutPassiveToActive;		
		this.nMax = nMax;
		this.nMin = nMin;
		bCommandLineOp = true;
	}

	@Override
	public String ToString() {
		
		return String.format("%s", this.eMutOp);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		
		return null;
	}	
}
