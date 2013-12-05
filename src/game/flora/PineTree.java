package game.flora;

public class PineTree extends Tree {

	public Tree newInstance(TreeAxis axis) {
		Tree t = new Tree();

		t.addRule(new ChainRule(2,4));
		t.addRule(new ChainRule(3,5));
		t.addRule(new FanRule());
		t.buildBranches();

		return t;
	}

	public class FanRule implements BranchingRule {
		public List<TreeAxis> newAxesHelper(TreeAxis axis) {

		}

		public List<Branch> newBranchesHelper(TreeAxis axis) {

		}
	}

	public class ChainRule implements BranchingRule {
		
		private int lower_bound;
		private int upper_bound;

		public ChainRule(int low, int high) {
			this.lower_bound = low;
			this.upper_bound = high;
		}

		public List<TreeAxis> newAxesHelper(TreeAxis axis) {

		}

		public List<Branch> newBranchesHelper(TreeAxis axis) {

		}
	}
}