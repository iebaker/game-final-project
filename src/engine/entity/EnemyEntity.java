package engine.entity;

import java.util.Map;

import cs195n.LevelData.EntityData;
import cs195n.Vec2f;
import engine.World;
import engine.connections.Input;

/**
 * Empty Entity subclass to support classification of enemies
 * 
 * @author dgattey
 * 
 */
public class EnemyEntity extends Entity {
	
	private static final long	serialVersionUID	= -6022185589322028097L;
	private float				damage;
	
	@Override
	public void setProperties(EntityData ed, World world) {
		super.setProperties(ed, world);
		this.damage = Float.parseFloat(create("damage", "1", ed));
		
		/**
		 * Makes the enemy stop being static and start closing in on player
		 */
		inputs.put("startClose", new Input() {
			
			private static final long	serialVersionUID	= -6313273253448519746L;
			
			@Override
			public void run(Map<String, String> args) {
				startClose();
				
			}
		});
	}
	
	private void startClose() {
		isStatic = false;
		applyForce(new Vec2f(0, -2000000));
	}
	
	public float getDamage() {
		return damage;
	}
}
