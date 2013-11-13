package iebaker.xenon.geom;

import cs195n.*;
import java.util.ArrayList;
import java.util.HashSet;

import iebaker.xenon.util.Collision;
import iebaker.xenon.util.Collisions;

import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;
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

	public Collision checkCollision(Shape s) {
		return s.checkPolygonCollision(this);
	}

	public Collision checkCircleCollision(Circle c) {
		Collision col = c.checkPolygonCollision(this);
		if(col.collides()) return new Collision(true, col.mtv1(), this, c);
		else return new Collision(false);
	}

	public Collision checkAABCollision(AAB a) {
		java.util.Set<SeparatingAxis> axes = this.toAxisSet();
		axes.addAll(AAB.standardAxes());
		return Collisions.axisCollide(axes, this, a);
	}

	public Collision checkPolygonCollision(Polygon p) {
		java.util.Set<SeparatingAxis> axes = this.toAxisSet();
		axes.addAll(p.toAxisSet());
		return Collisions.axisCollide(axes, this, p);
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

	public java.util.Set<LineSegment> toSegmentSet() {
		java.util.Set<LineSegment> return_value = new HashSet<LineSegment>();
		for(int i = 0; i < my_points.size() - 1; ++i) {
			LineSegment temp = new LineSegment(my_points.get(i + 1), my_points.get(i));
			return_value.add(temp);
		}
		return_value.add(new LineSegment(my_points.get(0), my_points.get(my_points.size() - 1)));
		return return_value;
	}

	public java.util.List<Vec2f> getPoints() {
		return my_points;
	}

	public Vec2f getCenter() {
		float hAcc = 0f;
		float vAcc = 0f;
		float len = my_points.size();
		for(Vec2f point : my_points) {
			hAcc += point.x;
			vAcc += point.y;
		}
		float hCenter = hAcc/(float)len;
		float vCenter = vAcc/(float)len;

		return new Vec2f(hCenter, vCenter);
	}

	public Shape at(Vec2f newCenter) {
		Vec2f pointing = newCenter.minus(this.getCenter());
		java.util.List<Vec2f> new_points = new ArrayList<Vec2f>();
		for(Vec2f point : my_points) {
			new_points.add(point.plus(pointing));
		}
		return new Polygon(new_points);
	}

	public void drawSelf(Artist a, Graphics2D g) {
		a.path(g, my_points);
	}

	@Override
	public String toString() {
		return "[POLYGON with P=" + my_points + "]";
	}


	public String typeString() {
		return "[POLYGON]";
	}
}