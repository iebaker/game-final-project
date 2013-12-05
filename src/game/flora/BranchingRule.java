package game.flora;

import java.util.List;
import java.util.ArrayList;

public abstract class BranchingRule {

	public List<TreeAxis> getNewAxes(List<TreeAxis> axes) {
		List<TreeAxis> ret_val = new ArrayList<TreeAxis>();
		for(TreeAxis axis : axes) {
			ret_val.addAll(newAxesHelper(axis));
		}
		return ret_val;
	}

	public List<Branch> getNewBranches(List<TreeAxis> axes) {
		List<Branch> ret_val = new ArrayList<TreeAxis>();
		for(TreeAxis axis : axes) {
			ret_val.addAll(newBranchesHelper(axis));
		}
		return ret_val;
	}

	public List<TreeAxis> newAxesHelper(TreeAxis axis);
	public List<Branch> newBranchesHelper(TreeAxis axis);
}

