package src.engine.lighting;

import java.util.List;
import java.util.ArrayList;
import cs195n.Vec2f;


/**
 * A utility object to store the result of a ray cast.  Stores a list of 
 * Intersection objects, each of which is a tuple of Vec2f and a Segment.
 * The Vec2f represents the point at which the Segment intersects the Ray.
 */
public class RayCastData {
	private Vec2f sourcePoint;
	private List<Intersection> intersections = new ArrayList<Intersection>();

	/** 
	 * Returns a list of the points at which the ray intersected segments.  
	 * Sorted in order of their distance to the source point.
	 */
	public List<Vec2f> getPoints() {
		List<Vec2f> points = new ArrayList<Vec2f>();
		for(Intersection i : intersections) {
			points.add(i.getPoint());
		}
		return points;
	}

	/** 
	 * Returns a list of the segments which were intersected by the ray, sorted
	 * by the distance to their intersection points.
	 */
	public List<Segment> getSegments() {
		List<Segment> segments = new ArrayList<Segment>();
		for(Intersection i : intersections) {
			segments.add(i.getSegment());
		}
		return segments;
	}

	/**
	 * Removes any intersections associated with a specified segment from the
	 * object
	 * 
	 * @param s 	The segment to remove
	 */
	public void removeSegment(Segment s) {
		for(int i = 0; i < intersections.size(); ++i) {
			Intersection temp = intersections.get(i);
			if(temp.getSegment() == s) {
				intersections.remove(temp);
			}
		}
	}

	/**
	 * Removes any intersections associated with a specified point.
	 *
	 * @param p 	The point to remove
	 */
	public void removePoint(Vec2f p) {
		for(int i = 0; i < intersections.size(); ++i) {
			Intersection temp = intersections.get(i);
			if(temp.getPoint == p) {
				intersections.remove(temp);
			}
		}
	}


	/**
	 * Inserts a new Intersection object into the list of intersections
	 * at the proper point based on the location of intersection and
	 * the infrontedness of the segment based on the source point of 
	 * the ray.
	 */
	public void addIntersection(Vec2f p, Segment s) {
		
		float distance = sourcePoint.dist(p);
		Intersection newInt = new Intersection(p, s);
		int i = 0;

		while(true) {
			Intersection temp = intersections.get(i);

			Vec2f otherPoint = temp.getPoint();
			Vec2f otherSegment = temp.getSegment();

			float tempDistance = sourcePoint.dist(otherPoint);

			if(tempDistance < distance) {
				continue;
			} else if(tempDistance == distance) {
				if(otherPoint == otherSegment.getEndPoint()) {
					intersections.add(i, newInt); break;
				} else if(p == s.getEndPoint()) {
					intersections.add(i + 1, newInt); break;
				} else {
					if(opposing(sourcePoint, s.getEndPoint(), otherSegment.getBeginPoint(), otherSegment.getEndPoint())) {
						intersections.add(i + 1, newInt); break;
					} else {
						intersections.add(i, newInt); break;
					}
				}
			} else {
				intersections.add(i, newInt); break;
			}
		}
	}

	/**
	 * Determines whether two points are on opposite sides of a given line
	 * 
	 * @param a1 	The first point
	 * @param a2 	The second point
	 * @param b1 	The beginning of the line
	 * @param b2 	The end of the line
	 */
	private boolean opposing(Vec2f a1, Vec2f a2, Vec2f b1, Vec2f b2) {
		//TODO: Implement opposing (see SO)
	}
}