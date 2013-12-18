package iebaker.krypton.world.continuous;

import cs195n.Vec2f;
import iebaker.krypton.util.Utils;

public class Circle implements Shape {
	private Vec2f my_center_position;
	private float my_radius;

	public Circle(Vec2f pos, float rad) {
		my_center_position = pos;
		my_radius = rad;
	}

	public boolean checkCollision(Shape s) {
		return s.checkCircleCollision(this);
	}

	public boolean checkCircleCollision(Circle c) {
		float dist = my_center_position.dist(c.getCenter());
		float comp = (my_radius + c.getRadius());

		if(dist < comp) {
			return true;
		}	
		return false;
	}
	
	public boolean checkAABCollision(AAB a) {
		Vec2f their_upper_left = a.getUpperLeft();
		Vec2f their_lower_right = a.getLowerRight();

		Vec2f closest_point = new Vec2f(Utils.clamp(their_upper_left.x, their_lower_right.x, my_center_position.x),
			Utils.clamp(their_upper_left.y, their_lower_right.y, my_center_position.y));

		if(my_center_position.dist(closest_point) < my_radius) {
			return true;
		}
		return false;
	}

	public boolean checkPolygonCollision(Polygon p) {
		java.util.Set<SeparatingAxis> axes = p.toAxisSet();

		float min_dist = Float.MAX_VALUE;
		Vec2f min_point = null;
		for(Vec2f point : p.getPoints()) {
			float temp_dist = point.dist(my_center_position);
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_point = point;
			}
		}

		if(min_point != null) {
			axes.add(new SeparatingAxis(min_point.minus(my_center_position)));
		}

		for(SeparatingAxis axis : axes) {
			Range r1 = axis.project(p);
			Range r2 = axis.project(this);
			if(!r1.overlaps(r2)) {
				return false;
			}
		}

		return true;
	}

	public Vec2f getCenter() {
		return my_center_position;
	}

	public float getRadius() {
		return my_radius;	
	}
}
