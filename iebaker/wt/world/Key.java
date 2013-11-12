package iebaker.wt.world;

import cs195n.Vec2f;
import iebaker.xenon.core.Input;
import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;

public class Key extends Entity implements WTEntity {
	private WTWorld.Color my_color;

	public Key(Vec2f pos, String id) {
		super(pos, new Vec2f(0,0), 1f, id);
		this.fix();
		this.addOutputs("onTouched");
	}

	public void onCollideWith(Entity e) {
		if(e instanceof Player) getOutput("onTouched").run();
	}

	public WTWorld.Color getColor() {
		return my_color;
	}

	public void setColor(WTWorld.Color c) {
		if(my_color == null) my_color = c;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		a.setFillPaint(Color.ORANGE);
		super.render(a, g);
	}
}