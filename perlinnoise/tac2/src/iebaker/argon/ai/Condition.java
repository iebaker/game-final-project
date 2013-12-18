package iebaker.argon.ai;

public abstract class Condition implements BehaviorTree {
	public abstract Status update();
	public void reset() {
		return;
	}
}