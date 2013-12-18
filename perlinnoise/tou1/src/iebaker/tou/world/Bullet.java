package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;
import cs195n.*;

public class Bullet extends Entity {
	private Entity source_entity;

	public Bullet(Vec2f pos, Vec2f vel, Entity e) {
		super(pos, vel);
		source_entity = e;
		damage_dealt = -10;
		my_top_speed = 5;
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.BLACK);
		a.ellipse(g, my_position.x, my_position.y, 5, 5);
	}

	@Override
	public boolean onCollisionCheck(Entity e) {
		boolean cc = super.onCollisionCheck(e);
		if(cc && !(e instanceof Bullet) && e.getClass() != source_entity.getClass()) {
			this.freeFromWorld();
		}
		if(cc && e.getClass() == source_entity.getClass()) {
			return false;
		}
		return cc;
	}

	@Override
	public Shape getRepresentativeShape() {
		return new Circle(my_position.plus(new Vec2f(2.5f, 2.5f)), 2.5f);
	}

	@Override
	public void damage(float damage_amt) {
		return;
	}

	public Entity getSourceEntity() {
		return source_entity;
	}
}
