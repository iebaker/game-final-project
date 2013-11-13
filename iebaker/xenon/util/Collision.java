package iebaker.xenon.util;

import cs195n.Vec2f;
import iebaker.xenon.geom.Shape;

public class Collision {
	private Vec2f mtv1;
	private Vec2f mtv2;
	private boolean collision;
	private Shape shape1;
	private Shape shape2;

	public Collision(boolean c, Vec2f m, Shape s1, Shape s2) {
		Vec2f s1tos2 = s2.getCenter().minus(s1.getCenter());
		if(s1tos2.dot(m) < 0) {
			mtv2 = m;
			mtv1 = new Vec2f(-m.x, -m.y);
		} else {
			mtv1 = m;
			mtv2 = new Vec2f(-m.x, -m.y);
		}
		collision = c;
		shape1 = s1;
		shape2 = s2;
	}

	public Collision(boolean c) {
		collision = c;
	}

	public Vec2f mtv1() {
		return mtv1;
	}

	public Vec2f mtv2() {
		return mtv2;
	}

	public boolean collides() {
		return collision;
	}
}
