package engine.entity;

import engine.connections.Output;

/**
 * Sensor Entity, supporting afterCollision callbacks (or arbitrary ones)
 * 
 * @author dgattey
 * 
 */
public class SensorEntity extends StaticEntity {
	
	private static final long	serialVersionUID	= -308280903550886181L;
	private Output				onCollide;
	private boolean activated = false;
	
	/**
	 * Empty constructor, making a new output from onCollide
	 */
	public SensorEntity() {
		super();
		onCollide = new Output();
		outputs.put("onCollide", onCollide);
	}
	
	@Override
	/**
	 * Runs onCollide after a collision
	 */
	public void afterCollision(Entity other) {
		if(!activated && !other.isStatic && !(other instanceof EnemyEntity)) {
			onCollide.run();
			activated = true;
		}
	}
	
}
