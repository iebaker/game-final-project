package engine.collision;

import cs195n.Vec2f;
import engine.entity.Entity;

/**
 * Class to represent a collision and respond to it
 * 
 * @author dgattey
 * 
 */
public class CollisionInfo {
	
	public final Entity			other;
	public final Vec2f			mtv;
	public final CollisionShape	thisShape;
	public final CollisionShape	otherShape;
	
	/**
	 * Constructor for a CollisionInfo object
	 * 
	 * @param a
	 *            the entity colliding with something
	 * @param b
	 *            the entity that a is colliding with
	 */
	public CollisionInfo(Entity a, Entity b) {
		this.other = b;
		this.thisShape = a.shape;
		this.otherShape = b.shape;
		this.mtv = a.shape.getMTV(b.shape);
	}
}
