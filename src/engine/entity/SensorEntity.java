package engine.entity;

import engine.connections.Output;

/**
 * Sensor Entity, supporting afterCollision callbacks (or arbitrary ones)
 * 
 * @author dgattey
 * 
 */
public class SensorEntity extends StaticEntity {
	
	private Output	onCollide;
	
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
	public void afterCollision() {
		onCollide.run();
	}
	
}
