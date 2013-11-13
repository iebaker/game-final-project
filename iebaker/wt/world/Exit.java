package iebaker.wt.world;

import cs195n.Vec2f;
import iebaker.xenon.core.Input;
import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;

public class Exit extends Entity implements WTEntity {
	public Exit(Vec2f pos, String n) {
		super(pos, new Vec2f(0,0), 1f, n);
		this.fix();

		this.addInput("doRecordRedFinish", new RecordRedFinishInput());
		this.addInput("doRecordBlueFinish", new RecordBlueFinishInput());
	}

	private class RecordRedFinishInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			WTWorld wtw = (WTWorld) my_world;
			wtw.recordRedFinish();
		}
	}

	private class RecordBlueFinishInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			WTWorld wtw = (WTWorld) my_world;
			wtw.recordBlueFinish();
		}
	}

	public WTWorld.Color getColor() {
		return WTWorld.Color.NEUTRAL;
	}

	public void setColor(WTWorld.Color c) {
		return;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		a.setFillPaint(a.makeTexturePaint("lib/img/wood.png"));
		a.setStroke(true);
		super.render(a, g);
		a.setStroke(false);
	}
}
