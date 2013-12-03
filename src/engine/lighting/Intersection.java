package engine.lighting;

import cs195n.Vec2f;

/**
 * A class representing an intersection of a line segment with a ray
 * at a certain location.
 */
public class Intersection {
	private Vec2f point;
	private Segment segment;

	/**
	 * Constructor.  
	 *
	 * @param p 	The point of intersection
	 * @param s 	The segment intersected with;
	 */
	public Intersection(Vec2f p, Segment s) {
		this.point = p;
		this.segment = s;
	}

	/**
	 * Accessor for the point of intersection.
	 * @return 		The point of intersection, as a Vec2f
	 */
	public Vec2f getPoint() {
		return point;
	}


	/** 
	 * Accessor for the segment involved
	 * @return 		The segment involved, as a Vec2f.
	 */
	public Segment getSegment() {
		return segment;
	}

	@Override
	/**
	 * Returns a "useful" String representation of this Intersection object
	 */
	public String toString() {
		return "[engine.lighting.Intersection POINT=" + point + " SEGMENT=" + segment + "]";
	}
}