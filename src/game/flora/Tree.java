package game.flora;

import java.util.List;
import java.util.ArrayList;

public abstract class Tree {

	private List<BranchingRule> my_rules = new ArrayList<BranchingRule>();
	private List<Branch> my_branches = new ArrayList<Branch>();

	public abstract Tree newInstance(TreeAxis axis);
	
	protected void buildBranches(TreeAxis axis) {
		List<TreeAxis> axes = new ArrayList<TreeAxis>();

		axes.add(axis);

		for(BranchingRule rule : this.my_rules) {
			axes = rule.getNewAxes(axes);
			my_branches.addAll(rule.getNewBranches(axes));
		}
	}

	protected void addRule(BranchingRule rule) {
		my_rules.add(rule);
	}

}