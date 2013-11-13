package iebaker.xenon.geom;

import cs195n.Vec2f;

public class LineSegment {
	private Vec2f point1;
	private Vec2f point2;

	public LineSegment(Vec2f p1, Vec2f p2) {
		point1 = p1;
		point2 = p2;
	}

	public Vec2f getP1() {
		return point1;
	}

	public Vec2f getP2() {
		return point2;
	}

	public Vec2f perpNorm() {
		Vec2f connecting = point1.minus(point2);
		connecting = new Vec2f(-connecting.y, connecting.x);
		return connecting.isZero() ? connecting : connecting.normalized();
	}
}