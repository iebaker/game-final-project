package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.core.Artist;
import java.awt.Color;
import java.awt.Graphics2D;
import cs195n.Vec2f;

public class GravityBall extends Entity {
	private Entity source_entity;
	public GravityBall(Vec2f pos, Vec2f vel, Entity e) {
		super(pos, vel);
		source_entity = e;
		my_top_speed = 4;
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.GREEN);
		a.ellipse(g, my_position.x, my_position.y, 15, 15);
	}

	@Override
	public Shape getRepresentativeShape() {
		return new Circle(my_position, 15);
	}

	@Override
	public void actOn(Entity e) {
		if(e instanceof Bullet) {
			Vec2f towards = my_position.minus(e.getPosition());
			float mag = (float) Math.sqrt(towards.mag());
			Vec2f normalized = towards.isZero() ? new Vec2f(0, 0) : towards.normalized();
			normalized = normalized.smult(mag * 2);
			e.accelerate(normalized);
		}
	}


}
