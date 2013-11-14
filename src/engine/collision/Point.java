package engine.collision;

import java.awt.Graphics2D;

import cs195n.Vec2f;
import engine.Shape;

/**
 * Abstract class for Points, supporting collision detection
 * 
 * @author dgattey
 * 
 */
public abstract class Point extends Shape implements CollisionShape {
	
	private static final long	serialVersionUID	= 4089374759818821624L;
	protected float				x;
	protected float				y;
	
	/**
	 * Constructor, taking a point
	 * 
	 * @param vec
	 */
	public Point(Vec2f vec) {
		this.x = vec.x;
		this.y = vec.y;
	}
	
	@Override
	/**
	 * Collides this (a point) with the shape
	 */
	public boolean collides(CollisionShape o) {
		return o.collides(this);
	}
	
	@Override
	/**
	 * Collides the point with another point
	 */
	public boolean collidesPoint(Vec2f p) {
		return (p.x == this.x && p.y == this.y);
	}
	
	@Override
	/**
	 * Collides the point with a circle
	 */
	public boolean collidesCircle(Circle c) {
		return c.collidesPoint(new Vec2f(x, y));
	}
	
	@Override
	/**
	 * Collides the point with an AAB
	 */
	public boolean collidesAAB(AAB aab) {
		return aab.collidesPoint(new Vec2f(x, y));
	}
	
	@Override
	/**
	 * Collides the point with a Poly
	 */
	public boolean collidesPoly(Poly p) {
		return p.collidesPoint(new Vec2f(x, y));
	}
	
	/**
	 * Abstract drawing methods, never to be implemented
	 */
	public abstract void drawShape(Graphics2D g);
	
	public abstract void drawAndFillShape(Graphics2D g);
	
}
