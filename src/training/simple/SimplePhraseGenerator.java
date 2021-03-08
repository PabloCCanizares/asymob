package training.simple;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import analyser.TokenAnalyser;
import generator.GeneratorFactory;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.Token;
import generator.TrainingPhrase;
import training.PhraseVariation;
import training.PhraseVariationSet;
import training.VariantPhraseGeneratorBase;
import training.VariationsCollectionText;
import training.chaos.TrainingPhraseVarChaosComposed;
import training.chaos.TrainingPhraseVarChaosSingle;
import training.chaos.TrainingPhraseVarChaosTemplate;
import variants.UtteranceVariantCore;
import variants.operators.base.MutationOperatorSet;

public class SimplePhraseGenerator extends VariantPhraseGeneratorBase {


	public SimplePhraseGenerator()
	{
		oMutCore =  new UtteranceVariantCore();
		trainingPhraseSet = new PhraseVariationSet();
		tokenAnalyser = new TokenAnalyser();
	}


	@Override
	protected LinkedList<PhraseVariation> createTrainingPhrase(IntentInput inputIn, MutationOperatorSet cfgIn) {
		TrainingPhrase trainIn;
		List<Token> tokenList;
		LinkedList<PhraseVariation> retList;

		try
		{
			retList = null;

			//Dynamically check if are training phrases [TrainingPhrase] 
			if (inputIn instanceof TrainingPhrase) {
				retList = new LinkedList<PhraseVariation>();
				trainIn = (TrainingPhrase) inputIn;

				tokenList = trainIn.getTokens();

				//This method generates a list of elements, which have a pointer to the input token,
				//and a list of sentences variations in form of strings. 
				retList = generateTrainingPhrase(tokenList, cfgIn); //TODO: Procesar con la lista entera de tokens para contar con un contexto
				int i=1;
				retList = associateTrainingPhrase(retList, trainIn);
			}
		}
		catch(Exception e)
		{
			System.out.println("[SimplePhraseGenerator::createTrainingPhrase] Exception catched while creating traning phrases");
			retList = null;
		}
		
		return retList;
	}

	private LinkedList<PhraseVariation> associateTrainingPhrase(LinkedList<PhraseVariation> listIn,
			TrainingPhrase trainIn) {
		LinkedList<PhraseVariation> retList;
		PhraseVarSimple simple;
		retList = null;
		if(listIn != null)
		{
			retList = new LinkedList<PhraseVariation>();
			for(PhraseVariation pVar: listIn)
			{
				simple = new PhraseVarSimple();
				simple.setMutOp(pVar.getMutOp());
				simple.setPhrase(pVar.getPhrase());
				simple.setTrainingPhrase(trainIn);
				retList.add(simple);
			}
		}
		
		return retList;
	}

	private LinkedList<PhraseVariation> generateTrainingPhrase(List<Token> tokenList, MutationOperatorSet cfgIn) throws Exception {

		String strStringPhrase;
		LinkedList<PhraseVariation> listVariants;
		boolean bRet;

		//Initialise
		listVariants = null;
		
		if(tokenList == null)
			throw new Exception("[SimplePhraseGenerator::generateTrainingPhrase] Empty token list");
		
		//First of all, is is necessary to convert to string the whole phrase
		strStringPhrase = tokenAnalyser.convertListToString(tokenList);
		
		//Provide it to the mutation core
		listVariants = oMutCore.generateVariants(cfgIn, strStringPhrase);	

		return listVariants;		
	}
	
	@Override
	protected TrainingPhrase createPhrase(PhraseVariation tPhraseVar) {
		TrainingPhrase tPhrase, originalPhrase;
		PhraseVarSimple simplePhrase;
		List<Token> tokenList;
		tPhrase =  null;
		//Depending on the type of each trainingPhraseVariation, we must create different types of elements.
		if(tPhraseVar instanceof PhraseVarSimple)
		{
			tPhrase = GeneratorFactory.eINSTANCE.createTrainingPhrase();
			simplePhrase = (PhraseVarSimple)tPhraseVar;
			
			originalPhrase = simplePhrase.getOriginalPhrase();
			tokenList = originalPhrase.getTokens();
			
			if(!isComplexPhrase(tokenList))
			{
				insertSimplePhrase(tPhrase, tPhraseVar);
			}
			else
			{
				insertComplexPhrase(tokenList, tPhrase, tPhraseVar);
			}

			//Check if the original training phrase is simple or complex (Single literal or multiple)
			
			//IF is complex, try to annotate the variation phrase, similarly to the original one.
				        			  
		}
		
		return tPhrase;
		
	}
	//Este metodo trata de re-etiquetar las frases compuestas. Tratamos de hacerlo con coreNLP? Mientras tanto, esbozamos un metodo sencillo
	private void insertComplexPhrase(List<Token> tokenList, TrainingPhrase tPhrase, PhraseVariation tPhraseVar) {
	
		PhraseIntervals phraseIntervals;
		LinkedList<Pair<Integer, Integer>> pairList;
		LinkedList<String> strList;
		String strVariatedPhrase, strIndexString;
		Pair<Integer, Integer> pair;
		int nIndexInit, nIndexEnd, nIndexString;
		Token tokenIndex;
		boolean bFill;
		
		//Initialise
		phraseIntervals = new PhraseIntervals();
		nIndexInit = nIndexEnd = 0;
		bFill = false;
		strVariatedPhrase =  tPhraseVar.getPhrase();
		strList = tokenAnalyser.convertTokenListToStringList(tokenList);
		phraseIntervals.createPairList(strVariatedPhrase);		
		
		for(int i=0;i<strList.size();i++)
		{
			strIndexString = strList.get(i);
			
			nIndexString = strVariatedPhrase.indexOf(strIndexString, nIndexInit);
			
			//Esto quiere decir, que tiene una parte de la frase igual.
			if(nIndexString != -1)
			{
				phraseIntervals.createInterval(nIndexString, nIndexString+strIndexString.length(), i); 
				
				nIndexInit=nIndexString+strIndexString.length()-1;
			}
			else
				//If the token not exists in the new phrase, we have a different variation
				phraseIntervals.createInterval(-1, -1, i); 
		}
		
		for(int i=0;i<tokenList.size();i++)
		{
			pair = phraseIntervals.get(i);
			tokenIndex = tokenList.get(i);
			
			if(pair.getLeft()!=-1 && pair.getRight()!=-1)
			{
				if(bFill)
				{
					addLiteralToken(tPhrase, tPhraseVar, nIndexEnd, pair.getLeft());
				}
				
				nIndexEnd = pair.getRight();
				addToken(tPhrase, tokenIndex);
			}
			else
			{
				//this sentences has not been detected in the new phrase, we include it as Literal
				bFill = true;
			}
		}
	}

	private void addLiteralToken(TrainingPhrase tPhrase, PhraseVariation tPhraseVar, int nInit, int nEnd) {
		String strPartialPhrase;
		Literal lit;
		
		strPartialPhrase = tPhraseVar.getPhrase().substring(nInit, nEnd);
		lit = GeneratorFactory.eINSTANCE.createLiteral();
		lit.setText(strPartialPhrase);
		tPhrase.getTokens().add(lit);
	}

	private boolean isComplexPhrase(List<Token> tokenList) {
		
		return tokenList!= null ? tokenList.size()>1 : false;
	}

	private void insertSimplePhrase(TrainingPhrase tPhrase, PhraseVariation tPhraseVar) {
		PhraseVarSimple varSimple;
		Literal lit;

		varSimple = (PhraseVarSimple) tPhraseVar;
		if(varSimple != null && tPhrase != null)
		{
			lit = GeneratorFactory.eINSTANCE.createLiteral();
			lit.setText(varSimple.getPhrase());
			tPhrase.getTokens().add(lit);
		}
		
	}
	private void addToken(TrainingPhrase tPhrase, Token tokIn)
	{
		Literal lit;
		ParameterReferenceToken parRef;
		if (tokIn instanceof Literal)
		{
			lit = GeneratorFactory.eINSTANCE.createLiteral();
			lit.setText(((Literal) tokIn).getText());
			tPhrase.getTokens().add(lit);
		}
		else if(tokIn instanceof ParameterReferenceToken)
		{
			parRef = GeneratorFactory.eINSTANCE.createParameterReferenceToken();
			parRef.setTextReference(((ParameterReferenceToken) tokIn).getTextReference());
			parRef.setParameter(((ParameterReferenceToken) tokIn).getParameter());
			tPhrase.getTokens().add(parRef);
		}
	}

	@Override
	protected String handleTrainingPhrase(PhraseVariation tPhraseVar) {
		String strRet;
		
		strRet = null;
		//Depending on the type of each trainingPhraseVariation, we must create different types of elements.
		if(tPhraseVar instanceof PhraseVarSimple)
		{
			strRet =((PhraseVarSimple) tPhraseVar).getPhrase();     			  
		}
		
		return strRet;
	}

}
