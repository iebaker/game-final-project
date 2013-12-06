package game;

import cs195n.Vec2f;

/**
 * An angry armadillo that is angered by light and will do anything to smother it, preferably by rolling into a ball and running it over.
 * @author Sawyer
 *
 */
public class ArmadilloOfDarkness extends ShadowEnemy {

	private static final long serialVersionUID = 562708050413509584L;
	private Vec2f				goalVelocity = new Vec2f(0,0);
	
	public ArmadilloOfDarkness() {
		super(50, new float[] {0.2f, 1f});
	}
	
	@Override
	public void onTick(float t) {
		float absDist = ((Player) this.world.getPlayer()).getCenterPosition().minus(this.shape.getCenter()).mag2();
		if(absDist <= 300000) {
			float xDist = ((Player) this.world.getPlayer()).getCenterPosition().x - this.shape.getCenter().x;
			if(xDist > 0) {
				goalVelocity = new Vec2f(600, 0);
			}
			else {
				goalVelocity = new Vec2f(-600, 0);
			}
		}
		
		if (!goalVelocity.equals(new Vec2f(0, 0))) {
			if (!goalVelocity.equals(getVelocity())) {
				applyImpulse((goalVelocity.minus(getVelocity())).smult(0.05f));
			}
		}
		
		super.onTick(t);
	}
}
