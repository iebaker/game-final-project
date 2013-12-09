package game.flora;

import java.io.Serializable;

import cs195n.Vec2f;

/**
 * Branch is a class representing a single branch of a fractal tree.
 * A Branch can be modified by a Transformation to produce a new 
 * Branch.
 */
public class Branch implements Serializable {

	private static final long serialVersionUID = -7771979956042799994L;
	private Vec2f startPoint;
	private Vec2f endPoint;
	private Vec2f repVector;

	/**
	 * Constructor. Takes a starting point and ending point, in that order
	 * @param s 	The starting point
	 * @param e 	The ending point
	 */
	public Branch(Vec2f s, Vec2f e) {
		this.startPoint = s;
		this.endPoint = e;	
		this.repVector = e.minus(s);
	}

	/**
	 * Constructor. Takes an already existing branch and constructs a new one
	 * with the same starting and ending points
	 *
	 * @param b  	The branch to copy
	 */
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