package analyser.flowTree;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

import generator.Action;
import generator.UserInteraction;

public class TreeBranch {

	ListIterator iterator;
	List<TreeInterAction> branch;
	public TreeBranch(List<Pair<UserInteraction, List<Action>>> flowActionsTemp) {
		branch = new LinkedList<TreeInterAction>();
		
		if(flowActionsTemp != null)
		{
			for(Pair<UserInteraction, List<Action>> interAction: flowActionsTemp)
			{
				if(interAction != null)
					branch.add(new TreeInterAction(interAction.getLeft(), interAction.getRight()));
			}
		}
	}

	public void resetIndex()
	{
		if(branch != null)
			iterator = branch.listIterator();
	}
	
}
