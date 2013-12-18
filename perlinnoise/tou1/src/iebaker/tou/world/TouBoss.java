package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.world.continuous.Polygon;
import iebaker.krypton.core.Artist;
import java.awt.Color;
import java.awt.Graphics2D;
import cs195n.Vec2f;

public class TouBoss extends Entity {
	private int gball_ticks = 350;

	public TouBoss(Vec2f pos) {
		super(pos);
		my_top_speed = 0.5f;
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.BLACK);
		a.path(g, ((Polygon)this.getRepresentativeShape()).getPoints());
	}

	@Override
	public void onTick(long nanos) {
		super.onTick(nanos);
		gball_ticks--;
	}

	@Override
	public void actOn(Entity e) {
		Vec2f towards = e.getPosition().minus(my_position);
		if(e instanceof Player) {
			Vec2f normalized = towards.isZero() ? towards : towards.normalized();
			this.accelerate(normalized);
			if(gball_ticks < 0) {
				GravityBall gb = new GravityBall(my_position.plus(normalized.smult(50)), normalized.smult(4), this);
				gb.bindToWorld(my_world);
				gball_ticks = 300;
			}
		}
	}

	@Override
	public Shape getRepresentativeShape() {
		return Polygon.regular(10, 50, my_position, 0);
	}

	@Override
	public void damage(float damage_amt) {
		return;
	}

	@Override
	public void onEdgeCheck() {	
		return;
	}

}