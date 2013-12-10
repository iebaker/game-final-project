package game.entities;

import cs195n.Vec2f;
import engine.collision.CollisionInfo;
import engine.entity.Entity;
import engine.entity.PassableEntity;

public class WaterEntity extends PassableEntity {
	
	private static final long	serialVersionUID	= -8333751295539940583L;
	
	/**
	 * Collision response:
	 */
	@Override
	public void onCollide(CollisionInfo col) {
		Entity o2 = col.other;
		o2.applyForce((new Vec2f(0, world.gravity())).smult(-0.8f * 30));
	}
}
