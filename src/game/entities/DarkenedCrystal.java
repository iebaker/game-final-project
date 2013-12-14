package game.entities;

import java.awt.Color;

import cs195n.LevelData.EntityData;
import engine.World;
import engine.entity.Entity;
import game.entities.spawners.Spawner;

public class DarkenedCrystal extends Entity implements Consumable {
	
	private static final long	serialVersionUID	= -7516116297460743602L;
	protected Spawner			source;
	
	public DarkenedCrystal(Spawner source) {
		super();
		stopsLight = false;
		this.source = source;
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		super.setProperties(ed, w);
		c = new Color(180, 180, 220);
	}
	
	@Override
	public void destroy() {
		world.removeEntity(this);
		source.spawnConsumed();
	}
	
}
