package operators;

import java.util.LinkedList;

import operators.base.EMutationOperators;
import operators.base.MutationOperator;

public class MutChangeWordToNumberOp extends MutationOperator{

	public MutChangeWordToNumberOp()
	{
		this(1,1);
	}
	public MutChangeWordToNumberOp(int nMin, int nMax)
	{
		this.nMin = nMin;
		this.nMax = nMax;
		
		if(nMin>nMax)
			nMax = nMin;
		
		this.eMutOp = EMutationOperators.EChangeWordToNumber;
	}
	@Override
	public String ToString() {
		
		return String.format("%s", this.eMutOp);
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		// TODO Unimplemented method
		return null;
	}

}
