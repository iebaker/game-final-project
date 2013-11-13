package engine.collision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import cs195n.Vec2f;
import engine.Shape;
import engine.Viewport;

/**
 * Class for Axis Aligned Boxes, supporting collision detection
 * 
 * @author dgattey
 * 
 */
public class AAB extends Shape implements CollisionShape {
	
	private Rectangle2D	rect;
	protected Vec2f		min;
	protected Vec2f		max;
	
	/**
	 * Constructor takes sdim, dim, a color
	 * 
	 * @param sdim
	 * @param dim
	 * @param c
	 */
	public AAB(Vec2f sdim, Vec2f dim, Color c) {
		this(sdim, dim);
		this.c = c;
	}
	
	/**
	 * Alternate constructor with only coordinates
	 * 
	 * @param sdim
	 * @param dim
	 */
	public AAB(Vec2f sdim, Vec2f dim) {
		this.min = sdim;
		this.max = dim;
		this.rect = new Rectangle2D.Double(sdim.x, sdim.y, dim.x - sdim.x, dim.y - sdim.y);
	}
	
	/**
	 * Draws shape to screen
	 */
	public void drawShape(Graphics2D g) {
		g.draw(rect);
	}
	
	/**
	 * Does the same as just drawing the shape, but also fills it with the correct color
	 */
	public void drawAndFillShape(Graphics2D g) {
		g.fill(rect);
	}
	
	@Override
	/**
	 * Returns the MTV of this and the otherShape
	 */
	public Vec2f getMTV(CollisionShape otherShape) {
		return new MTV().shapeMTV(this, otherShape);
	}
	
	/**
	 * Returns the axes of this AAB (just 1,0 and 0,1)
	 * 
	 * @return the axes of the AAB
	 */
	public ArrayList<Vec2f> getAxes() {
		ArrayList<Vec2f> ax = new ArrayList<Vec2f>();
		ax.add(new Vec2f(0, 1));
		ax.add(new Vec2f(1, 0));
		return ax;
	}
	
	@Override
	/**
	 * Collides a shape with this (an AAB)
	 */
	public boolean collides(CollisionShape o) {
		return o.collidesAAB(this);
	}
	
	/**
	 * Collides a Point (Vec2f) with an AAB
	 */
	public boolean collidesPoint(Vec2f p) {
		return ((p.x > min.x && p.x < max.x) && (p.y > min.y && p.y < max.y));
	}
	
	@Override
	/**
	 * Collides a Circle with an AAB
	 */
	public boolean collidesCircle(Circle c) {
		return c.collidesPoint(clamp(c.center));
	}
	
	@Override
	/**
	 * Collides an AAB with an AAB
	 */
	public boolean collidesAAB(AAB aab) {
		boolean overlapX = this.min.x < aab.max.x && this.max.x > aab.min.x;
		boolean overlapY = this.min.y < aab.max.y && this.max.y > aab.min.y;
		return (overlapX && overlapY);
		
	}
	
	@Override
	/**
	 * Collides a Poly with an AAB
	 */
	public boolean collidesPoly(Poly p) {
		return p.collidesAAB(this);
	}
	
	@Override
	/**
	 * Raycasts onto this shape
	 */
	public Vec2f cast(Ray ray) {
		ArrayList<Vec2f> pts = new ArrayList<Vec2f>();
		pts.add(min);
		pts.add(new Vec2f(max.x, min.y));
		pts.add(max);
		pts.add(new Vec2f(min.x, max.y));
		return ray.getIntersection(pts);
	}
	
	@Override
	/**
	 * Updates the rect frame given the viewport transform
	 * @param l
	 * @param d
	 */
	public void toScreen(Viewport v) {
		Vec2f m1 = v.gamePtToScreen(this.min);
		Vec2f m2 = v.gamePtToScreen(this.max);
		this.rect.setFrame(m1.x, m1.y, m2.x - m1.x, m2.y - m1.y);
	}
	
	@Override
	/**
	 * Changes the position of the AAB in game
	 */
	public void changeLocation(Vec2f move) {
		Vec2f diff = this.max.minus(this.min);
		this.min = move;
		this.max = move.plus(diff);
	}
	
	@Override
	/**
	 * Updates position of the AAB based on previous location in game
	 */
	public void move(Vec2f move) {
		changeLocation(getLocation().plus(move));
	}
	
	@Override
	/**
	 * Returns the location (upper corner) of the shape
	 */
	public Vec2f getLocation() {
		return this.min;
	}
	
	@Override
	/**
	 * Returns the center of this shape
	 */
	public Vec2f getCenter() {
		return this.min.plus(this.max.minus(min).sdiv(2));
	}
	
	/**
	 * Returns a string representing this AAB
	 */
	public String toString() {
		return "AAB<min" + min + ", max" + max + ">";
	}
	
	/**
	 * Clamps a point to the closest point on the AAB
	 * 
	 * @param center
	 *            the point to clamp
	 * @return the clamped point
	 */
	public Vec2f clamp(Vec2f center) {
		float x = (center.x < min.x) ? min.x : ((center.x > max.x) ? max.x : center.x);
		float y = (center.y < min.y) ? min.y : ((center.y > max.y) ? max.y : center.y);
		return new Vec2f(x, y);
	}
}
