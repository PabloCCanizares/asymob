package variants.operators;

import java.util.LinkedList;

import variants.operators.base.EMutationOperators;
import variants.operators.base.MutationOperator;

public class MutTraductionChainedOp extends MutationOperator{

	LinkedList<String> tradChain;
	
	public MutTraductionChainedOp()
	{
		this(1,1,null);
	}

	public MutTraductionChainedOp(int nMin, int nMax, LinkedList<String> tradChain)
	{		
		this.eMutOp = EMutationOperators.ETraductionChained;		
		this.nMax = nMax;
		this.nMin = nMin;
		
		if(tradChain != null)
			this.tradChain = tradChain;		
		else
		{	
			tradChain = new LinkedList<String>();
			tradChain.add("en");
			tradChain.add("spa");
			tradChain.add("fr");
			tradChain.add("spa");
			
		}
		
		bCommandLineOp = true;
	}

	@Override
	public String ToString() {
		
		return String.format("%s %s", this.eMutOp, tradChain.toString());
	}

	@Override
	protected LinkedList<String> doVariantsByDefault(String strInputPhrase) {
		
		return null;
	}	
}
