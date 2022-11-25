package analyser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import generator.Bot;
import generator.Element;
import generator.Entity;
import generator.EntityInput;
import generator.Intent;
import generator.LanguageInput;
import generator.SimpleInput;

public class EntityAnalyser {

	public EntityAnalyser() {
	}
	public EntityAnalyser(Conversor converter) {
		// TODO Auto-generated constructor stub
	}

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
	
	Map<String, LinkedList<String>> extractEntityMap(Entity entityIn) {
		
		Map<String, LinkedList<String>> mapRet;
		LinkedList<String> listSyn;
		mapRet =  new HashMap<String, LinkedList<String>>();;
		for(LanguageInput lan: entityIn.getInputs())
		{
			for(EntityInput enIn: lan.getInputs())
			{
				
				if(enIn instanceof SimpleInput)
				{
					listSyn = new LinkedList<String>();
					listSyn.addAll(((SimpleInput) enIn).getValues());
					
					mapRet.put(((Element) enIn).getName(),listSyn);
				}
			}
		}
		return mapRet;
	}
}
