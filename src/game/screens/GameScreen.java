package game.screens;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.Vec2f;
import cs195n.Vec2i;
import engine.Application;
import engine.Saver;
import engine.Screen;
import engine.Viewport;
import engine.entity.Entity;
import engine.sound.MusicPlayer;
import engine.ui.TextBox;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIRoundRect;
import engine.ui.UIText;
import game.GameWorld;
import game.MuteHolder;

/**
 * A Screen subclass supporting playing the game - creates viewport with gameview.getGame() and starts new game
 * 
 * @author dgattey
 * 
 */
public class GameScreen extends Screen {
	
	private UIRect		bkgrd;
	private Viewport	view;
	private Vec2f		mouseLocation;
	private GameWorld	game;
	private UIButton	newGame;
	private UIText		gameStatusText;
	private UIText		gameOverText;
	private UIRect		transOverlay;
	private UIRect		messageBG;
	private UIText		message;
	private UIRect		healthRect;
	private UIText		healthText;
	private TextBox textBox;
	private volatile MusicPlayer music;
	
	/**
	 * Constructor creates relevant items and places them based on ratios
	 * 
	 * @param a
	 */
	public GameScreen(Application a) {
		super(a);
		/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				if(game != null && game.getEntities() != null) {
					for(Entity e : game.getPassableEntities()) {
						e.stopSound();
					}
					for(Entity e : game.getEntities()) {
						e.stopSound();
					}
				}
			}
		}));*/
		music = new MusicPlayer("lib/equinox.wav");
		music.start();
		
		Vec2f zVec = new Vec2f(0, 0);
		try {
			LevelData data = CS195NLevelReader.readLevel(new File("lib/Level1.nlf"));
			String[] dimensions = data.getProperties().get("dimensions").split("[,]");
			this.view = new Viewport(a);
			UIRoundRect cutsceneRect = new UIRoundRect(zVec, zVec, new Color(255, 255, 255), new BasicStroke(0f));
			cutsceneRect.setVisible(false);
			UIText cutsceneText = new UIText("", new Color(0,0,0), zVec, 1);
			cutsceneText.setVisible(false);
			textBox = new TextBox(cutsceneRect, cutsceneText);
			this.game = new GameWorld(new Vec2f(Float.parseFloat(dimensions[0]), Float.parseFloat(dimensions[1])), textBox);
			this.view.setGame(game);
			this.bkgrd = new UIRect(zVec, zVec, game.getBGColor(), new BasicStroke(0f));
			this.newGame = new UIButton("New Game", zVec, zVec, new Color(0, 195, 0), Color.white,
					new BasicStroke(2.0f));
			this.gameStatusText = new UIText("Game status here", Color.white, zVec, 1);
			this.gameOverText = new UIText("Game Over", Color.white, zVec, 1);
			this.transOverlay = new UIRect(zVec, zVec, new Color(0, 0, 0, 130), new BasicStroke(0f));
			
			this.messageBG = new UIRect(zVec, zVec, new Color(0, 20, 0, 200), new BasicStroke(0f));
			this.healthRect = new UIRect(zVec, zVec, new Color(20, 0, 0, 120), new BasicStroke(0f));
			this.healthText = new UIText("Health: 100", Color.white, zVec, 1);
			
			this.message = new UIText("Game starts in 3", Color.white, zVec, 1);
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find level file!");
			e.printStackTrace();
		} catch (InvalidLevelException e) {
			System.out.println("Invalid level!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Changes game state and checks end conditions, passing onTick through to gameview.getGame() if still playing after
	 * updating score text, health text, level text, bomb count, and message if applicable
	 */
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		float secs = (float) (nanosSincePreviousTick / 1000000000.0);
		this.healthText.updateText("HP: " + (int) game.getHealth() + "/100");
		game.onTick(secs);
		if(game.isOver()) {
			GameWorld temp = (GameWorld) Saver.loadGame(GameWorld.saveFile, view, game);
			if (temp != null) {
				game = temp;
				textBox = game.getTextBox();
			}
		}
	}
	
	/**
	 * Draws the background, viewport, relevant rects and texts, and game over screen if needed
	 */
	@Override
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		view.onDraw(game, g);
		healthRect.drawAndFillShape(g);
		healthText.drawShape(g);
		textBox.draw(g);
	}
	
	/**
	 * Resizes all objects in the screen based on their ratios to the screen size
	 * 
	 * @param newSize
	 *            The new size to use for the items onscreen
	 */
	@Override
	protected void onResize(Vec2i newSize) {
		float w = newSize.x;
		float h = newSize.y;
		bkgrd.updatePosition(new Vec2f(0, 0), new Vec2f(w, h));
		Vec2f portCoord = new Vec2f(0, 0);
		Vec2f portEndCoord = new Vec2f(w, h);
		view.resizeView(portCoord, portEndCoord);
		newGame.updatePosition(new Vec2f(w / 10, 48 * (h / 80)), new Vec2f(4 * w / 7, 54 * (h / 70)));
		gameStatusText.resizeText(new Vec2f(w / 10, 2 * (h / 5)), h / 12);
		gameOverText.resizeText(new Vec2f(9 * (w / 100), 16 * (h / 50)), h / 6);
		transOverlay.updatePosition(new Vec2f(0, 0), new Vec2f(w, h));
		messageBG.updatePosition(new Vec2f(0, h / 2 - h / 10), new Vec2f(w, h / 2 + h / 10));
		message.resizeText(new Vec2f(w / 3, h / 2), h / 12);
		healthRect.updatePosition(new Vec2f(2 * w / 3, h - h / 8), new Vec2f(w, h));
		healthText.resizeText(new Vec2f(w - w / 3 + w / 60, h - h / 30), h / 14);
		textBox.getRect().updatePosition(new Vec2f(10, h - h/4), new Vec2f(w-10, h-10));
		textBox.getText().resizeText(new Vec2f(20, h - h/8), h/16);
	}
	
	/**
	 * Gets the key press and checks for escape/Q or R for regenerate, otherwise passing it through to game
	 * 
	 * @param e
	 *            The KeyEvent corresponding to the press
	 */
	@Override
	protected void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case (KeyEvent.VK_R):
			newGame(); // R pressed (new game)
			break;
		case (KeyEvent.VK_ESCAPE): // ESC pressed (quit)
		case (KeyEvent.VK_Q): // Q pressed (quit)
			for(Entity ent : game.getEntities()) {
				ent.stopSound();
			}
			for(Entity ent : game.getPassableEntities()) {
				ent.stopSound();
			}
			music.pause(true);
			a.popScreen();
			break;
		case (KeyEvent.VK_3): // 3 pressed, save game
			Saver.saveGame(GameWorld.saveFile, game);
			break;
		case (KeyEvent.VK_4): // 4 pressed, load game
			//if(!textBox.getVisible()) {
				GameWorld temp = (GameWorld) Saver.loadGame(GameWorld.saveFile, view, game);
				if (temp != null) {
					game = temp;
					textBox = game.getTextBox();
				}
			//}
			break;
		case (KeyEvent.VK_M):
			if(!MuteHolder.muted) {
				music.pause(true);
			}
			else {
				music.pause(false);
			}
			MuteHolder.muted = !MuteHolder.muted;
		default:
			game.onKeyPressed(e);
			break;
		}
	}
	
	/**
	 * Passes event through to view.getGame()
	 */
	@Override
	protected void onKeyReleased(KeyEvent e) {
		game.onKeyReleased(e);
	}
	
	/**
	 * Starting a new game if needed or passing it through to the game
	 * 
	 * @param e
	 *            The MouseEvent corresponding to the press
	 */
	@Override
	protected void onMousePressed(MouseEvent e) {
		mouseLocation = new Vec2f(e.getX(), e.getY());
		if (mouseLocation != null) {
			game.onMouseClicked(e);
		}
	}
	
	/**
	 * Gets the mouse dragged event and saves the mouselocation so it doesn't jump strangely
	 * 
	 * @param e
	 *            The MouseEvent corresponding to the drag
	 */
	@Override
	protected void onMouseDragged(MouseEvent e) {
		Vec2f m = new Vec2f(e.getX(), e.getY());
		mouseLocation = m; // important to save it again so it doesn't jump strangely!
	}
	
	/**
	 * Gets wheel movement, checks if mouse was in viewport, and zooms if so
	 * 
	 * @param e
	 *            The MouseWheelEvent representing the scroll
	 */
	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		float zm = e.getWheelRotation();
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, e.getComponent());
		Vec2f pt = new Vec2f(p.x, p.y);
		Vec2f sdim = view.getSDim();
		Vec2f dim = view.getDim();
		if (checkBounds(pt, sdim.x, dim.x, sdim.y, dim.y)) view.zoomView(pt, zm);
	}
	
	/**
	 * Begins a new game
	 */
	private void newGame() {
		game.newGame();
	}
	
	/**
	 * Checks bounds of the point p against the passed in coordinates
	 * 
	 * @param p
	 *            The point to check
	 * @param x1
	 *            The starting x
	 * @param x2
	 *            The ending x
	 * @param y1
	 *            The starting y
	 * @param y2
	 *            The ending y
	 * @return A boolean representing if p was in bounds
	 */
	private boolean checkBounds(Vec2f p, float x1, float x2, float y1, float y2) {
		return (p.x > x1 && p.x <= x2 && p.y > y1 && p.y <= y2);
	}
}
