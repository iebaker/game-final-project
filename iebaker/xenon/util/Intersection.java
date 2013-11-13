package iebaker.xenon.util;

import iebaker.xenon.core.Entity;
import cs195n.Vec2f;

public class Intersection {
	private Entity my_entity;
	private Vec2f my_intersect;
	private float my_distance;
	
	public Intersection(Entity e, Vec2f i) {
		my_entity = e;
		my_intersect = i;
		my_distance = Float.MAX_VALUE;
	}

	public Entity getEntity() {
		return my_entity;
	}

	public Vec2f getIntersect() {
		return my_intersect;
	}

	public float getDistance() {
		return my_distance;
	}

	public void setDistance(float d) {
		my_distance = d;
	}
}