package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.world.continuous.AAB;
import iebaker.krypton.world.continuous.Polygon;
import iebaker.krypton.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;
import cs195n.*;

public class ExtraBullets extends Entity {
	public ExtraBullets(Vec2f pos) {
		super(pos);
	}

	@Override 
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.GREEN);
		a.path(g, ((Polygon)this.getRepresentativeShape()).getPoints());
	}

	@Override
	public boolean onCollisionCheck(Entity e) {
		boolean cc = super.onCollisionCheck(e);
		if(cc && e instanceof Player) {
			((Player)e).addBullets(20);
			this.freeFromWorld();
		}
		return cc;	
	}

	@Override
	public Shape getRepresentativeShape() {
		return Polygon.regular(3, 20, my_position, 0.6f);
	}
}
