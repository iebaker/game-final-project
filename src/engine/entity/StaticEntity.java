package engine.entity;

import java.util.Map;

import cs195n.Vec2f;
import engine.collision.CollisionShape;
import engine.connections.Input;

/**
 * Static Entity class for any static object in the game
 * 
 * @author dgattey
 * 
 */
public class StaticEntity extends Entity {
	
	private final Vec2f		zVec;
	private CollisionShape	last;
	
	/**
	 * Empty constructor, sets zVec and makes an input for hiding/showing the wall
	 */
	public StaticEntity() {
		super();
		
		/**
		 * Hides wall
		 */
		inputs.put("doHide", new Input() {
			
			@Override
			public void run(Map<String, String> args) {
				last = shape;
				shape = null;
			}
		});
		
		/**
		 * Shows wall
		 */
		inputs.put("doShow", new Input() {
			
			@Override
			public void run(Map<String, String> args) {
				shape = last;
				last = null;
			}
		});
		
		this.zVec = new Vec2f(0, 0);
	}
	
	@Override
	/**
	 * Should never move
	 */
	protected Vec2f getVelocity() {
		return zVec;
	}
	
	@Override
	/**
	 * No forces can apply
	 */
	public void applyForce(Vec2f f) {
		return;
	}
	
	@Override
	/**
	 * No impulses can apply
	 */
	public void applyImpulse(Vec2f p) {
		return;
	}
	
}
