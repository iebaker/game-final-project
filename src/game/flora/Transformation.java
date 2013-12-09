package game.flora;

import java.io.Serializable;

public class Transformation implements Serializable {
	
	private static final long serialVersionUID = -8663637859650141079L;
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