package game.entities;

import engine.World;
import game.entities.spawners.Spawner;
import cs195n.LevelData.EntityData;

public class DarkenedCrystal extends LightCrystal {

	private static final long serialVersionUID = -7516116297460743602L;

	public DarkenedCrystal(Spawner source) {
		super(source);
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		super.setProperties(ed, w);
		this.c = c.darker();
	}
	
	@Override
	public void destroy() {
		world.removeEntity(this);
		this.source.spawnConsumed();
	}

}
