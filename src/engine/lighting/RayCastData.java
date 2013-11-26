package engine.lighting;

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

	public RayCastData(Vec2f sourcePoint) {
		this.sourcePoint = sourcePoint;
	}

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

	public List<Intersection> getIntersections() {
		return intersections;
	}


	/**
	 * Returns the closest point to the source.
	 */
	public Vec2f minPoint() {
		return intersections.get(0).getPoint();
	}

	public Vec2f findMinPoint() {
		float min_dist = Float.POSITIVE_INFINITY;
		Vec2f minPoint = null;
		for(Intersection i : intersections) {
			float dist = sourcePoint.dist(i.getPoint());
			if(dist < min_dist) {
				min_dist = dist;
				minPoint = i.getPoint();
			}
		}
		return minPoint;
	}

	/**
	 * Returns the closest segment to the source.
	 */
	public Segment minSegment() {
		return intersections.get(0).getSegment();
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
			if(temp.getPoint() == p) {
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
		//System.out.println("[RCD.addIntersection] p is " + p + " and s is " + s);
		//System.out.println("[RCD.addIntersection] Starting size of intersections: " + intersections.size());
		float distance = sourcePoint.dist(p);
		Intersection newInt = new Intersection(p, s);
		int i = 0;

		if(intersections.isEmpty()) {
			intersections.add(newInt);
			return;
		}

		while(i < intersections.size()) {
			Intersection temp = intersections.get(i);
			Vec2f otherPoint = temp.getPoint();
			Segment otherSegment = temp.getSegment();

			float tempDistance = sourcePoint.dist(otherPoint);

			if(tempDistance < distance) {

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
			i++;
		}
		if (i == intersections.size()) {
			intersections.add(newInt);
		}
		//System.out.println("[RCD.addIntersection] Ending size of intersections " + intersections.size());
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
		float term1 = ((b1.y - b2.y)*(a1.x - b1.x) + (b2.x - b1.x)*(a1.y - b1.y));
		float term2 = ((b1.y - b2.y)*(a2.x - b1.x) + (b2.x - b1.x)*(a2.y - b1.y));
		return term1 * term2 < 0;
	}

	@Override
	/**
	 * Returns a useful String representation of this RayCastData object
	 */ 
	public String toString() {
		String result =  "[engine.lighting.RayCastData SOURCE=" + sourcePoint + " ";
		if(intersections.isEmpty()) {
			result += "<NO INTERSECTIONS>]";
			return result;
		}

		result += "INTERSECTIONS=";
		for(Intersection i : intersections) {
			result += i.toString() + " ";
		}

		result += "]";
		return result;
	}
}