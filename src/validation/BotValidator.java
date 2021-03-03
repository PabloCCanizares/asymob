package validation;

import java.util.LinkedList;

import generator.Bot;

public abstract class BotValidator {

	LinkedList<IBotValidationMethod> methodList;
	
	protected BotValidator()
	{
		methodList = new LinkedList<IBotValidationMethod>();
	}
	
	boolean doValidate(Bot botIn)
	{
		boolean bRet;
		
		bRet = false;
		
		try
		{
			for(IBotValidationMethod valMethod: methodList)
			{
				if(valMethod != null)
					bRet = valMethod.doValidate(botIn);
			}
		}catch(Exception e)
		{
			
		}
		return bRet;
	}
}
