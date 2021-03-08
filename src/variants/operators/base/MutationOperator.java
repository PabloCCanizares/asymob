package variants.operators.base;

import java.util.Collections;
import java.util.LinkedList;

import variants.IVariantGenerator;

public abstract class MutationOperator {

	protected int nMax;
	protected int nMin;	
	protected boolean bCommandLineOp;
	protected EMutationOperators eMutOp;
	protected IVariantGenerator oVariantGen;
	
	public EMutationOperators getType() {
		return this.eMutOp;
	}
	
	//Create the variations
	public LinkedList<String> doVariants(String strInputPhrase)
	{
		LinkedList<String> listRet;
		listRet = null;
		
		if(oVariantGen != null)
		{			
			listRet = oVariantGen.doMutate(strInputPhrase);
		}
		else
			listRet = doVariantsByDefault(strInputPhrase);
			 
		//Delete all the equivalent results
		if(listRet != null)
			listRet.removeAll(Collections.singleton(strInputPhrase));
		
		return listRet;
	}
	public void setVariantGen(IVariantGenerator oVariantGen)
	{
		this.oVariantGen = oVariantGen; 
	}
	
	public int getMaxMutants()
	{
		return nMax;
	}
	public abstract String ToString();
	
	protected abstract LinkedList<String> doVariantsByDefault(String strInputPhrase);
}
