package game.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import cs195n.Vec2f;
import cs195n.Vec2i;
import engine.Application;
import engine.Saver;
import engine.Screen;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIText;
import game.GameWorld;
import game.Umbra;

/**
 * A Screen subclass supporting drawing a main menu and transitioning to gameplay
 * 
 * @author dgattey
 * 
 */
public class MainScreen extends Screen {
	
	private final UIRect	bkgrd;
	private final UIText	title;
	private final UIButton	playButton;
	private final UIButton	contButton;
	
	/**
	 * Constructor creates relevant items and places them based on ratios
	 * 
	 * @param a
	 */
	public MainScreen(Application a) {
		super(a);
		bkgrd = new UIRect(Vec2f.ZERO, Vec2f.ZERO, Color.black, new BasicStroke(0.0f));
		playButton = new UIButton("New Game", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET, GameWorld.DARK_LAVENDER,
				null, new BasicStroke(0f));
		contButton = new UIButton("Continue Game", Vec2f.ZERO, Vec2f.ZERO, GameWorld.DUSKY_VIOLET,
				GameWorld.DARK_LAVENDER, GameWorld.DUSKY_VIOLET.darker().darker(), new BasicStroke(0f));
		contButton.disable(false);
		title = new UIText(Umbra.gameName, Color.white, Vec2f.ZERO, 1);
	}
	
	/**
	 * Check for save
	 */
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		if (Saver.checkForSave(GameWorld.SAVEFILE)) {
			if (contButton.isDisabled()) contButton.enable();
		} else if (!contButton.isDisabled()) contButton.disable(false);
	}
	
	/**
	 * Draws the background and titles
	 */
	@Override
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		title.drawShape(g);
		playButton.drawShape(g);
		contButton.drawShape(g);
	}
	
	@Override
	/**
	 * Resizes all objects in the screen based on their ratios to the screen size
	 */
	protected void onResize(Vec2i newSize) {
		float w = newSize.x;
		float h = newSize.y;
		bkgrd.updatePosition(new Vec2f(0, 0), new Vec2f(w, h));
		playButton.updatePosition(new Vec2f(w / 10, 34 * (h / 80)), new Vec2f(4 * w / 7, 46 * (h / 80)));
		contButton.updatePosition(new Vec2f(w / 10, 52 * (h / 80)), new Vec2f(4 * w / 7, 64 * (h / 80)));
		title.resizeText(new Vec2f(w / 10, 16 * h / 50), 2 * h / 7);
	}
	
	/**
	 * Gets key events: if enter key released, transition to the game
	 */
	@Override
	protected void onKeyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) switchToGame("Enter key pressed", contButton.isDisabled());
	}
	
	/**
	 * Checks if the play button was clicked and if so, transitions to the game
	 */
	@Override
	protected void onMouseReleased(MouseEvent e) {
		if (playButton.hitTarget(e)) {
			switchToGame("New Button clicked", true);
		} else if (!contButton.isDisabled() && contButton.hitTarget(e)) {
			switchToGame("Continue Button clicked", false);
		}
	}
	
	/**
	 * Switches to the game by pushing on a new GameScreen, prints out the sender
	 * 
	 * @param msg
	 */
	private void switchToGame(String msg, boolean newGame) {
		if (new File(GameWorld.LEVEL_NAME).exists())
			a.pushScreen(new GameScreen(a, newGame));
		else
			a.pushScreen(new ErrorScreen(a));
	}
}
