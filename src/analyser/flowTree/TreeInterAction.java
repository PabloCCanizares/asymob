package analyser.flowTree;

import java.util.LinkedList;
import java.util.List;


import generator.Action;
import generator.Intent;
import generator.UserInteraction;

public class TreeInterAction {
	private UserInteraction user;
	private List<Action> action;
	
	public TreeInterAction(UserInteraction user, List<Action> action) {
		this.action = action;
		this.user = user;
	}

	public Intent getIntent() {
		Intent intentRet;
		
		intentRet = null;
		if(user != null)
		{
			intentRet = user.getIntent();
		}
		return intentRet;
	}

	public String  getIntentName() {
		String strRet;
		Intent intentRet;
		
		strRet = null;
		intentRet =getIntent();
		if( intentRet!= null)
			strRet = intentRet.getName();
		
		return strRet;
	}

	public LinkedList<String> getActionNames() {
		LinkedList<String> retList;
		
		retList = null;
		if(action != null && action.size()>0)
		{
			retList = new LinkedList<String>();
			for(Action actIndex: action)
			{
				retList.add(actIndex.getName());
			}
		}
		
		return retList;
	}

	public List<Action> getActions() {
		return action;
	}
}
