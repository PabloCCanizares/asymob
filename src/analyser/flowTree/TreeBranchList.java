package analyser.flowTree;

import java.util.LinkedList;
import java.util.List;

public class TreeBranchList {

	List<TreeBranch> branchList;
	
	public TreeBranchList ()
	{
		branchList = new LinkedList<TreeBranch>();
	}

	public void add(TreeBranch treeBranch) {
		if(treeBranch != null)
			branchList.add(treeBranch);
	}
	
}
