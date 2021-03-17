package analyser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;

import com.fasterxml.jackson.databind.util.Converter;

import analyser.flowTree.TreeBranch;
import analyser.flowTree.TreeBranchList;
import analyser.flowTree.TreeInterAction;
import generator.Action;
import generator.Bot;
import generator.BotInteraction;
import generator.HTTPRequest;
import generator.HTTPResponse;
import generator.Image;
import generator.Intent;
import generator.IntentInput;
import generator.IntentLanguageInputs;
import generator.Literal;
import generator.ParameterReferenceToken;
import generator.ParameterToken;
import generator.Text;
import generator.TextInput;
import generator.TextLanguageInput;
import generator.Token;
import generator.TrainingPhrase;
import generator.UserInteraction;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
public class BotAnalyser {

	FlowAnalyser flowAnalyser;
	
	public BotAnalyser()
	{
		flowAnalyser = new FlowAnalyser();
	}
	public BotAnalyser(Conversor converter)
	{
		flowAnalyser = new FlowAnalyser(converter);
	}
	private void extractAllIntentInputs(Bot botIn)
	{
		List<Intent> listIntent;
		List<IntentLanguageInputs> listLanguages;
		if(botIn != null)
		{
			//
			listIntent =  botIn.getIntents();
			
			for (Intent intent : listIntent) {
				listLanguages = intent.getInputs();
			}
		}
	}
	
	public LinkedList<String> extractAllIntentPhrases(Intent intentIn)
	{
		LinkedList<String> retList;
		EList<IntentLanguageInputs> listLanguages;
		EList<IntentInput> inputList;
		TrainingPhrase trainIn;
		EList<Token> tokenList;

		//TODO: Refactor y bien
		
		retList = null;
		if(intentIn != null)
		{
			retList = new LinkedList<String>();
			//Analyse the different languages
			listLanguages = intentIn.getInputs();

			for (IntentLanguageInputs intentLan : listLanguages) {

				if(intentLan != null)
				{
					//System.out.println("extractAllPhrases - Analysing intent in language "+intentLan.getLanguage().getLiteral());
					inputList = intentLan.getInputs();

					//Find all the inputs and process them
					if(inputList != null)
					{
						for (IntentInput input : inputList) {
							
							if (input instanceof TrainingPhrase) {
								trainIn = (TrainingPhrase) input;

								if(trainIn != null)
								{
									tokenList = trainIn.getTokens();
									
									String strPhrase;
									
									strPhrase = "";
									for(Token tokIn: tokenList)
									{
										strPhrase+=getTokenText(tokIn);
										
									}
									if(!strPhrase.isEmpty() && !strPhrase.isBlank())
										retList.add(strPhrase);
								}
							}
						}
					}			
				}
			}
		}	
		
		return retList;
	}
	
	private String getTokenText(Token token) {
		String strText;
		Literal litIn;
		ParameterReferenceToken paramRefIn;
		
		//Initially, the returning string is null
		strText = null;
		
		if(token != null)
		{
			if (token instanceof Literal) 
			{
				//process as literal
				litIn = (Literal) token;
				
				if(litIn != null)
					strText = litIn.getText();
			}
			else if(token instanceof ParameterReferenceToken)
			{
				paramRefIn = (ParameterReferenceToken) token;
				
				if(paramRefIn != null)
					strText = 	paramRefIn.getTextReference();
			}
		}

		return strText;
	}

	/*public int analyseNumPaths(UserInteraction userActIn)
	{
		List<Pair<UserInteraction, List<Action>>> treeBranch;
		int nRet;
		
		nRet = 0;
		treeBranch = plainActionTreeInBranches(userActIn);
		
		if(treeBranch != null)
			nRet = treeBranch.size();
		return nRet;
	}*/
	public int analyseNumPaths(UserInteraction userActIn)
	{
		BotInteraction botInteraction;
		EList<UserInteraction> userActionList;
		int nRet, nSize;
		
		nRet = 0;
		if(userActIn !=null)
		{
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				userActionList = botInteraction.getOutcoming();
				if(userActionList != null)
				{
					nSize = 0;
					
					if(userActionList.size()>0)
						nRet = userActionList.size()-1;
					
					//Here we have another intent with a action list
					for(UserInteraction userAct: userActionList)
					{
						nSize += analyseNumPaths(userAct);
					}
					nRet += (nSize);
				}
			}
		}
		return nRet;
	}
	//TODO: Ask for the convention of counting lenght (nodes or edges)
	public int analyseMaxLenght(UserInteraction userActIn)
	{
		BotInteraction botInteraction;
		EList<UserInteraction> userActionList;
		int nRet, nSize, nSizeAux;
		
		nRet = 0;
		if(userActIn !=null)
		{
			nRet = 1;
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				userActionList = botInteraction.getOutcoming();
				if(botInteraction.getOutcoming() != null && botInteraction.getOutcoming().size()>0)
				{
					nSizeAux = nSize = 0;
					//Here we have another intent with a action list
					for(UserInteraction userAct: userActionList)
					{
						nSizeAux = analyseMaxLenght(userAct);
						
						nSize = Math.max(nSize, nSizeAux);
					}
					nRet += nSize;
				}
			}
		}
		return nRet;
	}
	
	public List<Pair<UserInteraction, List<Action>>> plainActionTreeInBranches(UserInteraction userActIn) {
	
		List<Pair<UserInteraction, List<Action>>> combinedList, partialList;
		List<Action> actionList;
		BotInteraction botInteraction;
		Pair<UserInteraction, List<Action>> pairIntentAction;
		EList<UserInteraction> userActionList;
		
		combinedList = null;
		actionList = null;
		if(userActIn !=null)
		{
			combinedList = new LinkedList<Pair<UserInteraction,List<Action>>>();
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				actionList = botInteraction.getActions();
				pairIntentAction = Pair.of(userActIn, actionList);
				combinedList.add(pairIntentAction);
			}
			userActionList = botInteraction.getOutcoming();
			if(botInteraction.getOutcoming() != null)
			{
				//Here we have another intent with a action list
				for(UserInteraction userAct: userActionList)
				{
					partialList = plainActionTreeInBranches(userAct);
					combinedList.addAll(partialList);
				}
			}
				
		}
		
		return combinedList;
	}
	public List<Pair<UserInteraction, Action>> plainActionTree(UserInteraction userActIn) {
		List<Pair<UserInteraction, Action>> combinedList, partialList;
		List<Action> actionList;
		BotInteraction botInteraction;
		Pair<UserInteraction, Action> pairIntentAction;
		EList<UserInteraction> userActionList;
		
		combinedList = null;
		actionList = null;
		if(userActIn !=null)
		{
			combinedList = new LinkedList<Pair<UserInteraction,Action>>();
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
			{
				actionList = botInteraction.getActions();
				for(Action action : actionList)
				{
					pairIntentAction = Pair.of(userActIn, action);
					combinedList.add(pairIntentAction);
				}
				
			}
			userActionList = botInteraction.getOutcoming();
			if(botInteraction.getOutcoming() != null)
			{
				//Here we have another intent with a action list
				for(UserInteraction userAct: userActionList)
				{
					partialList = plainActionTree(userAct);
					combinedList.addAll(partialList);
				}
			}
				
		}
		
		return combinedList;
	}
	/*public List<TreeBranch> plainActionTreeList(UserInteraction userActIn, TreeBranchList listIn) {
		TreeBranchList combinedList, partialList;
		List<Action> actionList;
		BotInteraction botInteraction;
		TreeInterAction pairIntentAction;
		EList<UserInteraction> userActionList;
		
		combinedList = null;
		actionList = null;
		try
		{
			combinedList = new TreeBranchList();
			botInteraction = userActIn.getTarget();
			actionList = botInteraction.getActions();
			if(actionList != null)
			{
				//combinedList = (TreeBranchList) SerializationUtils.clone((T) listIn); 
				//listIn.insertIntentActionList(userActIn, actionList);				
			}

			userActionList = botInteraction.getOutcoming();
			if(botInteraction.getOutcoming() != null)
			{
				//Here we have another intent with a action list
				for(UserInteraction userAct: userActionList)
				{
					partialList = plainActionTreeList(userAct);
					combinedList.addAll(partialList);
				}
			}
		}catch(Exception e)
		{
			System.out.println("[plainActionTreeList] Exception plaining tree");
		}
		
		return combinedList;
	}*/
	public EList<Action> extractActionList(UserInteraction userActIn) {
		EList<Action> retList;
		BotInteraction botInteraction;
		
		retList = null;
		if(userActIn !=null)
		{
			botInteraction = userActIn.getTarget();
			
			if(botInteraction != null)
				retList = botInteraction.getActions();
			
			if(botInteraction.getOutcoming() != null)
			{
				//Here we have another intent with a action list
			}
				
		}
		return retList;
	}

	public LinkedList<String> extractAllActionPhrases(Action actIndex) {
		return flowAnalyser != null ? flowAnalyser.extractAllActionPhrases(actIndex, false) : null;
	}
	public List<String> extractAllActionPhrasesByRef(Action action) {
		// TODO Auto-generated method stub
		return flowAnalyser != null ? flowAnalyser.extractAllActionPhrases(action, true) : null;
	}
}
