package game.entities.spawners;

import java.util.Map;

import cs195n.LevelData.EntityData;
import engine.World;
import engine.connections.Input;
import engine.entity.Entity;
import game.entities.ShadowEnemy;

public abstract class EnemySpawner extends Entity implements Spawner {

	private static final long serialVersionUID = -2639861131160682303L;
	protected ShadowEnemy toSpawn;
	protected boolean active = false;
	protected EntityData properties;
	
	public EnemySpawner() {
		super();
		this.isStatic = true;
		this.stopsLight = false;
		this.inputs.put("playSound", new Input() {

			private static final long serialVersionUID = -2765949738819150438L;

			@Override
			public void run(Map<String, String> args) {
				if(toSpawn != null) {
					toSpawn.runInput("playSound", args);
				}
			}
			
		});
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		properties = ed;
		world = w;
		produce();
	}
	
	@Override
	public void spawnConsumed() {
		toSpawn = null;
		active = false;
	}
		
	@Override
	public abstract void produce();

}
