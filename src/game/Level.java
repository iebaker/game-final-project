package game;

import java.io.Serializable;

/**
 * Level class to encapsulate what changes each level - relatively unused here
 * 
 * @author dgattey
 * 
 */
public class Level implements Serializable {
	
	private static final long	serialVersionUID	= -7569167150735348241L;
	private final int			level;
	private float				score;
	
	/**
	 * Constructor, taking a level number and cumulative score
	 * 
	 * @param n
	 */
	public Level(int n, float s) {
		this.level = n;
		this.score = s;
	}
	
	/**
	 * Public getter for the level
	 * 
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Public getter for the score
	 * 
	 * @return
	 */
	public float getScore() {
		return score;
	}
	
	/**
	 * Adds the given float to the score
	 * 
	 * @param n
	 *            A natural number
	 */
	protected void addToScore(float n) {
		this.score += n;
	}
	
	/**
	 * On tick method - adds 3 to score every second
	 * 
	 * @param secs2
	 *            Nanoseconds since the previous tick
	 */
	protected void onTick(float secs2) {
		float secs = secs2;
		addToScore(secs);
	}
}
