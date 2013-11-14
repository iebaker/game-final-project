package engine.entity;

import java.io.Serializable;
import java.util.Map;

import engine.collision.CollisionShape;
import engine.connections.Input;

/**
 * Static Entity class for any static object in the game
 * 
 * @author dgattey
 * 
 */
public class StaticEntity extends Entity implements Serializable {
	
	private static final long	serialVersionUID	= 7765213139897853879L;
	private CollisionShape		last;
	
	/**
	 * Empty constructor, sets zVec and makes an input for hiding/showing the wall
	 */
	public StaticEntity() {
		super();
		
		/**
		 * Hides wall
		 */
		inputs.put("doHide", new Input() {
			
			private static final long	serialVersionUID	= 8399600977246421801L;
			
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
			
			private static final long	serialVersionUID	= 5192386216627791599L;
			
			@Override
			public void run(Map<String, String> args) {
				shape = last;
				last = null;
			}
		});
	}
	
}
