package analyser.flowTree;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.tuple.Pair;

import generator.Action;
import generator.UserInteraction;

/**
 * Intermediate class used to generate test case, receives a list of userInteraction-List<Action> and encapsulate it
 * and provide a set of 
 * @author Pablo C. Ca&ntildeizares
 *
 */
public class TreeBranch {

	ListIterator<TreeInterAction> iterator;
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

	public TreeBranch(TreeInterAction pairIntentAction) {
		branch = new LinkedList<TreeInterAction>();
		branch.add(pairIntentAction);
	}

	public void resetIndex()
	{
		if(branch != null)
			iterator = branch.listIterator();
	}

	public boolean hasNext() {
		
		return iterator != null ? iterator.hasNext() : null;
	}

	public TreeInterAction getNext() {
		return iterator != null ? iterator.next() : null;
	}

	public void addParent(TreeInterAction pairIntentAction) {
		branch.add(0,pairIntentAction);
		
	}
	
}
