package game.flora;

import cs195n.Vec2f;

public class Branch {

	private Vec2f startPoint;
	private Vec2f endPoint;

	private Vec2f repVector;

	public Branch(Vec2f s, Vec2f e) {
		this.startPoint = s;
		this.endPoint = e;	

		this.repVector = e.minus(s);
	}

	public Branch(Branch b) {
		this.startPoint = new Vec2f(b.getStartPoint().x, b.getStartPoint().y);
		this.endPoint = new Vec2f(b.getEndPoint().x, b.getEndPoint().y);

		this.repVector = endPoint.minus(startPoint);
	}

	public Vec2f getStartPoint() {
		return startPoint;
	}

	public Vec2f getEndPoint() {
		return endPoint;
	}

	public Branch transform(Transformation t) {
		Branch newBranch = new Branch(this);

		newBranch = newBranch.translate(t.getTrans());
		newBranch = newBranch.rotate(t.getAngle());
		newBranch = newBranch.scale(t.getScale());

		return newBranch;
	}

	private Branch rotate(float theta) {
		float xPrime = (float) (repVector.x * Math.cos(theta) - repVector.y * Math.sin(theta));
		float yPrime = (float) (repVector.x * Math.sin(theta) + repVector.y * Math.cos(theta));

		return new Branch(new Vec2f(startPoint.x, startPoint.y), startPoint.plus(new Vec2f(xPrime, yPrime)));
	}

	private Branch scale(float a) {
		Vec2f unit = repVector.normalized();
		float new_mag = a * repVector.mag();
		unit = unit.smult(new_mag);
		return new Branch(new Vec2f(startPoint.x, startPoint.y), startPoint.plus(unit));
	}

	private Branch translate(float d) {
		Vec2f unit = repVector.normalized();
		unit = unit.smult(d * repVector.mag());
		return new Branch(startPoint.plus(unit), endPoint.plus(unit));
	}
}