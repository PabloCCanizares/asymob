package operators;

import java.util.LinkedList;

import operators.base.EMutationOperators;
import operators.base.MutationOperator;

public class MutActiveToPassiveOp extends MutationOperator{

	
	public MutActiveToPassiveOp()
	{
		this(1,1,10,0);
	}

	public MutActiveToPassiveOp(int nMin, int nMax, int nPercentage, int nVariability)
	{		
		this.eMutOp = EMutationOperators.EMutActiveToPassive;		
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

	//Podria hacerse un modelo para facilitar el enlace a linea de comandos
}
