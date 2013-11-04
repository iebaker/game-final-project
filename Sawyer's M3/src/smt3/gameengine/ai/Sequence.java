package smt3.gameengine.ai;

public class Sequence extends Composite {


	/**
	 * @return Status representing the current state of the sequence
	 */
	@Override
	public Status update(float seconds) {
		// TODO Auto-generated method stub
		for(BTNode child : super.getChildren()) {
			if(super.getLastRunning() != null) {
				if(super.getLastRunning() == child) {
					super.setLastRunning(null);
				}
			}
			
			if(super.getLastRunning() == null) {
				Status childStatus = child.update(seconds);
				if(childStatus == Status.FAIL) {
					return childStatus;
				}
				if(childStatus == Status.RUNNING) {
					super.setLastRunning(child);
					return childStatus;
				}
			}
		}
		return Status.PASS;
	}
}
