package game.entities;

import game.entities.spawners.Spawner;

/**
 * The most basic enemy. Just a ball of darkness that steals light from the player
 * 
 * @author Sawyer
 * 
 */
public class DarkFrog extends ShadowEnemy {
	
	private static final long	serialVersionUID	= -8427819650544703193L;
	private float jumpCount = 5;
	
	public DarkFrog(Spawner source) {
		super(100, new float[] { 0.2f, 1f }, source);
		drains = true;
	}
	
	@Override
	public void onTick(float t) {
		if(jumpCount > 0) {
			jumpCount -= t;
		}
		if(jumpCount <= 0) {
			if(lastMTV != null && lastMTV.y < 0) {
				resetY();
				applyImpulse(lastMTV.normalized().smult(world.gravity() * 30));
				jumpCount = 5;
			}
		}
		super.onTick(t);
	}
	
	@Override
	public String getName() {
		return "Dark Frog";
	}
}
