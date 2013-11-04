package smt3.m;

import java.awt.Color;

import cs195n.Vec2f;
import smt3.gameengine.physics.Circle;

public class Explosion extends Circle {

	private long _nanosLeft;
	private MWorld _mw;
	
	public Explosion(Vec2f coords, float diameter, long nanosLeft, MWorld mw) {
		super(coords, diameter, new Color(255,0,0));
		_nanosLeft = nanosLeft;
		_mw = mw;
	}
	
	public void onTick(long nanosSinceLastTick) {
		_nanosLeft -= nanosSinceLastTick;
		if(_nanosLeft <= 0) {
			_mw.removeExplosion(this);
		}
	}

}
