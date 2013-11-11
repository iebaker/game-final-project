package engine.collision;

import java.awt.Graphics2D;

import cs195n.Vec2f;
import engine.Viewport;

/**
 * Public interface for CollisionShape, supporting collisions of shapes and points, drawing, and location methods
 * 
 * @author dgattey
 * 
 */
public interface CollisionShape {
	
	/**
	 * Collides this shape with another collision shape, o
	 * 
	 * @param o
	 *            another collision shape to check for collisions
	 * @return if o and this shape are colliding
	 */
	public boolean collides(CollisionShape o);
	
	/**
	 * Collides this shape with the Point p
	 * 
	 * @param p
	 *            a Point to check for collision
	 * @return if p is colliding with this shape
	 */
	public boolean collidesPoint(Vec2f p);
	
	/**
	 * Collides this shape with the Circle c
	 * 
	 * @param c
	 *            a Circle to check for collision
	 * @return if c is colliding with this shape
	 */
	public boolean collidesCircle(Circle c);
	
	/**
	 * Collides this shape with the AAB aab
	 * 
	 * @param aab
	 *            an AAB to check for collision
	 * @return if aab is colliding with this shape
	 */
	public boolean collidesAAB(AAB aab);
	
	/**
	 * Collides this shape with the Poly p
	 * 
	 * @param p
	 *            a Poly to check for collision
	 * @return if p is colliding with this shape
	 */
	public boolean collidesPoly(Poly p);
	
	/**
	 * Finds the MTV for this shape and otherShape, or null if they aren't colliding
	 * 
	 * @param otherShape
	 *            the other shape to use in calculating an MTV
	 * @return the MTV between this and otherShape
	 */
	public Vec2f getMTV(CollisionShape otherShape);
	
	/**
	 * Casts the pay passed in onto the given shape
	 * 
	 * @param ray
	 *            The ray to cast onto this
	 * @return The point at which this shape and the ray collide (null if no collision)
	 */
	public Vec2f cast(Ray ray);
	
	/**
	 * Draws the shape to g
	 * 
	 * @param g
	 *            the Graphics2D obj representing the screen to draw to
	 */
	public void drawShape(Graphics2D g);
	
	/**
	 * Draws the shape with a fill
	 * 
	 * @param g
	 *            the Graphics2D obj representing the screen to draw to
	 */
	public void drawAndFillShape(Graphics2D g);
	
	/**
	 * Calculates the correct coordinates to make the shape to draw it onscreen
	 * 
	 * @param v
	 *            the viewport representing the transformation
	 */
	public void toScreen(Viewport v);
	
	/**
	 * Changes the location of the shape to the absolute game coordinates specified
	 * 
	 * @param newLocation
	 *            the new location to put the shape
	 */
	public void changeLocation(Vec2f newLocation);
	
	/**
	 * Moves the shape by a relative offset in game coordinates
	 * 
	 * @param move
	 *            the offset to move the shape by
	 */
	public void move(Vec2f move);
	
	/**
	 * Public getter for location
	 * 
	 * @return the location of the shape
	 */
	public Vec2f getLocation();
	
}
