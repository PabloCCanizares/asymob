package coverage;

import java.util.LinkedList;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import analyser.EntityAnalyser;
import analyser.IntentAnalyser;
import analyser.TokenAnalyser;
import analyser.flowTree.conversationSplitter.ConversationSplitter;
import analyser.flowTree.conversationSplitter.IntentSplitter;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.Parameter;
import generator.Token;

public class LiteralCoverage implements ICoverageMeter {

	@Override
	public float measureCoverage(Bot botIn, CoverageMethod method) {
		float fCoverage, fValue, fTotal, fCovered, fMaxCoverage;
		int nParams, nEntities, nLiterals, nTokenIndex;
		String tokenValue;
		EList<Intent> intentsList;
		LinkedList<Entity> entityList;
		EList<Parameter> paramList;
		LinkedList<Token> tokenList;
		EntityAnalyser entAnalyser;
		IntentAnalyser intAnalyser;
		TokenAnalyser tokAnalyser;
		Map<String, LinkedList<String>> litMap;
		
		fCovered = fCoverage = fValue = fTotal = 0;
		fCoverage = -1;
		
		entAnalyser = new EntityAnalyser();
		intAnalyser = new IntentAnalyser(); 
		tokAnalyser = new TokenAnalyser();
		
		//Get all intents and entities
		intentsList = (EList<Intent>) botIn.getIntents();
		entityList = new LinkedList<Entity>();
		entityList.addAll(botIn.getEntities());
		
		if(intentsList != null && entityList != null)
		{
			nEntities = entityList.size();
			fCoverage = 0;
			for(Entity entIn: entityList)
			{
				//Extract the map: literal-synonim.
				litMap = entAnalyser.extractEntityMap(entIn);
				
				nLiterals = litMap.size();
				
				fMaxCoverage = -1;
				nTokenIndex = 0;
				fValue = 0;
				//Now, we analyse this entity over all the intents
				for (Intent intentIn: intentsList)
				{
					paramList = intentIn.getParameters();
					
					for(Parameter paramIn: paramList)
					{
						if(isPresent(entIn, paramIn))
						{
							nTokenIndex = 0;
							tokenList =  intAnalyser.searchTokensByParam(intentIn, paramIn);
							while(nTokenIndex < tokenList.size() && litMap.size()>0)
							{
								Token tokIn = tokenList.get(nTokenIndex);
								tokenValue = tokAnalyser.getTokenText(tokIn, false);
								if(isInEntityMap(litMap, tokenValue))
								{
									//remove the entry on the map
									tokenValue = getKey(litMap, tokenValue);
									litMap.remove(tokenValue);
									
									//increase a value
									fValue++;
								}
								nTokenIndex++;
							}
						}
					}
				}
				fTotal+=nLiterals;
				fCovered+=fValue;
				
				fMaxCoverage = fValue/nLiterals;
				System.out.println("* \""+entIn.getName()+"\": "+fMaxCoverage*100+"% ("+(int)fValue+" of "+nLiterals+")");
			}
			fCoverage = 100*(fCovered/fTotal);
			System.out.println("LiteralCoverage: "+fCoverage+"% ("+(int)fCovered+" of "+fTotal+")");
		}
		else
		{
			fValue = -1;
			System.out.println("The loaded chatbot has no intents");
		}

		return fCoverage;
	}

	private boolean isInEntityMap(Map<String, LinkedList<String>> litMap, String tokenValue) {
		boolean bRet;
		LinkedList<String> synonims;
		bRet = false;
		
		if(litMap == null)
			return false;
		
		//Check if the token has the same nameof the entity key
		if(!litMap.containsKey(tokenValue))
		{
			//Otherwise, iterate the lists of the map
			for (Map.Entry<String, LinkedList<String>> set :
				litMap.entrySet()) {
	 
	            synonims = set.getValue();
	            if(synonims.contains(tokenValue))
	            {
		            // Printing all elements of a Map
	            	System.out.println("Literal covered ("+tokenValue+"): "+tokenValue + " = " + set.getValue());
	            	bRet = true;
	            }
	        }
		}
		else
		{
			System.out.println("Literal covered ("+tokenValue+"): "+tokenValue + " = " + litMap.get(tokenValue));
			bRet = true;
		}
		
		return bRet;
	}
	
	private String getKey(Map<String, LinkedList<String>> litMap, String tokenValue) {
		String strRet;
		LinkedList<String> synonims;
		strRet = "";
		
		if(litMap == null)
			return "";
		
		//Check if the token has the same nameof the entity key
		if(!litMap.containsKey(tokenValue))
		{
			//Otherwise, iterate the lists of the map
			for (Map.Entry<String, LinkedList<String>> set :
				litMap.entrySet()) {
	 
	            synonims = set.getValue();
	            if(synonims.contains(tokenValue))
	            {
		            // Printing all elements of a Map
	            	System.out.println("Literal removed ("+set.getKey()+"): "+tokenValue + " = " + set.getValue());
	            	strRet = set.getKey();
	            }
	        }
		}
		else
		{
			System.out.println("Literal removed ("+tokenValue+"): "+tokenValue + " = " + litMap.get(tokenValue));
			strRet = tokenValue;
		}
		
		return strRet;
	}

	private boolean isPresent(Entity entIn, Parameter paramIn) {
		return entIn != null && paramIn != null ? entIn == paramIn.getEntity() : false;
	}
}


/*
//Check how many of them have training phrases
for (Intent intent: intentsList)
{
	intentAggreator = convSplitt.intentAggregator(intent, false);
	
	nParams = intentAggreator.getNumParameters();
	fMaxCoverage = fValue = 0;				
	if(nParams > 0)
	{
		fMaxCoverage = intentAggreator.getMaxParamCoverage();
		
		fValue = nParams * fMaxCoverage;
		fCovered += fValue;
		fTotal += nParams;
	}
	System.out.println("* \""+intent.getName()+"\": "+fMaxCoverage*100+"% ("+(int)fValue+" of "+nParams+")");
}

if(fTotal>0)
	fCoverage = 100*(fCovered / fTotal);
else
	fCoverage = -1;
*/