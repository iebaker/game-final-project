package iebaker.xenon.geom;

import cs195n.Vec2f;
import iebaker.xenon.util.Utils;
import iebaker.xenon.util.Collision;
import iebaker.xenon.util.Collisions;

import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;

public class Circle implements Shape {
	private Vec2f my_center;
	private float my_radius;

	public Circle(Vec2f pos, float rad) {
		my_center = pos;
		my_radius = rad;
	}

	public Collision checkCollision(Shape s) {
		return s.checkCircleCollision(this);
	}

	public Collision checkCircleCollision(Circle c) {
		float dist = my_center.dist(c.getCenter());
		float comp = (my_radius + c.getRadius());

		if(dist < comp) {
			Vec2f dir = c.getCenter().minus(my_center);
			dir = dir.isZero() ? dir : dir.normalized().smult(comp - dist);
			return new Collision(true, dir, this, c);
		}	
		return new Collision(false);
	}	

	public Collision checkAABCollision(AAB a) {
		Vec2f their_upper_left = a.getUpperLeft();
		Vec2f their_lower_right = a.getLowerRight();
		if(a.containsPoint(my_center)) {
			return Collisions.axisCollide(AAB.standardAxes(), this, a);
		} else {
			Vec2f closest_point = new Vec2f(
				Utils.clamp(their_upper_left.x, their_lower_right.x, my_center.x),
				Utils.clamp(their_upper_left.y, their_lower_right.y, my_center.y)
			);

			float c2c_dist = closest_point.dist(my_center);

			if(c2c_dist < my_radius) {
				Vec2f dir = my_center.minus(closest_point);
				if(dir.isZero()){
					//System.out.println("Circle.checkAABCollision computed a zero mtv");
					//System.out.println(my_center + "," + closest_point);
				}
				dir = (dir.isZero() ? dir : dir.normalized().smult(my_radius - c2c_dist));
				return new Collision(true, dir, this, a);
			}
			return new Collision(false);
		}
	}

	public Collision checkPolygonCollision(Polygon p) {
		java.util.Set<SeparatingAxis> axes = p.toAxisSet();

		float min_dist = Float.MAX_VALUE;
		Vec2f min_point = null;
		for(Vec2f point : p.getPoints()) {
			float temp_dist = point.dist(my_center);
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_point = point;
			}
		}

		if(min_point != null) {
			axes.add(new SeparatingAxis(min_point.minus(my_center)));
		}

		return Collisions.axisCollide(axes, this, p);
	}

	public Vec2f getCenter() {
		return my_center;
	}

	public float getRadius() {
		return my_radius;	
	}

	public Shape at(Vec2f newCenter) {
		Vec2f pointing = newCenter.minus(my_center);
		return new Circle(my_center.plus(pointing), my_radius);
	}

	public void drawSelf(Artist a, Graphics2D g) {
		a.ellipse(g, my_center.x - my_radius, my_center.y - my_radius, 2 * my_radius, 2 * my_radius);
	}

	@Override
	public String toString() {
		return "[CIRCLE with C=" + my_center + " and R=" + my_radius + "]";
	}

	public String typeString() {
		return "[CIRCLE]";
	}
}
