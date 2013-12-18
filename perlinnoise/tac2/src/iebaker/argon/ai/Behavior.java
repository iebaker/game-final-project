package iebaker.argon.ai;

public abstract class Behavior implements BehaviorTree {
	protected boolean started = false;
	public abstract Status update();
	public void reset() {
		started = false;
	}
}