package utteranceVariantCore;

import java.util.LinkedList;

import aux.Common;
import training.PhraseVariation;

public class RandomReduce implements IMutantsReduceMethod{

	@Override
	public
	//Tha main idea is to have different methods to reduce this set: random, similarly...
	LinkedList<PhraseVariation> reduceMutants(LinkedList<PhraseVariation> variantTemp, int maxMutants)
	{
		int nDifference;
		
		if(variantTemp != null)
		{
			nDifference = variantTemp.size() - maxMutants;
			
			while(nDifference>0)
			{
				variantTemp.remove(Common.generateRandom(0, variantTemp.size()-1));
				nDifference--;
			}
		}
				
		return variantTemp;
	}

}
