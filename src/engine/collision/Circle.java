package engine.collision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import cs195n.Vec2f;
import engine.Shape;
import engine.Vec2fPair;
import engine.Viewport;

/**
 * Class for Circles, supporting collision detection
 * 
 * @author dgattey
 * 
 */
public class Circle extends Shape implements CollisionShape {
	
	private static final long	serialVersionUID	= 6451380434234857918L;
	private final Ellipse2D		circ;
	protected float				radius;
	protected Vec2f				center;
	
	/**
	 * Constructor takes sdim, a radius, and a color
	 * 
	 * @param sdim
	 * @param radius
	 * @param c
	 */
	public Circle(Vec2f sdim, float radius, Color c) {
		this(sdim, radius);
		this.c = c;
	}
	
	/**
	 * Alternate constructor with only sdim and radius
	 * 
	 * @param sdim
	 * @param radius
	 */
	public Circle(Vec2f sdim, float radius) {
		this.radius = radius;
		center = sdim.plus(new Vec2f(radius, radius));
		circ = new Ellipse2D.Double(sdim.x, sdim.y, radius * 2, radius * 2);
	}
	
	/**
	 * Draws shape to screen
	 */
	@Override
	public void drawShape(Graphics2D g) {
		g.draw(circ);
	}
	
	/**
	 * Does the same as just drawing the shape, but also fills it with the correct color
	 */
	@Override
	public void drawAndFillShape(Graphics2D g) {
		g.fill(circ);
	}
	
	@Override
	/**
	 * Returns the MTV of this and the otherShape
	 */
	public Vec2f getMTV(CollisionShape otherShape) {
		return new MTV().shapeMTV(this, otherShape);
	}
	
	@Override
	/**
	 * Collides a shape with this (a circle)
	 */
	public boolean collides(CollisionShape o) {
		return o.collidesCircle(this);
	}
	
	/**
	 * Collides a Point (Vec2f) with a Circle
	 */
	@Override
	public boolean collidesPoint(Vec2f p) {
		double radius = Math.pow(this.radius, 2);
		double dist = Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2);
		return (radius > dist);
	}
	
	@Override
	/**
	 * Collides a Circle with a Circle
	 */
	public boolean collidesCircle(Circle c) {
		double r1 = radius;
		double r2 = c.radius;
		double dist = Math.pow(c.center.x - center.x, 2) + Math.pow(c.center.y - center.y, 2);
		return Math.pow(r1 + r2, 2) > dist;
	}
	
	@Override
	/**
	 * Collides an AAB with a Circle
	 */
	public boolean collidesAAB(AAB aab) {
		float x = (center.x < aab.min.x) ? aab.min.x : ((center.x > aab.max.x) ? aab.max.x : center.x);
		float y = (center.y < aab.min.y) ? aab.min.y : ((center.y > aab.max.y) ? aab.max.y : center.y);
		return collidesPoint(new Vec2f(x, y));
	}
	
	@Override
	/**
	 * Collides a Poly with a Circle
	 */
	public boolean collidesPoly(Poly p) {
		return p.collidesCircle(this);
	}
	
	@Override
	/**
	 * Raycasts onto this shape - null means no collision
	 */
	public Vec2f cast(Ray ray) {
		Vec2f a = ray.getSource().minus(center);
		Vec2f b = ray.getSource().plus(ray.getDirection().smult(500000)).minus(center);
		
		float la = a.dot(a) - 2.0f * a.dot(b) + b.dot(b);
		float lb = -2.0f * a.dot(a) + 2.0f * a.dot(b);
		float lc = a.dot(a) - radius * radius;
		float det = lb * lb - 4.0f * la * lc;
		
		// Negative determinant = doesn't collide
		if (det >= 0) {
			float t = (float) (-lb - Math.sqrt(det)) / (2.0f * la);
			if (0 <= t && t <= 1) return center.plus(a.lerpTo(b, t));
		}
		return null;
	}
	
	@Override
	/**
	 * Updates the position of the Circle given the Viewport transform
	 * @param l
	 * @param d
	 */
	public void toScreen(Viewport v) {
		Vec2f c = Viewport.gamePtToScreen(center);
		float r = radius * v.getScale();
		circ.setFrame(c.x - r, c.y - r, r * 2, r * 2);
	}
	
	@Override
	/**
	 * Changes the position of the Circle
	 */
	public void changeLocation(Vec2f move) {
		center = move;
	}
	
	@Override
	/**
	 * Updates position of the Circle based on previous location in game
	 */
	public void move(Vec2f move) {
		changeLocation(getCenter().plus(move));
	}
	
	@Override
	/**
	 * Returns the location of the upper left of a bounding box of the circle
	 */
	public Vec2f getLocation() {
		return center.minus(radius, radius);
	}
	
	@Override
	/**
	 * Returns the location of the center of the circle
	 */
	public Vec2f getCenter() {
		return center;
	}
	
	public float getRadius() {
		return radius;
	}
	
	/**
	 * String representation of a circle
	 */
	@Override
	public String toString() {
		return "Circle<center:" + center + ", radius:" + radius + " OR min:" + center.minus(radius, radius) + " max: "
				+ center.plus(radius, radius) + ">";
	}

	@Override
	public Color getColor() {
		return this.c;
	}

	@Override
	public void setColor(Color c) {
		this.c = c;
	}

	@Override
	public Vec2fPair getBoundingBox() {
		return new Vec2fPair(center.minus(radius, radius), center.plus(radius, radius));
	}
}
