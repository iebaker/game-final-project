package engine.lighting;

import cs195n.Vec2f;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Segment is a class representing an ordered tuple of two points which defines
 * a single line segment making up the LightWorld
 */
public class Segment {
	private Vec2f beginPoint = null;
	private Vec2f endPoint = null;
	private boolean ignored = false;
	private boolean flipped = false;

	private static Map<Vec2f, List<Segment>> byEndpoints = new HashMap<Vec2f, List<Segment>>();
	private static Map<Vec2f, List<Segment>> byBeginpoints = new HashMap<Vec2f, List<Segment>>();
	
	public static void clear() {
		byEndpoints = new HashMap<Vec2f, List<Segment>>();
		byBeginpoints = new HashMap<Vec2f, List<Segment>>();
	}

	public static List<Segment> beginningAt(Vec2f v) {
		if(byBeginpoints.containsKey(v)) {
			return byBeginpoints.get(v);
		} 
		return new ArrayList<Segment>();
	}

	public static List<Segment> endingAt(Vec2f v) {
		if(byEndpoints.containsKey(v)) {
			return byEndpoints.get(v);
		}
		return new ArrayList<Segment>();
	}

	public static void ignoreEndingAt(Vec2f v) {
		for(Segment s : Segment.endingAt(v)) {
			s.ignore();
		}
	}

	public static void ignoreBeginningAt(Vec2f v) {
		for(Segment s : Segment.beginningAt(v)) {
			s.ignore();
		}
	}

	public static void attendEndingAt(Vec2f v) {
		for(Segment s : Segment.endingAt(v)) {
			s.attend();
		}
	}

	public static void attendBeginningAt(Vec2f v) {
		for(Segment s : Segment.beginningAt(v)) {
			s.attend();
		}
	}

	public void ignore() {
		this.ignored = true;
	}

	public void attend() {
		this.ignored = false;
	}

	public void flip() {
		this.flipped = true;
	}

	public boolean isFlipped() {
		return this.flipped;
	}

	public boolean isIgnored() {
		return ignored;
	}

	public boolean endsAt(Vec2f v) {
		return v.equals(this.endPoint);
	}

	/**
	 * Setter method for the beginning point of the segment.  If the beginPoint
	 * is already set, nothing is changed.
	 *
	 * @param p 	The point to set the beginning point to.
	 */
	public void setBeginPoint(Vec2f p) {
		if(beginPoint == null) {
			beginPoint = p;
			p.setStart(true);
			if(!byBeginpoints.containsKey(p)) {
				byBeginpoints.put(p, new ArrayList<Segment>());
			}
			byBeginpoints.get(p).add(this);
		}
	}

	/**
	 * Setter method for the ending point of this segment.  If the endPoint is
	 * already set, nothing is changed.
	 *
	 * @param p 	The point to set the ending point to
	 */
	public void setEndPoint(Vec2f p) {
		if(endPoint == null) {
			endPoint = p;
			p.setEnd(true);
			if(!byEndpoints.containsKey(p)) {
				byEndpoints.put(p, new ArrayList<Segment>());
			}
			byEndpoints.get(p).add(this);
		}
	}

	/**
	 * Resetter method for the ending point of this segment.
	 */
	public void resetBeginPoint(Vec2f p) {
		beginPoint = p;
	}

	/**
	 * Accessor method for the beginning point of the segment
	 *
	 * @return 		The beginning point of the segment
	 */
	public Vec2f getBeginPoint() {
		return beginPoint;
	}

	public Vec2f getEndPointForRayCast() {
		Vec2f toEndPoint = endPoint.minus(beginPoint);
		if(toEndPoint.isZero()) return endPoint;

		toEndPoint = toEndPoint.normalized().smult(5);
		return endPoint.plus(toEndPoint);
	}

	public Vec2f getBeginPointForRayCast() {
		Vec2f toBeginPoint = beginPoint.minus(endPoint);
		if(toBeginPoint.isZero()) return beginPoint;

		toBeginPoint = toBeginPoint.normalized().smult(5);
		return beginPoint.plus(toBeginPoint);
	}


	/**
	 * Accessor method for the ending point of the segment.
	 *
	 * @return 		The ending point of the segment
	 */
	public Vec2f getEndPoint() {
		return endPoint;
	}

	public void reverse() {
		Vec2f temp = this.beginPoint;
		this.beginPoint = this.endPoint;
		this.endPoint = temp;
	}

	@Override
	/**
	 * Returns a useful Stirng representation of this Segment object
	 */
	public String toString() {
		return "[engine.lighting.Segment BEGIN=" + beginPoint + " END=" + endPoint + "]";
	}
}
