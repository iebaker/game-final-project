package iebaker.xenon.geom;

import cs195n.Vec2f;
import java.util.HashSet;
import iebaker.xenon.util.Utils;
import iebaker.xenon.util.Collision;
import iebaker.xenon.util.Collisions;

import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;

public class AAB implements Shape {
	private Vec2f my_upper_left;
	private Vec2f my_lower_right;

	public AAB(Vec2f ul, Vec2f lr) {
		my_upper_left = ul;
		my_lower_right = lr;
	}

	public Collision checkCollision(Shape s) {
		return s.checkAABCollision(this);
	}

	public static java.util.Set<SeparatingAxis> standardAxes() {
		java.util.Set<SeparatingAxis> axes = new HashSet<SeparatingAxis>();

		axes.add(new SeparatingAxis(new Vec2f(0,1)));
		axes.add(new SeparatingAxis(new Vec2f(1,0)));

		return axes;
	}

	public Collision checkCircleCollision(Circle c) {
		Collision col = c.checkAABCollision(this);
		if(col.collides()) return new Collision(col.collides(), col.mtv2(), this, c);
		else return new Collision(false);
	}

	public Collision checkAABCollision(AAB a) {
		Vec2f their_upper_left = a.getUpperLeft();
		Vec2f their_lower_right = a.getLowerRight();
	
		if(Utils.overlap(their_upper_left.x, their_lower_right.x, my_upper_left.x, my_lower_right.x) &&
		   Utils.overlap(their_upper_left.y, their_lower_right.y, my_upper_left.y, my_lower_right.y)) {

			Float xOverlap = Utils.intervalMTV(their_upper_left.x, their_lower_right.x, my_upper_left.x, my_lower_right.x);
			Float yOverlap = Utils.intervalMTV(their_upper_left.y, their_lower_right.y, my_upper_left.y, my_lower_right.y);

			if(xOverlap < yOverlap) {
				return new Collision(true, (new Vec2f(1, 0)).smult(xOverlap), this, a);
			} else {
				return new Collision(true, (new Vec2f(0, 1)).smult(yOverlap), this, a);
			}
		}
		return new Collision(false);
	}

	public Collision checkPolygonCollision(Polygon p) { 
		java.util.Set<SeparatingAxis> axes = p.toAxisSet();
		axes.addAll(AAB.standardAxes());
		return Collisions.axisCollide(axes, this, p);
	}

	public boolean containsPoint(Vec2f point) {
		return Utils.within(point.x, my_upper_left.x, my_lower_right.x) &&
			   Utils.within(point.y, my_upper_left.y, my_lower_right.y);
	}

	public Vec2f getUpperLeft() {
		return my_upper_left;
	}

	public Vec2f getLowerRight() {
		return my_lower_right;
	}

	public Vec2f getCenter() {
		float vCenter = (my_upper_left.y + my_lower_right.y)/2;
		float hCenter = (my_upper_left.x + my_lower_right.x)/2;
		return new Vec2f(hCenter, vCenter);
	}

	public Shape at(Vec2f newCenter) {
		Vec2f pointing = newCenter.minus(this.getCenter());
		return new AAB(my_upper_left.plus(pointing), my_lower_right.plus(pointing));
	}

	public void drawSelf(Artist a, Graphics2D g) {
		a.rect(g, my_upper_left.x, my_upper_left.y, my_lower_right.x - my_upper_left.x, my_lower_right.y - my_upper_left.y);
	}

	@Override
	public String toString() {
		return "[AAB from UL=" + my_upper_left + " to LR=" + my_lower_right + "]";
	}

	public String typeString() {
		return "[AAB]";
	}
}
