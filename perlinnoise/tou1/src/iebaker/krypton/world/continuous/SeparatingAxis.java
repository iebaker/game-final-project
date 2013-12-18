package iebaker.krypton.world.continuous;

import cs195n.*;

public class SeparatingAxis {
	private Vec2f my_direction;

	public SeparatingAxis(Vec2f dir) {
		if(dir.x < 0) {
			my_direction = new Vec2f(-dir.x, -dir.y);
		} else {
			my_direction = dir;
		}
	}

	public Range project(Polygon p) {
		//System.out.println("\nBeginning polygon projection of polygon with " + p.getPoints().size() + " edges onto axis " + this);
		java.util.List<Vec2f> points = p.getPoints();
		java.util.List<Vec2f> path = p.toEdgePath();
		//System.out.println("Edge path: " + path);

		Range range = new Range(this, points.get(0));

		for(Vec2f edge : path) {
			Vec2f projected = edge.projectOnto(my_direction);
			range.update(projected);
		}

		//System.out.println("Polygon projected is " + range);
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