package main;

import java.util.Iterator;
import java.util.LinkedList;

import operators.base.MutationOperator;

public class MutOpsCfg {

	private static MutOpsCfg mutOps=null;

	//HashMap<EMutationOperators, MutationOperator> operatorHash;
	//Iterator<Entry<EMutationOperators, MutationOperator>> itHashMap;
	LinkedList<MutationOperator> operatorList;
	Iterator<MutationOperator> itList;
	private MutOpsCfg()
	{
		//operatorHash = new HashMap<EMutationOperators, MutationOperator>();
		operatorList = new LinkedList<MutationOperator>();
		//Initialises the hasmap with null for all the operators
		//It is performed for using isMutOpActive function.
		
		//TODO: Peligroso again
		/*for (EMutationOperators mutOp : EMutationOperators.values()) { 
			//operatorHash.put(mutOp, null);
		}*/
		
	}
	public void insertOperator(MutationOperator opIn)
	{
		operatorList.add(opIn);
		//operatorHash.put(opIn.getType(), opIn);
	}
	//TODO: Esto es peligroso, porque los operadores que requieren cfg, e.g. paths
	//no la van a tener
/*	public void activateAllOperatorsByDefault()
	{
		operatorHash.put(EMutationOperators.EMutateUtterance, new MutateUtteranceOp());
		
		//TODO: Completar el resto de operadores
	}*/
	public  static MutOpsCfg getInstance() {

		if (mutOps==null) {

			mutOps=new MutOpsCfg();
		}
		return mutOps;
	}	
	
	/*public boolean isMutOpActive(EMutationOperators mutOpIn)
	{
		boolean bRet;
		MutationOperator mutRet;
		
		bRet = false;
		if(mutOpIn != null)
		{
			mutRet = operatorHash.get(mutOpIn);
			
			if(mutRet != null)
				bRet = true;
		}
		return bRet;
	}*/
	public void resetIndex() {
		//itHashMap = operatorHash.entrySet().iterator();
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
