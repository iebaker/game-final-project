package game.flora;

import java.io.Serializable;

public class Transformation implements Serializable {
	
	private static final long serialVersionUID = -8663637859650141079L;
	private float angle;
	private float scale;
	private float trans;

	public static final float PI = (float) Math.PI;
	public static final float HALF_PI = (float) Math.PI/2;
	public static final float THIRD_PI = (float) Math.PI/3;
	public static final float FOURTH_PI = (float) Math.PI/4;
	public static final float FIFTH_PI = (float) Math.PI/5;
	public static final float SIXTH_PI = (float) Math.PI/6;
	public static final float SEVENTH_PI = (float) Math.PI/7;
	public static final float EIGHTH_PI = (float) Math.PI/8;

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