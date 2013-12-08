package game.entities;

/**
 * The most basic enemy. Just a ball of darkness that steals light from the player
 * @author Sawyer
 *
 */
public class DuskBall extends ShadowEnemy {

	private static final long serialVersionUID = -8427819650544703193L;

	public DuskBall() {
		super(100, new float[] {0.2f, 1f});
		this.drains = true;
	}
}
