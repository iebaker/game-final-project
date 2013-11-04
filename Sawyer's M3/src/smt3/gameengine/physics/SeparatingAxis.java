package smt3.gameengine.physics;

import java.util.ArrayList;

import cs195n.Vec2f;

public class SeparatingAxis {

	public final Vec2f _direction;
	
	public SeparatingAxis(Vec2f direction) {
		_direction = direction;
	}
	
	public Interval project(Circle c) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		
		ArrayList<Vec2f> points = new ArrayList<Vec2f>();
		points.add(c.getCenter().plus((float) (c.getRadius()*Math.cos(_direction.angle())), (float) (c.getRadius()*Math.sin(_direction.angle()))));
		points.add(c.getCenter().minus((float) (c.getRadius()*Math.cos(_direction.angle())), (float) (c.getRadius()*Math.sin(_direction.angle()))));
		for(Vec2f point : points) {
			if(point.dot(_direction) <= min) {
				min = point.dot(_direction);
			}
			if(point.dot(_direction) >= max) {
				max = point.dot(_direction);
			}
		}
		return new Interval(min, max);
	}
	
	public Interval project(Rectangle r) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		
		ArrayList<Vec2f> points = new ArrayList<Vec2f>();
		points.add(r.getCoords());
		points.add(r.getCoords().plus(r.getDims().x, 0));
		points.add(r.getCoords().plus(0, r.getDims().y));
		points.add(r.getCoords().plus(r.getDims()));
		for(Vec2f point : points) {
			if(point.dot(_direction) <= min) {
				min = point.dot(_direction);
			}
			if(point.dot(_direction) >= max) {
				max = point.dot(_direction);
			}
		}
		
		return new Interval(min, max);
	}
	
	public Interval project(ConvexPolygon p) {
		float min = Float.MAX_VALUE;
		float max = -Float.MAX_VALUE;
		
		for(Vec2f point : p.getPoints()) {
			if(point.dot(_direction) <= min) {
				min = point.dot(_direction);
			}
			if(point.dot(_direction) >= max) {
				max = point.dot(_direction);
			}
		}
		return new Interval(min, max);
	}	
}
