package engine.entity;

import java.awt.Color;

import engine.World;
import engine.collision.CollisionShape;

public class PassableEntity extends Entity {
	
	private static final long	serialVersionUID	= -7382617960447661170L;
	
	public PassableEntity(CollisionShape shape, float width, float height, float density, float restitution,
			boolean isStatic, Color c, World world) {
		super(shape, width, height, density, restitution, isStatic, c, world);
		world.addPassableEntity(this);
		world.removeEntity(this);
	}
	
}
