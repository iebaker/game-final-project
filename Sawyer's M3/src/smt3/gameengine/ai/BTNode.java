package smt3.gameengine.ai;

/**
 * 
 * @author Sawyer
 * Represents a Binary Tree node. Used for A*
 *
 */

public interface BTNode {
	public Status update(float seconds);
	public void reset();
}
