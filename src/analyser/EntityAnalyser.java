package analyser;

import generator.Entity;
import generator.EntityInput;
import generator.LanguageInput;
import generator.SimpleInput;

public class EntityAnalyser {

	public int analyseNumLiterals(Entity entityIn)
	{
		int nRet;
		
		nRet = 0;
		
		try
		{
			for(LanguageInput lan: entityIn.getInputs())
			{
				for(EntityInput enIn: lan.getInputs())
				{
					if(enIn instanceof SimpleInput)
					{
						nRet ++;
					}
				}
			}
		}
		catch(Exception e)
		{
			nRet = 0;
		}
		
		return nRet;
	}
}
