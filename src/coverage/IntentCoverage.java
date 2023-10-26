package coverage;

import java.util.LinkedList;

import org.eclipse.emf.common.util.EList;


import analyser.IntentAnalyser;
import generator.Bot;
import generator.Intent;

public class IntentCoverage implements ICoverageMeter {

	@Override
	public float measureCoverage(Bot botIn, CoverageMethod method) {
		float fCoverage, fValue, fTotal;
		int nTrainingPhrases;
		EList<Intent> intentsList;
		IntentAnalyser analyser;
		
		fCoverage = fValue = 0;
		nTrainingPhrases = 0;
		analyser = new IntentAnalyser();
		
		//Get all intents
		intentsList = (EList<Intent>) botIn.getIntents();
		
		if(intentsList != null)
		{
			fTotal = intentsList.size();
			//Check how many of them have training phrases
			for (Intent intent: intentsList)
			{
				if(!intent.isFallbackIntent())
				{
					nTrainingPhrases = analyser.getTotalPhrases(intent);
					if(nTrainingPhrases>0)
						fValue++;
				}
				else
					fTotal--;
			}
			fCoverage = 100*(fValue / fTotal);
			System.out.println("IntentCoverage: "+fCoverage+"% ("+(int)fValue+" of "+fTotal+")");
		}
		else
		{
			fValue = -1;
			System.out.println("The loaded chatbot has no intents");
		}

		return fCoverage;
	}

}
