package smt3.gameengine.physics;

import cs195n.Vec2f;

public class CollisionInfo {
	
	public final Vec2f mtv;
	public final Entity other;
	
	public CollisionInfo(Vec2f mtv, Entity other) {
		this.mtv = mtv;
		this.other = other;
	}	

}
