package game.entities;

import engine.Viewport;
import engine.entity.EnemyEntity;
import engine.entity.Entity;
import engine.ui.UIRoundRect;
import engine.ui.UIText;
import game.GameWorld;

import java.awt.Color;
import java.awt.Graphics2D;

import cs195n.Vec2f;

public class Tooltip {
	
	private final GameWorld		world;
	private final UIText		text;
	private final UIRoundRect	background;
	private boolean				draw;
	
	public Tooltip(GameWorld world) {
		this.world = world;
		text = new UIText("Enemy Name Here", Color.white, Vec2f.ZERO, 20f);
		background = new UIRoundRect(Vec2f.ZERO, Vec2f.ZERO, Color.black, null);
		draw = false;
	}
	
	public void setLocation(Vec2f newLocation) {
		boolean hasChanged = false;
		for (Entity e : world.getEntities()) {
			if (e instanceof EnemyEntity && e.shape.collidesPoint(newLocation)) {
				text.updateText(e.getName());
				text.updatePosition(Viewport.gamePtToScreen(newLocation), new Vec2f(0, 20f));
				float w = text.getWidth();
				background.updatePosition(Viewport.gamePtToScreen(newLocation).minus(10, 20),
						Viewport.gamePtToScreen(newLocation).plus(w + 0.3f * w + 10, 10));
				hasChanged = true;
			}
		}
		if (hasChanged) {
			draw = true;
			text.setVisible(true);
		} else {
			draw = false;
			text.setVisible(false);
		}
	}
	
	public void onDraw(Graphics2D g) {
		if (draw) {
			background.drawAndFillShape(g);
			text.drawShape(g);
		}
	}
	
}
