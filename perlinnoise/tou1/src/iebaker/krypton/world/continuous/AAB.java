package iebaker.krypton.world.continuous;

import cs195n.Vec2f;
import iebaker.krypton.util.Utils;

public class AAB implements Shape {
	private Vec2f my_upper_left;
	private Vec2f my_lower_right;

	public AAB(Vec2f ul, Vec2f lr) {
		my_upper_left = ul;
		my_lower_right = lr;
	}

	public boolean checkCollision(Shape s) {
		return s.checkAABCollision(this);
	}

	public boolean checkCircleCollision(Circle c) {
		Vec2f circle_center = c.getCenter();

	    Vec2f closest_point = new Vec2f(Utils.clamp(my_upper_left.x, my_lower_right.x, circle_center.x), 
			Utils.clamp(my_upper_left.y, my_lower_right.y, circle_center.y));

		if(circle_center.dist(closest_point) <= c.getRadius()) {
			return true;
		}		
		return false;
	}

	public boolean checkAABCollision(AAB a) {
		Vec2f their_upper_left = a.getUpperLeft();
		Vec2f their_lower_right = a.getLowerRight();
	
		if(Utils.overlap(their_upper_left.x, their_lower_right.x, my_upper_left.x, my_lower_right.x) &&
		   Utils.overlap(their_upper_left.y, their_lower_right.y, my_upper_left.y, my_lower_right.y)) {
			return true;
		}
		return false;
	}

	public boolean checkPolygonCollision(Polygon p) {
		//System.out.println("----------Beginning Polygon collision check!!");
		java.util.Set<SeparatingAxis> axes = p.toAxisSet();

		axes.add(new SeparatingAxis(new Vec2f(0,1)));
		axes.add(new SeparatingAxis(new Vec2f(1,0)));

		//System.out.println(axes.size());

		for(SeparatingAxis axis : axes) {
			//System.out.println("-----Collision check along axis " + axis);
			Range r1 = axis.project(this);
			Range r2 = axis.project(p);
			//System.out.println("Projection of me (the AAB) is " + r1);
			//System.out.println("Projection of the polygon is " + r2);
			if(!r1.overlaps(r2)) {
				//System.out.println("-----Collision not found");
				return false;
			}
		}

		//System.out.println("-----Collision found");
		return true;
	}

	public Vec2f getUpperLeft() {
		return my_upper_left;
	}

	public Vec2f getLowerRight() {
		return my_lower_right;
	}
}
