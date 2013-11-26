package engine.lighting;

import java.util.ArrayList;
import java.util.List;

import cs195n.Vec2f;

/**
 * A utility object to store the result of a ray cast. Stores a list of Intersection objects, each of which is a tuple
 * of Vec2f and a Segment. The Vec2f represents the point at which the Segment intersects the Ray.
 */
public class RayCastData {
	private final Vec2f					sourcePoint;
	private final List<Intersection>	intersections	= new ArrayList<Intersection>();
	private IntersectionComparator ic;	

	public RayCastData(Vec2f sourcePoint) {
		this.sourcePoint = sourcePoint;
		ic = new IntersectionComparator(this.sourcePoint);
	}
	
	public void addIntersection(Vec2f p, Segment s) {
		Intersection newInt = new Intersection(p, s);

		if(intersections.size() == 0) {
			intersections.add(newInt);
			return;
		}

		int i = 0; 
		while(i < intersections.size()) {
			Intersection existing = intersections.get(i);
			int test = ic.compare(existing, newInt);

			//If the new intersection is further, keep going
			if(test == -1) {
				++i;
			} else {
				intersections.add(i, newInt);
				break;
			}
		}
	}	
	
	public Vec2f findMinPoint() {
		float min_dist = Float.POSITIVE_INFINITY;
		Vec2f minPoint = null;
		for (Intersection i : intersections) {
			float dist = sourcePoint.dist(i.getPoint());
			if (dist < min_dist) {
				min_dist = dist;
				minPoint = i.getPoint();
			}
		}
		return minPoint;
	}
	
	public List<Intersection> getIntersections() {
		return intersections;
	}
	
	/**
	 * Returns a list of the points at which the ray intersected segments. Sorted in order of their distance to the
	 * source point.
	 */
	public List<Vec2f> getPoints() {
		List<Vec2f> points = new ArrayList<Vec2f>();
		for (Intersection i : intersections) {
			points.add(i.getPoint());
		}
		return points;
	}
	
	/**
	 * Returns a list of the segments which were intersected by the ray, sorted by the distance to their intersection
	 * points.
	 */
	public List<Segment> getSegments() {
		List<Segment> segments = new ArrayList<Segment>();
		for (Intersection i : intersections) {
			segments.add(i.getSegment());
		}
		return segments;
	}
	
	/**
	 * Returns the closest point to the source.
	 */
	public Vec2f minPoint() {
		return intersections.get(0).getPoint();
	}
	
	/**
	 * Returns the closest segment to the source.
	 */
	public Segment minSegment() {
		return intersections.get(0).getSegment();
	}
	
	/**
	 * Determines whether two points are on opposite sides of a given line
	 * 
	 * @param a1
	 *            The first point
	 * @param a2
	 *            The second point
	 * @param b1
	 *            The beginning of the line
	 * @param b2
	 *            The end of the line
	 */
	private boolean opposing(Vec2f a1, Vec2f a2, Vec2f b1, Vec2f b2) {
		float term1 = ((b1.y - b2.y) * (a1.x - b1.x) + (b2.x - b1.x) * (a1.y - b1.y));
		float term2 = ((b1.y - b2.y) * (a2.x - b1.x) + (b2.x - b1.x) * (a2.y - b1.y));
		return term1 * term2 < 0;
	}
	
	/**
	 * Removes any intersections associated with a specified point.
	 * 
	 * @param p
	 *            The point to remove
	 */
	public void removePoint(Vec2f p) {
		//System.out.println("[rcd.removePoint] Intersection size before " + intersections.size());
		for (int i = 0; i < intersections.size(); ++i) {
			Intersection temp = intersections.get(i);
			if (temp.getPoint().equals(p)) {
				intersections.remove(i);
			}
		}
		//System.out.println("[rcd.removePoint] Intersection size after " + intersections.size());
	}
	
	/**
	 * Removes any intersections associated with a specified segment from the object
	 * 
	 * @param s
	 *            The segment to remove
	 */
	public void removeSegment(Segment s) {
		//System.out.println("[rcd.removeSegment] Intersection size before " + intersections.size());
		for (int i = 0; i < intersections.size(); ++i) {
			Intersection temp = intersections.get(i);
			if (temp.getSegment() == s) {
				intersections.remove(i);
			}
		}
		//System.out.println("[rcd.removeSegment] Intersection size after " + intersections.size());
	}
	
	@Override
	/**
	 * Returns a useful String representation of this RayCastData object
	 */
	public String toString() {
		String result = "[engine.lighting.RayCastData SOURCE=" + sourcePoint + " ";
		if (intersections.isEmpty()) {
			result += "<NO INTERSECTIONS>]";
			return result;
		}
		
		result += "INTERSECTIONS=";
		for (Intersection i : intersections) {
			result += i.toString() + " ";
		}
		
		result += "]";
		return result;
	}
}
