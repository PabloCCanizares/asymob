package variants.operators;

import java.util.LinkedList;

import variants.operators.base.EMutationOperators;
import variants.operators.base.MutationOperator;

public class MutateUtteranceDistancesOp extends MutationOperator{

	int nPercentage;
	int nVariability;
	
	public MutateUtteranceDistancesOp()
	{
		this(1,1,10,0);
	}

	public MutateUtteranceDistancesOp(int nMin, int nMax, int nPercentage, int nVariability)
	{		
		this.eMutOp = EMutationOperators.EMutateUtteranceDistances;		
		this.nMax = nMax;
		this.nMin = nMin;
		this.nPercentage = nPercentage;
		this.nVariability = nVariability;
		bCommandLineOp = true;
	}

	@Override
	public String ToString() {
		
		return String.format("%s %d %d", this.eMutOp, nPercentage, nVariability);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		
		return null;
	}	
}
