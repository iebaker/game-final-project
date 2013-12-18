package iebaker.argon.ai;

public interface BehaviorTree {
	public Status update();
	public void reset();
}