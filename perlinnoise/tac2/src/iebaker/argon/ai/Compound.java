package iebaker.argon.ai;

import java.util.List;
import java.util.ArrayList;

public abstract class Compound implements BehaviorTree {
	protected List<BehaviorTree> children = new ArrayList<BehaviorTree>();

	public abstract Status update();

	public void reset() {
		for(BehaviorTree b : children) {
			b.reset();
		}
	}

	public void addChild(BehaviorTree b) {
		children.add(b);
	}
}