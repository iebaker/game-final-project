package game.flora;

public class Transformation {
	
	private float angle;
	private float scale;
	private float trans;

	public Transformation(float a, float s, float t) {
		this.angle = a;
		this.scale = s;
		this.trans = t;
	}

	public float getAngle() {
		return this.angle;
	}
	
	public float getScale() {
		return this.scale;
	}

	public float getTrans() {
		return this.trans;
	}
}