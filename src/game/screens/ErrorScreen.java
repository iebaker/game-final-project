package game.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import cs195n.Vec2f;
import cs195n.Vec2i;
import engine.Application;
import engine.Screen;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIText;

public class ErrorScreen extends Screen {
	
	UIRect		bkgrd;
	UIText		title;
	UIText		body;
	UIText		body2;
	UIButton	back;
	
	public ErrorScreen(Application a) {
		super(a);
		bkgrd = new UIRect(Vec2f.ZERO, Vec2f.ZERO, Color.black, new BasicStroke(0.0f));
		back = new UIButton("Back to Menu", Vec2f.ZERO, Vec2f.ZERO, new Color(0, 195, 0), Color.white, new BasicStroke(
				2.0f));
		title = new UIText("Error", Color.white, Vec2f.ZERO, 1);
		body = new UIText("Couldn't find level file. Game could not be loaded.", Color.white, Vec2f.ZERO, 1);
		body2 = new UIText("Press any key to go back.", Color.white, Vec2f.ZERO, 1);
	}
	
	@Override
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		title.drawShape(g);
		body.drawShape(g);
		body2.drawShape(g);
		back.drawShape(g);
	}
	
	@Override
	protected void onResize(Vec2i newSize) {
		float w = newSize.x;
		float h = newSize.y;
		bkgrd.updatePosition(Vec2f.ZERO, new Vec2f(w, h));
		back.updatePosition(new Vec2f(w / 10, 58 * (h / 80)), new Vec2f(4 * w / 7, 70 * (h / 80)));
		title.resizeText(new Vec2f(w / 10, h / 4), 2 * h / 7);
		body.resizeText(new Vec2f(w / 10, 2 * h / 5), h / 20);
		body2.resizeText(new Vec2f(w / 10, 24 * h / 50), h / 20);
	}
	
	/**
	 * Gets key events: if enter key released, transition to the game
	 */
	@Override
	protected void onKeyReleased(KeyEvent e) {
		a.popScreen();
	}
	
	/**
	 * Checks if the play button was clicked and if so, transitions to the game
	 */
	@Override
	protected void onMouseReleased(MouseEvent e) {
		if (back.hitTarget(e)) {
			a.popScreen();
		}
	}
	
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		
	}
	
}
