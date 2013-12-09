package engine.entity;

public class PassableEntity extends SensorEntity {
	
	private static final long	serialVersionUID	= -7382617960447661170L;
	protected boolean canCollide = true;

	public boolean canCollide() {
		return canCollide;
	}
	
}
