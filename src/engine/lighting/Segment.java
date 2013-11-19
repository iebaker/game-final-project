package engine.lighting;

import cs195n.Vec2f;

/**
 * Segment is a class representing an ordered tuple of two points which defines
 * a single line segment making up the LightWorld
 */
public class Segment {
	private Vec2f beginPoint = null;
	private Vec2f endPoint = null;

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
		}
	}

	/**
	 * Accessor method for the beginning point of the segment
	 *
	 * @return 		The beginning point of the segment
	 */
	public Vec2f getBeginPoint() {
		return beginPoint;
	}


	/**
	 * Accessor method for the ending point of the segment.
	 *
	 * @return 		The ending point of the segment
	 */
	public Vec2f getEndPoint() {
		return endPoint;
	}
}