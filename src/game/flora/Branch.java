package game.flora;

public class Branch {
	private Vec2f source;
	private Vec2f branchVector;

	public Branch(Vec2f s, Vec2f bv) {
		this.source = s;
		this.branchVector = bv;
	}

	public Vec2f getSource() {
		return source;
	}

	public Vec2f getBranchVector() {
		return branchVector;
	}
}