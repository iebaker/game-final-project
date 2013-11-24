package game;

import cs195n.Vec2f;
import engine.entity.Entity;
import engine.entity.PassableEntity;

public class WaterEntity extends PassableEntity {
	
	private static final long	serialVersionUID	= -8333751295539940583L;
	
	/**
	 * Collision response: translates entity out of collision, applies impulse, and does the same for the other shape -
	 * note: if Static, the translation is double for the non static entity for smoothness
	 * 
	 * @param collisionInfo
	 */
	@Override
	public void afterCollision(Entity o2) {
		super.afterCollision(o2);
		System.out.println("WORKING");
		o2.applyForce((new Vec2f(0, world.gravity())).smult(0.8f * 9999000));
	}
}
