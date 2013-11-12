package iebaker.wt.world;

import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;

import cs195n.Vec2f;

public class Barrier extends Entity implements WTEntity {
	
	private WTWorld.Color my_color;

	public Barrier(Vec2f pos, String id) {
		super(pos, new Vec2f(0,0), 1f, id);
		this.fix();
	}

	public void setColor(WTWorld.Color c) {
		if(my_color == null) {
			my_color = c;
		}
	}

	public WTWorld.Color getColor() {
		return my_color;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		a.setFillPaint(WTWorld.toAWTColor(my_color));

		if(my_color != WTWorld.Color.NEUTRAL) {
			a.setFillPaint(WTWorld.toTransAWTColor(my_color));
		}
		super.render(a, g);
		a.setFill(true);
		a.setStroke(false);
	}

}
