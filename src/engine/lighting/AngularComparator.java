package src.engine.lighting;

import cs195n.Vec2f;
import java.util.Comparator;

/**
 * A comparator for Vec2f that uses the Math.atan2() function in order to rank points
 * in a counterclockwise ordering around the light source.
 */
public class AngularComparator extends Comparator<Vec2f> {
	
	private Vec2f sourcePoint;

	/**
	 * Constructor.  Take a Vec2f because it needs a source point
	 * @param source  	The source of the light this time.
	 */
	public AngularComparator(Vec2f source) {
		this.sourcePoint = source;
	}

	/**
	 * Compares 2 Vec2f points.
	 *
	 * @param v1
	 * @param v2
	 * @return 		-1, 0, or 1 as the first number is less than, equal to, or greater than the second.
	 */
	@Override public int compare(Vec2f v1, Vec2f v2) {
		v1 = v1.minus(sourcePoint);
		v2 = v2.minus(sourcePoint);

		double angle1 = this.angleTo(v1);
		double angle2 = this.angleTo(v2);

		if(angle1 < angle2) {
			return -1;
		} else if(angle1 == angle2) {
			return 0;
		} else {
			return 1;
		}
	}


	/** 
	 * Modified atan2() which ranks the points all the way around the circle, instead of just over half.
	 * 
	 * @param point  	The Vec2f to run calculations on
	 * @return 			A double ranging from 0 to 2PI representing the distance around the circle of the point.
	 */
	public double angleTo(Vec2f point) {
		double temp = Math.atan2(point.y, point.x);

		if(temp < 0) {
			temp = 2 * Math.PI + temp;
		}
		return temp;
	}
}