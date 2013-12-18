package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.world.continuous.AAB;
import iebaker.krypton.world.continuous.Polygon;
import iebaker.krypton.world.continuous.CompoundShape;
import iebaker.krypton.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;
import cs195n.*;

public class ExtraHealth extends Entity {
	public ExtraHealth(Vec2f pos) {
		super(pos);
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(new Color(1f,0f,1f));
		a.path(g, ((Polygon)this.getRepresentativeShape()).getPoints());
	}

	@Override
	public boolean onCollisionCheck(Entity e) {
		boolean cc = super.onCollisionCheck(e);
		if(cc && e instanceof Player) {
			((Player)e).addHealth(50);
			this.freeFromWorld();
		}
		return cc;
	}

	@Override
	public Shape getRepresentativeShape() {
		return Polygon.regular(7, 20, my_position, 0.1f);
	}

	@Override
	public void damage(float damage_amt) {
		return;
	}
}
