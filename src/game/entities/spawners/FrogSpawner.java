package game.entities.spawners;

import game.entities.DarkFrog;

public class FrogSpawner extends EnemySpawner {

	private static final long serialVersionUID = -774830340069847124L;

	@Override
	public void produce() {
		if(!active) {
			toSpawn = new DarkFrog(this);
			toSpawn.setProperties(properties, world);
			world.addEntity(toSpawn);
			active = true;
		}
	}

}
