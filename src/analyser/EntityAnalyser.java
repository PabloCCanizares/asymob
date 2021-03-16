package analyser;

import org.eclipse.emf.common.util.EList;

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
						nRet++;
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

	public int analyseNumSynonyms(SimpleInput enIn) {
		int nRet;
		EList<String> retList;
		
		nRet = 0;
		if(enIn != null)
		{
			retList = enIn.getValues();
			
			if(retList != null)
				nRet = retList.size();
		}
		return nRet;
	}
}
