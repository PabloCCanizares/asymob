package utteranceVariantCore;

import java.util.LinkedList;

import aux.Common;
import operators.base.MutationOperator;
import operators.base.MutationOperatorSet;
import training.PhraseVariation;

public class UtteranceVariantCore {

	LinkedList<String> lastMutatedList;
	IMutantsReduceMethod reduceMethod;
	
	public UtteranceVariantCore()
	{
		reduceMethod = new RandomReduce();
	}
	public LinkedList<String> generateVariantsPlain(MutationOperatorSet cfgIn, String strInputPhrase) {
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
	
	//This method has been created to associate each phrase variation to a mutation operator
	public LinkedList<PhraseVariation> generateVariants(MutationOperatorSet cfgIn, String strInputPhrase) {
		MutationOperator mutOp;
		LinkedList<String> listAux;
		LinkedList<PhraseVariation>  variantTemp, variantRet;
		String strMutOp;
		int nIndex;
		
		variantRet = null;
		nIndex = 1;
		
		//Check if the input configuration is correct
		if (cfgIn != null && strInputPhrase != null && !strInputPhrase.isEmpty() && !strInputPhrase.isBlank()) {
			cfgIn.resetIndex();
			System.out.printf("[generateVariants] Phrase: [%s] MutOps[%d] [", strInputPhrase, cfgIn.getActiveOperators());
			//Loop the active mutation operators and apply them to the phrase			
			while (cfgIn.hasNext()) {
			
				mutOp = cfgIn.getNext();
				if (mutOp != null) {		
					
					System.out.printf(" %d |",nIndex);
					
					listAux = mutOp.doVariants(strInputPhrase);
					
					//Convert the plain list into a phrase with info by using PhraseVariation
					variantTemp = convertPlainPhrase(listAux, mutOp);
					
					if(variantTemp != null && mutOp.getMaxMutants()<variantTemp.size())
						variantTemp = reduceMethod.reduceMutants(variantTemp, mutOp.getMaxMutants());
					
					if(variantRet == null)
						variantRet = variantTemp;					
					else if (listAux != null)
						variantRet.addAll(variantTemp);
					
					nIndex++;
				}
			}
			if(variantRet != null)
			{
				System.out.printf(" Variations generated: %d]\n", variantRet.size());
			}
			else
				System.out.printf(" Variations generated: 0]\n");
		}

		return variantRet;
	}

	private LinkedList<PhraseVariation> convertPlainPhrase(LinkedList<String> listAux, MutationOperator mutOp) {
		LinkedList<PhraseVariation> listRet;
		
		listRet = null;
		if(listAux != null)
		{
			listRet = new LinkedList<PhraseVariation>();
			for(String strIn: listAux)
			{
				listRet.add(new PhraseVariation(strIn, mutOp.getType()));
			}
		}
		return listRet;
	}	
}
