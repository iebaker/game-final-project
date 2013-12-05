package game.flora;

public class TreeAxis {

	private Vec2f root;
	private Vec2f axis;

	public TreeAxis(Vec2f r, Vec2f a) {
		this.root = r;
		this.axis = a;
	}

	public Vec2f getRoot() {
		return root;
	}

	public Vec2f getAxis() {
		return axis;
	}
}