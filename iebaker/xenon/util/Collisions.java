package iebaker.xenon.util;

import cs195n.Vec2f;

import iebaker.xenon.core.Entity;
import iebaker.xenon.geom.SeparatingAxis;
import iebaker.xenon.geom.Range;
import iebaker.xenon.geom.Shape;
import iebaker.xenon.geom.Circle;
import iebaker.xenon.geom.Polygon;
import iebaker.xenon.geom.LineSegment;
import iebaker.xenon.geom.AAB;

public class Collisions {
	public static void resolve(Entity e1, Entity e2) {
		if(e1 == e2) {
			//System.out.println("They were the same!");
			return;
		} 

		Collision c = e1.getShape().checkCollision(e2.getShape());

		if(c.collides()) {
			e1.onCollideWith(e2);
			e2.onCollideWith(e1);

			if(!e1.isTangible() || !e2.isTangible()) return;

			float cor = (float)Math.sqrt(e1.getCOR() * e2.getCOR());

			Vec2f v1 = e1.getVelocity().projectOnto(c.mtv1());
			Vec2f v2 = e2.getVelocity().projectOnto(c.mtv2());

			float m1 = e1.getMass();
			float m2 = e2.getMass();

			float multiplier;

			if(e1.isFixed() && e2.isFixed()) {

				return;

			} else if(e1.isFixed()) {

				e2.unCollide(c.mtv2());

				multiplier = m2 * (1 + cor);
				//System.out.println("e1 is fixed");

			} else if(e2.isFixed()){

				//System.out.println("e2 is fixed");
				e1.unCollide(c.mtv1());

				multiplier = m1 * (1 + cor);

			} else {

				//System.out.println("Mtv for shape 1 " + c.mtv1() + ", MTV for shape 2 " + c.mtv2());

				e1.unCollide(c.mtv1().smult(0.5f));
				e2.unCollide(c.mtv2().smult(0.5f));

				multiplier = (m1 * m2 * (1 + cor)) / (m1 + m2);

			}

			Vec2f i1 = v2.minus(v1).smult(multiplier);
			Vec2f i2 = v1.minus(v2).smult(multiplier);

			e1.addImpulse(i1);
			e2.addImpulse(i2);
				
		}
	}

	public static Collision axisCollide(java.util.Set<SeparatingAxis> axes, Shape s1, Shape s2) {
		float min_o_amt = Float.MAX_VALUE;
		Vec2f mtv = null;

		for(SeparatingAxis axis : axes) {
			Range r1 = axis.project(s1);
			Range r2 = axis.project(s2);
			SuperFloat o_amt = new SuperFloat(Float.MAX_VALUE);

			if(!r1.overlaps(r2, o_amt)) {
				return new Collision(false);
			} else {
				if(o_amt.getVal() < min_o_amt) {
					min_o_amt = o_amt.getVal();
					Vec2f dir = axis.getDirection();
					mtv = dir.isZero() ? dir : dir.normalized().smult(min_o_amt);
				}
			}
		}

		if(mtv.isZero()) System.out.println("axisCollide computed a zero mtv");
 		return new Collision(true, mtv, s1, s2);
	}

	public static Vec2f checkCircleRayCast(Ray r, Circle c) {
		// System.out.println("		Checking ray circle cast");
		// System.out.println("		Printing math debug:");
		Vec2f circle_center = c.getCenter();
		Vec2f ray_source = r.getSource();
		Vec2f ray_direction = r.getDirection();
		float ray_magnitude = r.getMagnitude();
		float circle_radius = c.getRadius();

		// System.out.println("		Circle center: " + circle_center);
		// System.out.println("		Ray source: " + ray_source);
		// System.out.println("		Ray direction: " + ray_direction);
		// System.out.println("		Ray magnitude: " + ray_magnitude);
		// System.out.println("		Circle radius: " + circle_radius);

		Vec2f source_to_center = circle_center.minus(ray_source);
		//System.out.println("...");
		//System.out.println("		Vector from source to center: " + source_to_center);
		Vec2f projected = source_to_center.projectOnto(ray_direction);
		//System.out.println("		s2c projected onto ray direction: " + projected);

		Vec2f new_point = ray_source.plus(projected);
		//System.out.println("		New point (ray source + projection) calculated to be: " + new_point);
		Vec2f intersection = null;

		float x = circle_center.dist(new_point);
		//System.out.println("		Distance from center of circle of new point: " + x);
		float r2x2 = (float) Math.sqrt(circle_radius*circle_radius - x*x);
		//System.out.println("		r2x2: " + r2x2);

		//System.out.println(projected + ":" + ray_direction);

		if(projected.x*ray_direction.x > 0 && projected.y*ray_direction.y > 0 && x < circle_radius) {
			if(circle_center.dist(ray_source) < circle_radius) {
				intersection = ray_source.plus(ray_direction.smult(projected.mag() + r2x2));
			} else {
				intersection = ray_source.plus(ray_direction.smult(projected.mag() - r2x2));
			}
		}

		//System.out.println("		Intersection calculated to be: " + intersection);
		return intersection;
	}

	public static Vec2f checkAABRayCast(Ray r, AAB a) {
		//System.out.println("Checking ray AAB cast");
		return Collisions.checkPolygonRayCast(r, new Polygon(a));
	}

	public static Vec2f checkPolygonRayCast(Ray r, Polygon p) {
		//System.out.println("Checking ray poly cast");
		java.util.Set<LineSegment> edges = p.toSegmentSet();
		Vec2f ray_source = r.getSource();
		Vec2f ray_direction = r.getDirection();

		float min_t = Float.MAX_VALUE;
		Vec2f intersection = null;

		for(LineSegment segment : edges) {
			Vec2f p1 = segment.getP1();
			Vec2f p2 = segment.getP2();
			Vec2f ap = p1.minus(ray_source);
			Vec2f bp = p2.minus(ray_source);

			if(ap.cross(ray_direction) * bp.cross(ray_direction) > 0) {
				continue;
			} else {
				//System.out.println("Hey");
				float t = bp.dot(segment.perpNorm())/ray_direction.dot(segment.perpNorm());
				if(t < min_t && t > 0) {
					min_t = t;
					intersection = ray_source.plus(ray_direction.smult(t));
				}
				//System.out.println("ho");
			}
		}	
		//System.out.println("		Intersection calculated to be: " + intersection);
		return intersection;
	}

	public static Vec2f checkRayCast(Ray r, Shape s) {
		//System.out.println("	Beginning raycasting against shape at " + s.getCenter());
		if(s instanceof Circle) {
			//System.out.println("	The shape is a circle, so moving to checkCircleCast");
			return checkCircleRayCast(r, (Circle)s);
		} else if(s instanceof AAB) {
			//System.out.println("	The shape is an AAB, so moving to checkAABCast");
			return checkAABRayCast(r, (AAB)s);
		} else if(s instanceof Polygon) {
			//System.out.println("	The shape is a Polygon, to moving to checkPolygonCast");
			return checkPolygonRayCast(r, (Polygon)s);
		}
		//System.out.println("Stupid ray cast that does nothing");
		return null;
	} 
}
