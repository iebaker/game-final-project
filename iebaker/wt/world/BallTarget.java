package iebaker.wt.world;

import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Input;
import iebaker.xenon.core.Artist;

import java.awt.Color;
import java.awt.Graphics2D;
import cs195n.Vec2f;

public class BallTarget extends Entity implements WTEntity {

	private boolean active = false;

	public BallTarget(Vec2f pos, String id) {
		super(pos, new Vec2f(0,0), 1f, id);
		this.fix();

		this.addOutputs("onRedFinish", "onBlueFinish");
		this.addInput("doBecomeActive", new BecomeActiveInput());
		this.addInput("doFinishRed", new FinishRedInput());
		this.addInput("doFinishBlue", new FinishBlueInput());
	}

	private class BecomeActiveInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			active = true;
		}
	}

	private class FinishRedInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			if(active) {
				getOutput("onRedFinish").run();
			}
		}
	}

	private class FinishBlueInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			if(active) {
				getOutput("onBlueFinish").run();
			}
		}
	}

	public void setColor(WTWorld.Color c) {
		return;
	}

	public WTWorld.Color getColor() {
		return WTWorld.Color.NEUTRAL;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		if(active) {
			a.setFillPaint(new Color(0.3f, 0.5f, 0.3f));
		} else {
			a.setFillPaint(new Color(0.3f, 0.3f, 0.3f));
		}

		super.render(a, g);
	}
}