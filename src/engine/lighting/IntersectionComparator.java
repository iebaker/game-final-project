package engine.lighting;

import java.util.Comparator;
import cs195n.Vec2f;

/**
 * IntersectionComparator is a class which can compare two intersection objects which represent
 * the intersection between a ray cast and a segment from the world.  The comparator sorts by
 * distance to the source point -- ties broken in favor of the segment whose angle
 * with the ray is greater (i.e. which one "points" more towards the source point)
 *
 * @author iebaker
 */
public class IntersectionComparator implements Comparator<Intersection> {

	private Vec2f sourcePoint;	

	/**
	 * Constructor.  Needs knowledge of the source point in order to do distance calculations
	 * @param sourcePoint 	The source of the ray cast that led to the intersections being compared.
	 */
	public IntersectionComparator(Vec2f sourcePoint) {
		this.sourcePoint = sourcePoint;
	}
	
	@Override
	/**
	 * Compares two Intersections using a metric of distance and angle with respect to the source
	 * point and ray.  Prefers shorter distances, and greater angles.  Likes long walks on the beach.
	 *
	 * @param i1 	The first intersection (order doesn't matter)
	 * @param i2 	The second intersection
	 * @return		-1 if i1 is closer, 1 if i2 is closer
	 */
	public int compare(Intersection i1, Intersection i2) {

		// Capture points and segments from the intersection objects
		Vec2f p1 = i1.getPoint(); Segment s1 = i1.getSegment();
		Vec2f p2 = i2.getPoint(); Segment s2 = i2.getSegment();

		// Calculate distances to source point
		float dist1 = sourcePoint.dist(p1);
		float dist2 = sourcePoint.dist(p2);

		if(dist1 < dist2) {

			// The first intersection is closer
			return -1;		

		} else if(dist1 > dist2) {

			// The second intersection is closer
			return 1;	

		} else {

			if(p1.equals(s1.getEndPoint())) {
				// The first intersection is at an endpoint for its segment, so the second is closer
				return 1; 		
			}

			if(p2.equals(s2.getEndPoint())) {
				// The second intersection is at an end point for its segment, so the first is closer.
				return -1;		
			}

			// At this point, we know neither are endpoints.

			// Calculate the vector representing the ray's direction (Ray should have getDirection.... does it?)
			Vec2f rayVector = p1.minus(sourcePoint);

			// Convert s1, s2 to vectors.  
			Vec2f v1 = s1.asVector();
			Vec2f v2 = s2.asVector();

			// Calculate angles between s1Vector and s2Vector and the ray vector.
			float theta1 = this.angleBetween(rayVector, v1);
			float theta2 = this.angleBetween(rayVector, v2);

			if(theta1 > theta2) {
				// The first intersection is closer, because its angle from the ray is bigger
				return -1;
			} else {
				// The second intersection is closer because its angle from the ray is bigger
				return 1;
			}
		}
	}

	/**
	 * Computes the angle between two vectors.  Why the hell doesn't Vec2f do this?
	 *
	 * @param v  The first vector (order doesn't matter)
	 * @param w  The second vector
	 * @return 	The angle between the vectors, as a float (range 0-180)
	 */
	public float angleBetween(Vec2f v, Vec2f w) {
		float v_dot_w = v.dot(w);
		float mag_v = v.mag();
		float mag_w = w.mag();

		return (float) Math.acos(v_dot_w/(mag_v * mag_w));
	}
}
