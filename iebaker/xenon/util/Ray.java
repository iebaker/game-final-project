package iebaker.xenon.util;

import cs195n.Vec2f;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import iebaker.xenon.core.Entity;

public class Ray {
	private Vec2f my_source;
	private Vec2f my_direction;
	private float my_magnitude;
	private Entity my_entity;
	private java.util.List<Intersection> my_intersections = new ArrayList<Intersection>();

	public Ray(Entity e, Vec2f s, Vec2f d, float m) {
		my_entity = e;
		my_source = s;
		my_direction = d.isZero() ? d : d.normalized();
		my_magnitude = m;
	}

	public Vec2f getSource() {
		return my_source;
	}

	public Vec2f getDirection() {
		return my_direction;
	}

	public float getMagnitude() {
		return my_magnitude;
	}

	public void addPair(float distance, Intersection sp) {
		sp.setDistance(distance);
		my_intersections.add(sp);
		Collections.sort(my_intersections, new Comparator<Intersection>() {
		@Override public int compare(Intersection i1, Intersection i2) {
			Float d1 = new Float(i1.getDistance());
			Float d2 = new Float(i2.getDistance());
			return d1.compareTo(d2);
		}	
		});
	}

	public Intersection firstIntersection() {
		return my_intersections.get(0);
	}

	public boolean intersects() {
		return !my_intersections.isEmpty();
	}

	public Entity getEntity() {
		return my_entity;
	}
}