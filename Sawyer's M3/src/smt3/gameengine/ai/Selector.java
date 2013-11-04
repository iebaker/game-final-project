package smt3.gameengine.ai;

public class Selector extends Composite {

	/**
	 * @return Status with value PASS, RUNNING, or FAIL
	 */
	@Override
	public Status update(float seconds) {
		// TODO Auto-generated method stub
		for(BTNode child : super.getChildren()) {
			Status childStatus = child.update(seconds);
			if(childStatus == Status.PASS) {
				if(super.getLastRunning() != null) {
					super.getLastRunning().reset();
				}
				super.setLastRunning(null);
				return childStatus;
			}
			if(childStatus == Status.RUNNING) {
				if(super.getLastRunning() != null) {
					super.getLastRunning().reset();
				}
				super.setLastRunning(child);
				return childStatus;
			}
		}
		return Status.FAIL;
	}
}
