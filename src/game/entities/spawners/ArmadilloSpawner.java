package game.entities.spawners;

import game.entities.ArmadilloOfDarkness;

public class ArmadilloSpawner extends EnemySpawner {

	private static final long serialVersionUID = 138511048743627949L;

	@Override
	public void makeEnemy() {
		if(!active) {
			toSpawn = new ArmadilloOfDarkness(this);
			toSpawn.setProperties(properties, world);
			world.addEntity(toSpawn);
			active = true;
		}
	}

}
