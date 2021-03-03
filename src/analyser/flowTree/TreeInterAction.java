package analyser.flowTree;

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
}
