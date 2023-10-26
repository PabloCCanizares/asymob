package coverage;

import java.util.LinkedList;

import org.eclipse.emf.common.util.EList;

import analyser.IntentAnalyser;
import generator.Bot;
import generator.Entity;
import generator.Intent;
import generator.Parameter;

public class EntityCoverage implements ICoverageMeter{

	@Override
	public float measureCoverage(Bot botIn, CoverageMethod method) {
		float fCoverage, fTotal;
		int nIndex;
		boolean bFound;
		EList<Intent> intentsList;
		LinkedList<Entity> entityList;
		LinkedList<Entity> coveredEntities;
		LinkedList<Parameter> paramList;
		IntentAnalyser analyser;
		Entity entIn;
		
		fCoverage = fTotal = 0;
		
		//Get all intents
		intentsList = (EList<Intent>) botIn.getIntents();
		entityList = new LinkedList<Entity>();
		entityList.addAll(botIn.getEntities());
		fCoverage = -1;
		if(intentsList != null)
		{
			analyser = new IntentAnalyser();
			coveredEntities = new LinkedList<Entity>();
			fTotal = entityList.size();
			
			//Check how many of them have training phrases
			for (Intent intent: intentsList)
			{
				paramList = analyser.getParameters(intent);
				
				if(paramList != null && paramList.size()>0)
				{
					nIndex=0;
					//Search in the entityList if the some parameter cover any entity
					while(nIndex < entityList.size())
					{
						bFound=false;
						
						entIn = entityList.get(nIndex);
						bFound = searchEntity(paramList, entIn);
						
						if(bFound)
						{
							entityList.remove(nIndex);
							coveredEntities.add(entIn);
						}
						else
							nIndex++;
					}
				}
			}
			System.out.println("Covered entities:");
			for (Entity ent: coveredEntities)
			{
				System.out.println("* "+ent.getName());
			}
			
			System.out.println("Uncovered entities:");
			for (Entity ent: entityList)
			{
				System.out.println("* "+ent.getName());
			}
			//Now we have only the uncovered entities
			if(fTotal>0)
				fCoverage = 100*(coveredEntities.size() / fTotal);
			else
				fCoverage = -1;
			
		}
		else
		{
			System.out.println("The loaded chatbot has no intents");
		}

		return fCoverage;
	}

	private boolean searchEntity(LinkedList<Parameter> paramList, Entity entIn) {
		Entity paramEnt;
		boolean bFound;
		int nIndexParam;
		
		bFound = false;
		nIndexParam = 0;
		while(!bFound && nIndexParam < paramList.size())
		{
			Parameter param = paramList.get(nIndexParam);
			
			paramEnt = param.getEntity();
			if(paramEnt != null)
				if(entIn == paramEnt)
					bFound = true;
			
			nIndexParam++;
		}
		return bFound;
	}


}
