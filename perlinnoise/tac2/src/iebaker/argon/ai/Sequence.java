package iebaker.argon.ai;

public class Sequence extends Compound {

	private int lastRunningIndex = 0;

	@Override
	public Status update() {
		for(int i = lastRunningIndex; i < children.size(); ++i) {
			BehaviorTree child = children.get(i);
			Status s = child.update();
			switch(s) {
				case SUCCESS:
					continue;
				case FAILURE:
					return s;
				case RUNNING:
					lastRunningIndex = i;
					return s;
			}
		}
		return Status.SUCCESS;
	}

	@Override
	public void reset() {
		lastRunningIndex = 0;
		super.reset();
	}
}