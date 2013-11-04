package smt3.m;

import java.awt.Color;

import cs195n.Vec2f;
import smt3.gameengine.other.World;
import smt3.gameengine.physics.Entity;
import smt3.gameengine.physics.Rectangle;

public class Bound extends Entity {

	public Bound(Vec2f coords, Vec2f dims, World world, Color c, float restitution) {
		super(new Rectangle(coords, dims, c), world, 1, restitution);
		this.setStatic(true);
	}
}
