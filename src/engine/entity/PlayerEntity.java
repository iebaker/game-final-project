package engine.entity;

import cs195n.Vec2f;

/**
 * Empty interface to be implemented by the game
 * 
 * @author dgattey
 * 
 */
public interface PlayerEntity {
	/**
	 * A way to access the player's position for locational sound
	 * @return Vec2f of the center of the player's position
	 */
	public Vec2f getCenterPosition();
}
