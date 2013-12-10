package game.entities.spawners;

import cs195n.LevelData.EntityData;
import engine.World;
import engine.entity.Entity;
import game.entities.DarkenedCrystal;
import game.entities.LightCrystal;

public class CrystalSpawner extends Entity implements Spawner {
	
	private static final long serialVersionUID = -2639861131160682303L;
	protected Entity toSpawn;
	protected boolean active = false;
	protected EntityData properties;
	
	public CrystalSpawner() {
		super();
		this.isStatic = true;
		this.stopsLight = false;
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		properties = ed;
		world = w;
		toSpawn = new LightCrystal(this);
		toSpawn.setProperties(properties, world);
		world.addEntity(toSpawn);
		active = true;
	}
	
	@Override
	public void spawnConsumed() {
		toSpawn = null;
		active = false;
	}
		
	@Override
	public void produce() {
		if(!active) {
			toSpawn = new DarkenedCrystal(this);
			toSpawn.setProperties(properties, world);
			world.addEntity(toSpawn);
			active = true;
		}
	}

}
