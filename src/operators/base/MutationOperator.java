package operators.base;

import java.util.Collections;
import java.util.LinkedList;

import utteranceVariantCore.IVariantGenerator;

public abstract class MutationOperator {

	protected int nMax;
	protected int nMin;	
	protected boolean bCommandLineOp;
	protected EMutationOperators eMutOp;
	protected IVariantGenerator oVariantGen;
	
	//En principio no nos importa tampoco el tipo
	public EMutationOperators getType() {
		return this.eMutOp;
	}
	
	public LinkedList<String> doVariants(String strInputPhrase)
	{
		LinkedList<String> listRet;
		listRet = null;
		
		if(oVariantGen != null)
		{			
			listRet =oVariantGen.doMutate(strInputPhrase);
		}
		else
			listRet = doVariantsByDefault(strInputPhrase);
			 
		//Delete all the equivalent results
		listRet.removeAll(Collections.singleton(strInputPhrase));
		
		return listRet;
	}
	public void setVariantGen(IVariantGenerator oVariantGen)
	{
		this.oVariantGen = oVariantGen; 
	}
	
	public abstract String ToString();
	
	protected abstract LinkedList<String> doVariantsByDefault(String strInputPhrase);
}
