package src.engine.lighting;

import cs195n.Vec2f;

/**
 * This class only exists because Java doesn't have tuples.  It's a 
 * tuple of Vec2f.  Despite the names p1 and p2, there is no explicit
 * ordering over these points.
 */
public class Vec2fPair {
	private Vec2f point1;
	private Vec2f point2;

	/**
	 * Constructor.  
	 *
	 * @param p1 	The first point
	 * @param p2 	The second point
	 */
	public Vec2fPair(Vec2f p1, Vec2f p2) {
		this.point1 = p1;
		this.point2 = p2;
	}

	/**
	 * Accessor for the first point.
	 */
	public Vec2f getP1() {
		return point1;
	}

	/**
	 * Accessor for the second point.
	 */
	public Vec2f getP2() {
		return point2;
	}
}