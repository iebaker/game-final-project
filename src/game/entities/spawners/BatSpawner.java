package game.entities.spawners;

import game.entities.DuskBat;

public class BatSpawner extends EnemySpawner {
	
	private static final long serialVersionUID = 2168718817034464840L;

	@Override
	public void produce() {
		if(!active) {
			toSpawn = new DuskBat(this);
			toSpawn.setProperties(properties, world);
			world.addEntity(toSpawn);
			active = true;
		}
	}

}
