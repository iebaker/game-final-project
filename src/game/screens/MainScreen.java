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

/**
 * A Screen subclass supporting drawing a main menu and transitioning to gameplay
 * 
 * @author dgattey
 * 
 */
public class MainScreen extends Screen {
	
	UIRect		bkgrd;
	UIText		title;
	UIButton	playButton;
	
	/**
	 * Constructor creates relevant items and places them based on ratios
	 * 
	 * @param a
	 */
	public MainScreen(Application a) {
		super(a);
		Vec2f zVec = new Vec2f(0, 0);
		this.bkgrd = new UIRect(zVec, zVec, Color.black, new BasicStroke(0.0f));
		this.playButton = new UIButton("New Game", zVec, zVec, new Color(0, 195, 0), Color.white, new BasicStroke(2.0f));
		this.title = new UIText("M", Color.white, zVec, 1);
	}
	
	/**
	 * Nothing happens
	 */
	protected void onTick(long nanosSincePreviousTick) {
		
	}
	
	/**
	 * Draws the background and titles
	 */
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		title.drawShape(g);
		playButton.drawShape(g);
	}
	
	@Override
	/**
	 * Resizes all objects in the screen based on their ratios to the screen size
	 */
	protected void onResize(Vec2i newSize) {
		float w = newSize.x;
		float h = newSize.y;
		bkgrd.updatePosition(new Vec2f(0, 0), new Vec2f(w, h));
		playButton.updatePosition(new Vec2f(w / 10, 42 * (h / 80)), new Vec2f(4 * w / 7, 49 * (h / 70)));
		title.resizeText(new Vec2f(w / 10, 2 * h / 5), 2 * h / 7);
	}
	
	/**
	 * Gets key events: if enter key released, transition to the game
	 */
	protected void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == 10) switchToGame("Enter key pressed");
	}
	
	/**
	 * Checks if the play button was clicked and if so, transitions to the game
	 */
	protected void onMouseReleased(MouseEvent e) {
		if (playButton.hitTarget(e)) {
			switchToGame("Button clicked");
		}
	}
	
	/**
	 * Switches to the game by pushing on a new GameScreen, prints out the sender
	 * 
	 * @param msg
	 */
	private void switchToGame(String msg) {
		a.pushScreen(new GameScreen(a));
		System.out.println(msg + ", transitioning to game screen");
	}
}