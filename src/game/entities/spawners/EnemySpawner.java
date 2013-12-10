package game.entities.spawners;

import cs195n.LevelData.EntityData;
import engine.World;
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
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		properties = ed;
		world = w;
		produce();
	}
	
	public void spawnConsumed() {
		toSpawn = null;
		active = false;
	}
		
	@Override
	public abstract void produce();

}
