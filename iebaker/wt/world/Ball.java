package iebaker.wt.world;

import cs195n.Vec2f;
import iebaker.xenon.core.Input;
import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;
import iebaker.xenon.geom.Circle;

public class Ball extends Entity implements WTEntity {
	
	private WTWorld.Color my_color;
		
	public Ball(Vec2f pos, String n) {
		super(pos, new Vec2f(0,0), 10, n);
		this.addOutputs("onTouchTarget");
		this.addInput("doTurnRed", new TurnRedInput());
		this.addInput("doTurnBlue", new TurnBlueInput());
	}

	public void onCollideWith(Entity e) {
		if(e instanceof BallTarget) getOutput("onTouchTarget").run();
	}

	private class TurnBlueInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			turnBlue();
		}
	}

	private class TurnRedInput extends Input {
		@Override public void run(java.util.Map<String, String> args) {
			turnRed();
		}
	}

	public void turnRed() {
		my_color = WTWorld.Color.RED;
	}

	public void turnBlue() {
		my_color = WTWorld.Color.BLUE;
	}

	public WTWorld.Color getColor() {
		return my_color;
	}

	public void setColor(WTWorld.Color c) {
		my_color = c;
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		if(my_color == WTWorld.Color.RED) {
			a.setFillPaint(Color.RED);
		} else if(my_color == WTWorld.Color.BLUE) {
			a.setFillPaint(Color.BLUE);
		}

		a.setStrokePaint(Color.BLACK);
		a.setStroke(true);
		super.render(a, g);
		a.setStroke(false);
	}
	
	public void jump() {
		if(my_velocity.y > -500 && jumpable) {
			this.addImpulse(new Vec2f(0, -100*my_mass));
		}
	}
}
