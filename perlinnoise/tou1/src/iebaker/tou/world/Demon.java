package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.core.Artist;
import java.awt.Color;
import java.awt.Graphics2D;
import cs195n.Vec2f;

public class Demon extends Entity {
	private int ticks = 100;

	public Demon(Vec2f pos) {
		super(pos);
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.RED);
		a.ellipse(g, my_position.x, my_position.y, 10, 10);
		a.setFillPaint(Color.BLACK);
		a.text(g, health + "", my_position.x + 15, my_position.y + 15);
	}

	@Override 
	public void onTick(long nanos) {
		super.onTick(nanos);
		ticks--;
	}

	@Override
	public Shape getRepresentativeShape() {
		return new Circle(my_position.plus(new Vec2f(5f, 5f)), 5f);
	}

	@Override 
	public void actOn(Entity e) {
		Vec2f towards = e.getPosition().minus(my_position);
		if(ticks <= 0 && e instanceof Player) {
			Vec2f normalized = towards.isZero() ? new Vec2f(0,0) : towards.normalized();
			Bullet b = new Bullet(my_position.plus(new Vec2f(5f, 5f)).plus(normalized.smult(10)), normalized, this);
			b.bindToWorld(my_world);
			ticks = 100;
		}
	}
}		
