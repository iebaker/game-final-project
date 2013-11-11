package engine.collision;

import java.util.ArrayList;

import cs195n.Vec2f;

/**
 * MTV class, supporting calculation of different MTVs
 * 
 * @author dgattey
 * 
 */
public class MTV {
	/**
	 * Finds the MTV between two intervals
	 * 
	 * @param a
	 *            Range 1
	 * @param b
	 *            Range 2
	 * @return MTV for two intervals
	 */
	public Float intervalMTV(Range a, Range b) {
		Float aRight = b.getMax() - a.getMin();
		Float aLeft = a.getMax() - b.getMin();
		if (aLeft < 0 || aRight < 0) return null;
		if (aRight < aLeft)
			return aRight;
		else
			return -aLeft;
	}
	
	/**
	 * Finds the MTV between two shapes (yes, using instanceof...)
	 * 
	 * @param shape
	 *            Shape 1
	 * @param other
	 *            Shape 2
	 * @return MTV for two shapes
	 */
	public Vec2f shapeMTV(CollisionShape shape, CollisionShape other) {
		if ((shape instanceof AAB || shape instanceof Poly) && (other instanceof AAB || other instanceof Poly))
			return polyPolyMTV(shape, other);
		else if (shape instanceof Circle) {
			if (other instanceof Circle)
				return circleCircleMTV((Circle) shape, (Circle) other).smult(-1);
			else if (other instanceof Poly)
				return circlePolyMTV((Circle) shape, (Poly) other);
			else if (other instanceof AAB) return circleAABMTV((Circle) shape, (AAB) other).smult(-1);
		} else if (other instanceof Circle) {
			if (shape instanceof Poly) {
				Vec2f mtv = circlePolyMTV((Circle) other, (Poly) shape);
				if (mtv != null) return mtv.smult(-1);
			} else if (shape instanceof AAB) return circleAABMTV((Circle) other, (AAB) shape);
		}
		// should only occur if compound shapes involved
		return null;
	}
	
	/**
	 * Finds MTV for a Circle and an MTV
	 * 
	 * @param shape
	 *            Circle 1
	 * @param other
	 *            AAB 2
	 * @return MTV for two shapes
	 */
	private Vec2f circleAABMTV(Circle shape, AAB other) {
		if (other.collidesPoint(shape.center)) {
			return generalMTV(shape, other, other.getAxes());
		} else {
			Vec2f clamped = other.clamp(shape.center);
			Vec2f connex = shape.center.minus(clamped);
			float dist = connex.mag() - shape.radius;
			Vec2f mtv = connex.normalized().smult(dist);
			if (mtv == null) System.out.println("Null mtv for shapes: " + shape + ", " + other);
			assert (mtv != null);
			return mtv;
		}
	}
	
	/**
	 * Finds MTV for a Circle and a Poly
	 * 
	 * @param shape
	 *            Circle 1
	 * @param other
	 *            Poly 2
	 * @return MTV for two shapes
	 */
	private Vec2f circlePolyMTV(Circle shape, Poly other) {
		ArrayList<Vec2f> axes = other.getAxes();
		float dist = Float.MAX_VALUE;
		Vec2f point = null;
		for (Vec2f pt : other.getPoints()) {
			float newD = shape.center.minus(pt).mag2();
			if (newD < dist) {
				point = pt;
				dist = newD;
			}
		}
		SeparatingAxis circAxis = new SeparatingAxis(point.minus(shape.center), true);
		axes.add(circAxis.getNormalizedAxis());
		return generalMTV(shape, other, axes);
	}
	
	/**
	 * Finds MTV for a Circle and a Circle
	 * 
	 * @param shape
	 *            Circle 1
	 * @param other
	 *            Circle 2
	 * @return MTV for two shapes
	 */
	private Vec2f circleCircleMTV(Circle shape, Circle other) {
		Vec2f offset = shape.center.minus(other.center);
		float radii = (shape.radius + other.radius);
		float dist = offset.mag();
		Vec2f mtv = offset.normalized().smult(dist - radii);
		if (mtv == null) System.out.println("Null mtv for shapes: " + shape + ", " + other);
		assert (mtv != null);
		return mtv;
	}
	
	/**
	 * Finds MTV for a Poly/AAB and a Poly/AAB
	 * 
	 * @param shape
	 *            Shape 1
	 * @param other
	 *            Shape 2
	 * @return MTV for two shapes
	 */
	private Vec2f polyPolyMTV(CollisionShape shape, CollisionShape other) {
		ArrayList<Vec2f> axes = (shape instanceof AAB) ? ((AAB) shape).getAxes() : ((Poly) shape).getAxes();
		axes.addAll((other instanceof AAB) ? ((AAB) other).getAxes() : ((Poly) other).getAxes());
		return generalMTV(shape, other, axes);
	}
	
	/**
	 * Calculates MTV given axes and shapes to collide
	 * 
	 * @param shape
	 *            Shape 1
	 * @param other
	 *            Shape 2
	 * @param axes
	 *            axes to use for separating axes
	 * @param noConversion
	 *            if should convert axis to perpendicular
	 * @return MTV for two shapes given axes
	 */
	private Vec2f generalMTV(CollisionShape shape, CollisionShape other, ArrayList<Vec2f> axes) {
		Float minMagnitude = Float.MAX_VALUE;
		Vec2f mtv = null;
		for (Vec2f axis : axes) {
			SeparatingAxis a = new SeparatingAxis(axis, true);
			Float mtv1d = intervalMTV(a.project(shape), a.project(other));
			if (mtv1d == null || mtv1d == 0) return null;
			if (Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis.smult(mtv1d);
			}
		}
		return mtv;
	}
}
