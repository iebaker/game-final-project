package iebaker.argon.ai;

public class Selector extends Compound {
	@Override
	public Status update() {
		boolean allfailed = true;
		for(BehaviorTree child : children) {
			Status s = child.update();
			switch(s) {
				case SUCCESS:
					allfailed = false;
					continue;
				case RUNNING:
					return s;
			}
		}
		if(allfailed) return Status.FAILURE;
		return Status.SUCCESS;
	}
}