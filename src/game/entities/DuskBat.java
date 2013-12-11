package game.entities;

import game.entities.spawners.Spawner;
import cs195n.Vec2f;


public class DuskBat extends ShadowEnemy {

	private static final long serialVersionUID = -7338229074361234314L;
	private float				goalXVelocity		= 0;
	private float goalYVelocity = 0;

	public DuskBat(Spawner source) {
		super(25, new float[] {0.2f, 1f}, source);
		gravityImmune = true;
	}
	
	@Override
	public void onTick(float t) {
		if (world.getPlayer() != null) {
			float absDist = ((Player) world.getPlayer()).getCenterPosition().minus(shape.getCenter()).mag2();
			if (absDist <= 300000) {
				float xDist = ((Player) world.getPlayer()).getCenterPosition().x - shape.getCenter().x;
				if (xDist > 0) {
					goalXVelocity = 300;
				} else {
					goalXVelocity = -300;
				}
				float yDist = ((Player) world.getPlayer()).getCenterPosition().y - shape.getCenter().y;
				if (yDist > 0) {
					goalYVelocity = 300;
				} else {
					goalYVelocity = -300;
				}
			}
			
			if (!(goalXVelocity == 0)) {
				if (!(goalXVelocity == getVelocity().x)) {
					applyImpulse((new Vec2f(goalXVelocity, 0).minus(getVelocity())).smult(0.05f));
				}
			}
			
			if (!(goalYVelocity == 0)) {
				if (!(goalYVelocity == getVelocity().y)) {
					applyImpulse((new Vec2f(0, goalYVelocity).minus(getVelocity())).smult(0.05f));
				}
			}
			
			super.onTick(t);
		}
	}
	
	@Override
	public String getName() {
		return "Dusk Bat";
	}
}
