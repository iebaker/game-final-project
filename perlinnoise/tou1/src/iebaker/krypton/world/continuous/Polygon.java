package iebaker.krypton.world.continuous;

import cs195n.*;
import java.util.ArrayList;
import java.util.HashSet;

public class Polygon implements Shape {

	private java.util.List<Vec2f> my_points = new ArrayList<Vec2f>();

	public Polygon(Vec2f... points) {
		for(Vec2f v : points) {
			my_points.add(v);
		}
	}

	public Polygon(AAB a) {
		Vec2f ul = a.getUpperLeft();
		Vec2f lr = a.getLowerRight();
		my_points.add(ul);
		my_points.add(new Vec2f(ul.x, lr.y));
		my_points.add(lr);
		my_points.add(new Vec2f(lr.x, ul.y));
	}

	public Polygon(java.util.List<Vec2f> points) {
		my_points = points;
	}

	public static Polygon regular(int sides, float radius, Vec2f center_point, float rotation) {
		if(sides <= 0) return null;
		Vec2f vector = Vec2f.fromPolar(rotation, radius);
		float angle = rotation;
		java.util.List<Vec2f> points = new ArrayList<Vec2f>();
		for(int i = 0; i < sides; ++i) {
			points.add(center_point.plus(vector));
			angle += (float)((2 * Math.PI)/sides);
			vector = Vec2f.fromPolar(angle, radius);
		}
		return new Polygon(points);
	}

	@Override
	public boolean checkCollision(Shape s) {
		return s.checkPolygonCollision(this);
	}

	@Override
	public boolean checkCircleCollision(Circle c) {
		java.util.Set<SeparatingAxis> axes = this.toAxisSet();

		float min_dist = Float.MAX_VALUE;
		Vec2f min_point = null;
		for(Vec2f point : my_points) {
			float temp_dist = point.dist(c.getCenter());
			if(temp_dist < min_dist) {
				min_dist = temp_dist;
				min_point = point;
			}
		}

		if(min_point != null) {
			axes.add(new SeparatingAxis(min_point.minus(c.getCenter())));
		}

		for(SeparatingAxis axis : axes) {
			Range r1 = axis.project(this);
			Range r2 = axis.project(c);
			if(!r1.overlaps(r2)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean checkAABCollision(AAB a) {
		//System.out.println("----------Beginning AAB collision check!!");
		java.util.Set<SeparatingAxis> axes = this.toAxisSet();

		axes.add(new SeparatingAxis(new Vec2f(0,1)));
		axes.add(new SeparatingAxis(new Vec2f(1,0)));

		//System.out.println(axes.size());

		for(SeparatingAxis axis : axes) {
			//System.out.println("----- Collision check along axis " + axis);
			Range r1 = axis.project(this);
			Range r2 = axis.project(a);
			if(!r1.overlaps(r2)) {
				//System.out.println("-----Collision not found");
				return false;
			}
		}

		//System.out.println("-----Collision found");
		return true;
	}

	@Override
	public boolean checkPolygonCollision(Polygon p) {
		java.util.Set<SeparatingAxis> axes = this.toAxisSet();
		axes.addAll(p.toAxisSet());
		for(SeparatingAxis axis : axes) {
			Range r1 = axis.project(this);
			Range r2 = axis.project(p);
			if(!r1.overlaps(r2)) {
				return false;
			}
		}
		return true;
	}

	public java.util.Set<SeparatingAxis> toAxisSet() {
		java.util.Set<SeparatingAxis> return_value = new HashSet<SeparatingAxis>();
		for(int i = 0; i < my_points.size() - 1; ++i) {
			Vec2f edge = my_points.get(i + 1).minus(my_points.get(i));
			edge = new Vec2f(-edge.y, edge.x);
			return_value.add(new SeparatingAxis(edge));
		}
		Vec2f final_edge = my_points.get(0).minus(my_points.get(my_points.size() - 1));
		final_edge = new Vec2f(-final_edge.y, final_edge.x);
		return_value.add(new SeparatingAxis(final_edge));
		return return_value;
	}

	public java.util.List<Vec2f> toEdgePath() {
		java.util.List<Vec2f> return_value = new ArrayList<Vec2f>();
		for(int i = 0; i < my_points.size() - 1; ++i) {
			Vec2f temp = my_points.get(i + 1).minus(my_points.get(i));
			return_value.add(temp);
		}
		Vec2f final_edge = my_points.get(0).minus(my_points.get(my_points.size() - 1));
		return_value.add(final_edge);
		return return_value;
	}

	public java.util.List<Vec2f> getPoints() {
		return my_points;
	}
}
