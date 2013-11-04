package smt3.gameengine.ai;

import java.util.ArrayList;

public abstract class Composite implements BTNode {

	private ArrayList<BTNode> _children;
	private BTNode _lastRunning;
	
	public Composite() {
		_children = new ArrayList<BTNode>();
	}
	
	@Override
	public abstract Status update(float seconds);
	
	public void reset() {
		for(BTNode n : _children) {
			n.reset();
		}
	}
	
	public ArrayList<BTNode> getChildren() {
		return _children;
	}
	
	public void addChild(BTNode child) {
		_children.add(child);
	}
	
	public BTNode getLastRunning() {
		return _lastRunning;
	}
	
	public void setLastRunning(BTNode lastRunning) {
		_lastRunning = lastRunning;
	}
}
