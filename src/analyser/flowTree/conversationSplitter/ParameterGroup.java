package analyser.flowTree.conversationSplitter;

import java.util.LinkedList;

import generator.Parameter;

public class ParameterGroup {

	private String strCode;
	private LinkedList<Parameter> paramList;
	private LinkedList<Boolean> paramActiveList;
	
	public ParameterGroup(String strCode, LinkedList<Parameter> paramList)
	{
		this.strCode = strCode;
		this.paramList = paramList;
		paramActiveList = new LinkedList<Boolean>();
		initialiseParamList();
	}
	
	private void initialiseParamList()
	{
		int nLen;
		
		try
		{
			nLen = strCode.length();
			for(int i=0;i<nLen;i++)
			{
				if(strCode.charAt(i) == '0')
					paramActiveList.addLast(false);
				else
					paramActiveList.addLast(true);				
					
			}
		}catch(Exception e)
		{
			System.out.println("ParameterGroup - Exception while initialising the class");
		}
	}
	/**
	 * Analyse if all the parameters satisfy the provided condition
	 * @param bCondition
	 * @return
	 */
	public boolean checkAllParameters(boolean bCondition)
	{
		int nIndex;
		boolean bFound;
		
		nIndex=0;
		bFound = true;
		
		if(paramActiveList != null)
		{
			while(bFound && nIndex<paramActiveList.size())
			{
				if(paramActiveList.get(nIndex) != bCondition)
					bFound = false;
				
				nIndex++;
			}
		}
		
		return bFound;
	}
	
	/**
	 * Return all the parameters not present in a phrase
	 * @return
	 */
	public LinkedList<Parameter> getParametersNotPresent()
	{
		LinkedList<Parameter> paramRet;
		int nIndex;
		
		nIndex=0;
		paramRet = null;
		
		// Check the sizes
		if(paramList != null && paramActiveList != null && paramList.size() == paramActiveList.size())
		{
			paramRet = new LinkedList<Parameter>();
			for(boolean bPresent: paramActiveList)
			{
				if(!bPresent)
					paramRet.add(paramList.get(nIndex));
					
				nIndex++;
			}
		}
		
		return paramRet;
		
	}

	public String getCode() {
		return this.strCode;
	}

}
