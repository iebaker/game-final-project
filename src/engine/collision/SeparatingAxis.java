package engine.collision;

import java.util.ArrayList;

import cs195n.Vec2f;

/**
 * Separating Axis class, supporting projection for all shapes but Comps
 * 
 * @author dgattey
 * 
 */
public class SeparatingAxis {
	
	private Vec2f	axis;
	
	/**
	 * Public constructor, taking a direction and finding the perpendicular of that direction to use as an axis
	 * 
	 * @param direction
	 *            a Vec2f representing a direction to find a separating axis for
	 */
	public SeparatingAxis(Vec2f direction) {
		this.axis = new Vec2f(-direction.y, direction.x).normalized();
	}
	
	/**
	 * Public constructor with optional flag for setting the axis directly (use with circles)
	 */
	public SeparatingAxis(Vec2f dir, boolean directSet) {
		this(dir);
		if (directSet) this.axis = dir.normalized();
	}
	
	/**
	 * Returns the normalized axis, or just the axis in this case
	 * 
	 * @return the normalized version of this separating axis
	 */
	public Vec2f getNormalizedAxis() {
		return axis;
	}
	
	/**
	 * General projector for any shape - projects the shape onto the SA
	 * 
	 * @param shape
	 *            the shape to project
	 * @return the range of the projection of shape onto this SA
	 */
	protected Range project(CollisionShape shape) {
		if (shape instanceof Circle)
			return project((Circle) shape);
		else if (shape instanceof AAB)
			return project((AAB) shape);
		else if (shape instanceof Poly) return project((Poly) shape);
		// Should never reach this
		return null;
	}
	
	/**
	 * Projects a circle onto the separating axis and returns its range - does so by projecting the center, then
	 * calculating the min and max along the axis using the radius
	 * 
	 * @param c
	 *            a Circle to project
	 * @return the range of this projection
	 */
	private Range project(Circle c) {
		float proj = c.center.dot(axis.normalized());
		float min = proj - c.radius;
		float max = proj + c.radius;
		return new Range(min, max);
	}
	
	/**
	 * Projects an AAB onto the separating axis and returns its range - does so by finding max and min points and
	 * projecting those
	 * 
	 * @param a
	 *            an AAB to project
	 * @return the range of this projection
	 */
	private Range project(AAB a) {
		ArrayList<Vec2f> points = new ArrayList<Vec2f>();
		points.add(a.min);
		points.add(new Vec2f(a.max.x, a.min.y));
		points.add(a.max);
		points.add(new Vec2f(a.min.x, a.max.y));
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for (Vec2f point : points) {
			float mag = point.dot(axis.normalized());
			if (mag < min) min = mag;
			if (mag > max) max = mag;
		}
		Range rng = new Range(min, max);
		return rng;
	}
	
	/**
	 * Projects a polygon onto the separating axis and returns its range - does so by finding max and min points and
	 * projecting those
	 * 
	 * @param p
	 *            a Polygon to project
	 * @return the range of this projection
	 */
	private Range project(Poly p) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		for (Vec2f point : p.getPoints()) {
			float mag = point.dot(axis);
			if (mag < min) min = mag;
			if (mag > max) max = mag;
		}
		Range rng = new Range(min, max);
		return rng;
	}
	
	/**
	 * String representation for Separating Axes
	 */
	public String toString() {
		return "SeparatingAxis<" + axis + ">";
	}
}
