package smt3.gameengine.ai;

import java.util.ArrayList;

/**
 * A generic composite node.
 * @author Sawyer
 *
 */

public abstract class Composite implements BTNode {

	private ArrayList<BTNode> _children = new ArrayList<BTNode>();
	private BTNode _lastRunning;
	
	
	@Override
	public abstract Status update(float seconds);
	
	/**
	 * Resets each child node
	 */
	public void reset() {
		for(BTNode n : _children) {
			n.reset();
		}
	}
	
	/**
	 * 
	 * @return ArrayList of all BTNode children
	 */
	public ArrayList<BTNode> getChildren() {
		return _children;
	}
	
	/**
	 * Adds the given node as a new child
	 * @param child
	 */
	public void addChild(BTNode child) {
		_children.add(child);
	}
	
	/**
	 * 
	 * @return The last BTNode that was running
	 */
	public BTNode getLastRunning() {
		return _lastRunning;
	}
	
	/**
	 * Updates the last running node
	 * @param lastRunning
	 */
	public void setLastRunning(BTNode lastRunning) {
		_lastRunning = lastRunning;
	}
}
