package utteranceVariantCore;

import java.util.LinkedList;

import operators.base.MutationOperator;
import operators.base.MutationOperatorSet;

public class UtteranceVariantCore {

	LinkedList<String> lastMutatedList;

	public LinkedList<String> generateVariants(MutationOperatorSet cfgIn, String strInputPhrase) {
		MutationOperator mutOp;
		LinkedList<String> listRet, listAux;

		listRet = null;
		
		//Check if the input configuration is correct
		if (cfgIn != null && strInputPhrase != null && !strInputPhrase.isEmpty() && !strInputPhrase.isBlank()) {
			cfgIn.resetIndex();
			
			//Loop the active mutation operators and apply them to the phrase			
			while (cfgIn.hasNext()) {
				mutOp = cfgIn.getNext();
				if (mutOp != null) {				
					listAux = mutOp.doVariants(strInputPhrase);
					
					if(listRet == null)
						listRet = listAux;					
					else if (listAux != null)
						listRet.addAll(listAux);
				}
			}
		}

		return listRet;
	}

}
