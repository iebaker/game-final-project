package engine.collision;

import java.util.ArrayList;

import cs195n.Vec2f;

/**
 * Class supporting rays with direction and source that can cast, collide, and find intersections with shapes, edges,
 * and points
 * 
 * @author dgattey
 * 
 */
public class Ray {
	
	private final Vec2f	source;
	private final Vec2f	direction;
	
	/**
	 * Public constructor with source and destination
	 * 
	 * @param source
	 * @param destination
	 */
	public Ray(Vec2f source, Vec2f dest) {
		this.source = source;
		this.direction = (dest.minus(source)).normalized();
	}
	
	/**
	 * Gets point along the ray given a distance
	 * 
	 * @param dist
	 *            the distance along the ray from source
	 * @return the point on the ray
	 */
	public Vec2f findPoint(float dist) {
		return (source.plus(direction.smult(dist)));
	}
	
	/**
	 * Casts the ray onto the shape with double dispatch
	 * 
	 * @param a
	 *            the shape to cast onto
	 * @return the point of collision for a cast (or null for no cast)
	 */
	public Vec2f cast(CollisionShape a) {
		return a.cast(this);
	}
	
	/**
	 * Public getter for source
	 * 
	 * @return source of the ray
	 */
	public Vec2f getSource() {
		return source;
	}
	
	/**
	 * Public getter for direction
	 * 
	 * @return direction of the ray
	 */
	public Vec2f getDirection() {
		return direction;
	}
	
	/**
	 * Returns if this ray collides with the edge passed in
	 * 
	 * @param a
	 *            a start point for the edge
	 * @param b
	 *            an end point for the edge
	 * @return If the ray straddles the edge or not
	 */
	private boolean straddlesEdge(Vec2f a, Vec2f b) {
		return !(a.minus(source).cross(direction) * b.minus(source).cross(direction) > 0);
	}
	
	/**
	 * Given that the ray collides with the edge passed in, gives the t value for dist between source and edge
	 * 
	 * @param a
	 *            a start point for the edge
	 * @param b
	 *            an end point for the edge
	 * @return The float representing dist from the source to the edge where it collides
	 */
	private float collideEdge(Vec2f a, Vec2f b) {
		Vec2f n = (a.minus(b)).normalized().perpendicular();
		return (b.minus(source).dot(n)) / (direction.dot(n));
	}
	
	/**
	 * Given an array of points, finds closest point to ray on edge
	 * 
	 * @param pts
	 *            the points to check
	 * @return The point at which the ray intersects the edge, closest to the edge of the shape
	 */
	public Vec2f getIntersection(ArrayList<Vec2f> pts) {
		int s = pts.size();
		Vec2f closest = null;
		float t = Float.POSITIVE_INFINITY;
		for (int i = 0; i < s; i++) {
			Vec2f a = pts.get(i % s);
			Vec2f b = pts.get((i + 1) % s);
			
			// If the ray crosses the edge, see if the t is less than the current t
			if (straddlesEdge(a, b)) {
				float tmpt = collideEdge(a, b);
				if (tmpt < t && tmpt >= 0) {
					t = tmpt;
					Vec2f tmp = source.plus(direction.smult(t));
					if (tmp != null) closest = tmp;
				}
			}
		}
		return closest;
	}
	
	/**
	 * Public toString method to make it easier to test
	 */
	public String toString() {
		return "Ray<src:" + source + " dir:" + direction + ">";
	}
	
}