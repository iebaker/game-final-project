package iebaker.xenon.geom;

import cs195n.Vec2f;

public class SeparatingAxis {
	private Vec2f my_direction;

	public SeparatingAxis(Vec2f dir) {
		// if(dir.x < 0) {
		// 	my_direction = new Vec2f(-dir.x, -dir.y);
		// } else {
			my_direction = dir;
		// }
	}

	public Range project(Shape s) {
		if(s instanceof Polygon) {
			return this.project((Polygon)s);
		} else if(s instanceof Circle) {
			return this.project((Circle)s);
		} else {
			return this.project((AAB)s);
		}
	}

	public Range project(Polygon p) {
		java.util.List<Vec2f> points = p.getPoints();
		java.util.List<Vec2f> path = p.toEdgePath();

		Range range = new Range(this, points.get(0));

		for(Vec2f edge : path) {
			Vec2f projected = edge.projectOnto(my_direction);
			range.update(projected);
		}
		return range;
	}

	public Range project(Circle c) {	
		Range range = new Range(this, c.getCenter());
		Vec2f temp = my_direction.normalized().smult(c.getRadius());
		range.update(temp);
		range.update(new Vec2f(2 * -temp.x, 2 * -temp.y));
		return range;
	}

	public Range project(AAB a) {
		return this.project(new Polygon(a));
	}

	public Vec2f getDirection() {
		return my_direction;
	}

	@Override
	public String toString() {
		return "[SeparatingAxis direction:" + my_direction + "]";
	}
}
