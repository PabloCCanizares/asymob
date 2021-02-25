package analyser.flowTree;

import java.util.List;


import generator.Action;
import generator.UserInteraction;

public class TreeInterAction {
	private UserInteraction user;
	private Action action;
	
	public TreeInterAction(UserInteraction user, Action action) {
		this.action = action;
		this.user = user;
	}
}
