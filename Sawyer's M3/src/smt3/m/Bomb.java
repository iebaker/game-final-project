package smt3.m;

import java.awt.Color;

import cs195n.Vec2f;
import smt3.gameengine.physics.Circle;
import smt3.gameengine.physics.Entity;

public class Bomb extends Entity {
	
	private MWorld _mw;
	
	public Bomb(Vec2f coords, float size, MWorld world, Vec2f velocity) {
		super(new Circle(coords, size, new Color(255,0,0)), world, size * size * (float) Math.PI, velocity);
		_mw = world;
	}
	
	public void explode() {
		_mw.removeBomb(this);
		_mw.makeExplosion(this.getShape().getCenter(), 5000, 100, this);
	}
}
