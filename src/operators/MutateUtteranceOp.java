package operators;

import java.util.LinkedList;

import operators.base.EMutationOperators;
import operators.base.MutationOperator;

public class MutateUtteranceOp extends MutationOperator{

	int nPercentage;
	int nVariability;
	
	public MutateUtteranceOp()
	{
		this(1,1,10,0);
	}

	public MutateUtteranceOp(int nMin, int nMax, int nPercentage, int nVariability)
	{		
		this.eMutOp = EMutationOperators.EMutateUtterance;		
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
		
		LinkedList<String> listRet;
		
		//In this case
		// TODO: Implementarlo en Java para ver el cambio
		listRet = new LinkedList<String>();
		
		//strInputPhrase = strInputPhrase.replace("a","4");
		//strInputPhrase = strInputPhrase.replace("e","3");
		strInputPhrase = strInputPhrase.replace("o","0");
		listRet.add(strInputPhrase);
		
		return listRet;
	}	
}
