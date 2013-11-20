package game;

import java.util.Map;

import engine.connections.Input;
import engine.entity.Entity;

public class WorldTrigger extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8673507927839925307L;
	
	private GameWorld w;

	public WorldTrigger() {
		this.inputs.put("allowJumps", new Input() {
			private static final long serialVersionUID = 5782831638451389990L;

			@Override
			public void run(Map<String, String> args) {
				System.out.println("triggered");
				WorldTrigger.this.w.unlockJump();
			}
		});
	}
	
	public void setWorld(GameWorld w) {
		this.w = w;
	}
}
