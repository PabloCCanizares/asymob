package analyser.flowTree.conversationSplitter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import analyser.Conversor;
import analyser.FlowAnalyser;
import analyser.IntentAnalyser;
import analyser.flowTree.TreeInterAction;
import generator.Action;
import generator.Intent;
import generator.Parameter;

public class ActionConversationGroup {
	
	//Analizadores
	FlowAnalyser flowAnalyser;
	IntentAnalyser intentAnalyser;
	//Lista de respuestas
	LinkedList<String> responseList;
	TreeInterAction treeIntentAction;
	Map<String, LinkedList<String>> paramValueMap;	//Map used for rebuild a conversation (with parameter prompts and parameters value)
	LinkedList<String> finishingResponsesList; //List of chatbot responses when the chatbot have all the parameters
	
	
	public ActionConversationGroup(Conversor converter) {
		paramValueMap = new HashMap<String, LinkedList<String>>();
		flowAnalyser = new FlowAnalyser(converter);
		intentAnalyser = new IntentAnalyser(converter);
	}
	
	public boolean extractActions(IntentConversationGroup intentGroup, TreeInterAction treeIntentAction)
	{
		boolean bRet;
		LinkedList<String> auxList, defaultParamValuesList;
		LinkedList<Parameter> paramList;
		Intent intentIn;
		
		bRet = true;
		try
		{
			this.treeIntentAction = treeIntentAction;
			responseList = new LinkedList<String>();
			//If all the parameters are present in the intent, get all the action phrases
			if(intentGroup.areAllParametersPresent())
			{
				//TODO: Separamos esta parte?
				responseList = extractAllPhrases(treeIntentAction);
			}
			else
			{
				//On the contrary, obtain the parameters that are not present in the intent
				intentIn = treeIntentAction.getIntent();
				paramList = intentGroup.getNotPresentParameters();
				
				//And get its associated prompts
				auxList = intentAnalyser.extractIntentParameterPrompts(intentIn, paramList);
				
				//Get all the finishing phrases (to rebuild the conversation in the future)
				finishingResponsesList = extractAllPhrases(treeIntentAction);
				
				//Iterate the non present parameters, and fill the map with all posible values for future tasks ()
				for(Parameter param: paramList)
				{
					
					defaultParamValuesList = intentAnalyser.extractIntentParameterDefaultValues(intentIn, param);
					
					if(defaultParamValuesList != null)
						this.paramValueMap.put(param.getName(), defaultParamValuesList);
				}
				if(auxList != null)
					responseList.addAll(auxList);
			}

		}
		catch(Exception e)
		{
			bRet = false;
		}

		
		return bRet;
	}

	private LinkedList<String> extractAllPhrases(TreeInterAction treeIntentAction) {
		LinkedList<String> auxList, retList;
		
		retList = new LinkedList<String>();
		for(Action action: treeIntentAction.getActions())
		{
			auxList = flowAnalyser.extractAllActionPhrases(action, true);
			
			if(auxList != null)
				retList.addAll(auxList);
		}
		
		return retList;
	}

	public String getActionGroupName() {
		//TODO: Peligrisimo
		return treeIntentAction!=null ? treeIntentAction.getActions().get(0).getName() : "";
	}

	public LinkedList<String> getResponseList() {
		return this.responseList;
	}

	public LinkedList<String> getDefaultResponseForParam(Parameter param) {
		LinkedList<String> retList;
		String strName;
		retList = null;
		
		strName = param.getName();
		if(this.paramValueMap.containsKey(strName))
		{
			retList = paramValueMap.get(strName);
		}
		
		return retList;
	}

	public LinkedList<String> getFinishingResponse() {
		return this.finishingResponsesList;
	}

}
