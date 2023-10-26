package coverage;

import java.util.LinkedList;

import org.eclipse.emf.common.util.EList;
import analyser.IntentAnalyser;
import analyser.flowTree.conversationSplitter.ConversationSplitter;
import analyser.flowTree.conversationSplitter.IntentSplitter;
import generator.Bot;
import generator.Intent;
import generator.Parameter;

public class ParamCoverage implements ICoverageMeter {

	@Override
	public float measureCoverage(Bot botIn, CoverageMethod method) {
		float fCoverage, fValue, fTotal, fCovered, fMaxCoverage;
		int nParams;
		EList<Intent> intentsList;
		ConversationSplitter convSplitt;
		IntentSplitter intentAggreator;
		fCovered = fCoverage = fValue = fTotal = 0;
		
		//Get all intents
		intentsList = (EList<Intent>) botIn.getIntents();
		
		fCoverage = -1;
		if(intentsList != null)
		{
			convSplitt = new ConversationSplitter();
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
			
			System.out.println("ParamCoverage: "+fCoverage+"% ("+(int)fCovered+" of "+fTotal+")");
		}
		else
		{
			fValue = -1;
			System.out.println("The loaded chatbot has no intents");
		}

		return fCoverage;
	}

}
