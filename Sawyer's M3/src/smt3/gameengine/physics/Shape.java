package smt3.gameengine.physics;

import java.awt.Color;

import cs195n.Vec2f;

public abstract class Shape implements Drawable {
	
	//Find the overlap between two ranges. Will return null if there is no overlap
	public Float rangeMTV(Interval a, Interval b) {
		
		float aRight = b.max - a.min;
		float aLeft = a.max - b.min;
		if(aLeft < 0 || aRight < 0) {
			return null;
		}
		
		if(aRight < aLeft) {
			return -aRight;
		}
		return aLeft;
	}
	
	public Float linecast(Vec2f a, Vec2f b, Vec2f d, Vec2f p) {
		Vec2f m = b.minus(a).normalized();
		Vec2f n = new Vec2f(m.y, -m.x);
		if((a.minus(p)).cross(d) * (b.minus(p)).cross(d) <= 0) {
			float t = ((b.minus(p)).dot(n)) / (d.dot(n));
			if(t >= 0) {
				return t;
			}
		}
		
		return null;
	}
	
	public abstract Vec2f getCenter();
	public abstract Float raycast(Vec2f source, Vec2f dir);
	public abstract MTVHolder collide(Shape s);
	public abstract MTVHolder collideRect(Rectangle r);
	public abstract MTVHolder collideCircle(Circle c);
	public abstract MTVHolder collidePolygon(ConvexPolygon p);
	public abstract boolean contains(Vec2f p);
	public abstract void setColor(Color c);
	public abstract void setCoords(Vec2f coords);
	public abstract Vec2f getCoords();
	public abstract Vec2f getDims();
}
