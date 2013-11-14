package engine.entity;

import java.util.Map;

import engine.collision.CollisionShape;
import engine.connections.Input;

/**
 * Static Entity class for any static object in the game
 * 
 * @author dgattey
 * 
 */
public class StaticEntity extends Entity {
	
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
	}
	
}
