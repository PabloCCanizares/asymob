package main;

import java.util.Iterator;
import java.util.LinkedList;

import operators.base.MutationOperator;

public class MutationOperatorSet {

	LinkedList<MutationOperator> operatorList;
	Iterator<MutationOperator> itList;
	
	public  MutationOperatorSet()
	{
		operatorList = new LinkedList<MutationOperator>();
	}
	public void insertOperator(MutationOperator opIn)
	{
		operatorList.add(opIn);
	}

	/*
	public  static MutOpsCfg getInstance() {

		if (mutOps==null) {

			mutOps=new MutOpsCfg();
		}
		return mutOps;
	}	*/
	
	public void resetIndex() {
		itList = operatorList.iterator();
	}
	public boolean hasNext() {
		boolean bRet;
		
		bRet = false;
		if(itList != null)
		{
			if(itList.hasNext())
				bRet = true;
		}
		return bRet;
	}
	public MutationOperator getNext() {
		MutationOperator opRet;
		
		opRet = null;
		if(itList != null)
		{
			opRet = (MutationOperator) itList.next();
		}
		return opRet;
	}
}
