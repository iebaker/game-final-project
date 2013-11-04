package smt3.gameengine.ai;

public interface BTNode {
	public Status update(float seconds);
	public void reset();
}
