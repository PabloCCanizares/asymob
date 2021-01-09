package operators;

import java.util.LinkedList;

import operators.base.EMutationOperators;
import operators.base.MutationOperator;

public class MutObjectsToSynonymsOp extends MutationOperator{

	int nPercentage;
	
	public MutObjectsToSynonymsOp()
	{
		this(1,1,10);
	}

	public MutObjectsToSynonymsOp(int nMin, int nMax, int nPercentage)
	{		
		this.eMutOp = EMutationOperators.EMutObjectsToSynonyms;		
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
