package iebaker.wt.world;

import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import iebaker.xenon.core.Input;
import java.awt.Graphics2D;
import java.awt.Color;

import cs195n.Vec2f;

public class LockWall extends Entity implements WTEntity {

	private WTWorld.Color my_color;

	public LockWall(Vec2f pos, String id) {
		super(pos, new Vec2f(0,0), 2f, id);
		this.fix();
		this.addInput("doRemoveSelf", new RemoveSelfInput());
	}

	private class RemoveSelfInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			if(my_world != null) freeFromWorld();
		}
	}

	public void setColor(WTWorld.Color c) {
		if(my_color == null) my_color = c;
	}

	public WTWorld.Color getColor() {
		return my_color;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		a.setFillPaint(Color.ORANGE);

		super.render(a, g);
	}
}