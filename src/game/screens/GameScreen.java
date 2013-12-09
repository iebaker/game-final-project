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
import game.entities.Player;

/**
 * A Screen subclass supporting playing the game - creates viewport with gameview.getGame() and starts new game
 * 
 * @author dgattey
 * 
 */
public class GameScreen extends Screen {
	
	private final UIRect			bkgrd;
	private final Viewport			view;
	private Vec2f					mouseLocation;
	private GameWorld				game;
	private final UIButton			newGame;
	private final UIText			gameStatusText;
	private final UIText			gameOverText;
	private final UIRect			transOverlay;
	private final UIRect			messageBG;
	private final UIText			message;
	private final UIRect			healthRect;
	private final UIText			healthText;
	private final UIRect			crystalRect;
	private final UIText			crystalText;
	private TextBox					textBox;
	private volatile MusicPlayer	music;
	private float					fadeCount	= 0;
	private final UIRect			fadeRect;
	
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
		if(MuteHolder.muted) {
			music.pause(true);
		}
		view = new Viewport(a);
		UIRoundRect cutsceneRect = new UIRoundRect(Vec2f.ZERO, Vec2f.ZERO, new Color(255, 255, 255),
				new BasicStroke(0f));
		cutsceneRect.setVisible(false);
		UIText cutsceneText = new UIText("", new Color(0, 0, 0), Vec2f.ZERO, 1);
		cutsceneText.setVisible(false);
		textBox = new TextBox(cutsceneRect, cutsceneText);
		try {
			LevelData data = CS195NLevelReader.readLevel(new File("lib/Level1.nlf"));
			String[] dimensions = data.getProperties().get("dimensions").split("[,]");
			game = new GameWorld(new Vec2f(Float.parseFloat(dimensions[0]), Float.parseFloat(dimensions[1])), textBox);
			
		} catch (FileNotFoundException e) {
			System.err.println("Could not locate level file");
		} catch (InvalidLevelException e) {
			System.err.println("Invalid level file");
		}
		if(game != null) view.setGame(game);
		bkgrd = new UIRect(Vec2f.ZERO, Vec2f.ZERO, (game != null) ? game.getBGColor() : Color.black,
				new BasicStroke(0f));
		newGame = new UIButton("New Game", Vec2f.ZERO, Vec2f.ZERO, new Color(0, 195, 0), Color.white, null,
				new BasicStroke(2.0f));
		gameStatusText = new UIText("Game status here", Color.white, Vec2f.ZERO, 1);
		gameOverText = new UIText("Game Over", Color.white, Vec2f.ZERO, 1);
		transOverlay = new UIRect(Vec2f.ZERO, Vec2f.ZERO, new Color(0, 0, 0, 130), new BasicStroke(0f));
		
		messageBG = new UIRect(Vec2f.ZERO, Vec2f.ZERO, new Color(0, 20, 0, 200), new BasicStroke(0f));
		healthRect = new UIRect(Vec2f.ZERO, Vec2f.ZERO, new Color(20, 0, 0, 120), new BasicStroke(0f));
		healthText = new UIText("Health: 100", Color.white, Vec2f.ZERO, 1);
		
		crystalRect = new UIRect(Vec2f.ZERO, Vec2f.ZERO, new Color(20, 0, 0, 120), new BasicStroke(0f));
		crystalText = new UIText("Crystals: 0", Color.white, Vec2f.ZERO, 1);
		
		fadeRect = new UIRect(Vec2f.ZERO, Vec2f.ZERO, new Color(0, 0, 0, 0), new BasicStroke(0f));
		
		message = new UIText("Game starts in 3", Color.white, Vec2f.ZERO, 1);
		fadeIn();
	}
	
	/**
	 * Changes game state and checks end conditions, passing onTick through to gameview.getGame() if still playing after
	 * updating score text, health text, level text, bomb count, and message if applicable
	 */
	@Override
	protected void onTick(long nanosSincePreviousTick) {
		float secs = (float) (nanosSincePreviousTick / 1000000000.0);
		if(fadeCount > 0) {
			fadeCount -= secs * 100;
			if(fadeCount < 0) {
				fadeCount = 0;
			}
			fadeRect.changeColor(new Color(0, 0, 0, (int) fadeCount));
		}
		if(game != null) {
			healthText.updateText("Light: " + (int) game.getHealth() + "/100");
			Player p = (Player) game.getPlayer();
			crystalText.updateText("Crystals: " + ((p == null) ? 0 : p.getCrystals()));
			game.onTick(secs);
			if(game.isOver()) {
				GameWorld temp = (GameWorld) Saver.loadGame(GameWorld.saveFile, view, game);
				die();
				if(temp != null) {
					game = temp;
					textBox = game.getTextBox();
				}
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
		crystalRect.drawAndFillShape(g);
		crystalText.drawShape(g);
		textBox.draw(g);
		if(fadeCount != 0) {
			fadeRect.drawAndFillShape(g);
		}
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
		fadeRect.updatePosition(new Vec2f(0, 0), new Vec2f(w, h));
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
		crystalRect.updatePosition(new Vec2f(2 * w / 3, 0), new Vec2f(w, h / 8));
		crystalText.resizeText(new Vec2f(w - w / 3 + w / 60, h / 14), 2 * h / 30);
		textBox.getRect().updatePosition(new Vec2f(10, h - h / 4), new Vec2f(w - 10, h - 10));
		textBox.getText().resizeText(new Vec2f(20, h - h / 8), h / 16);
	}
	
	/**
	 * Gets the key press and checks for escape/Q or R for regenerate, otherwise passing it through to game
	 * 
	 * @param e
	 *            The KeyEvent corresponding to the press
	 */
	@Override
	protected void onKeyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case (KeyEvent.VK_R):
			fadeIn();
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
			// if(!textBox.getVisible()) {
			GameWorld temp = (GameWorld) Saver.loadGame(GameWorld.saveFile, view, game);
			if(temp != null) {
				fadeIn();
				game = temp;
				textBox = game.getTextBox();
			}
			// }
			break;
		case (KeyEvent.VK_5): // 5, load upgrades
			ShopScreen shop = new ShopScreen(a);
			shop.setWorld(game);
			a.pushScreen(shop);
			break;
		case (KeyEvent.VK_M):
			if(!MuteHolder.muted) {
				music.pause(true);
				for(Entity ent : game.getEntities()) {
					ent.stopSound();
				}
				for(Entity ent : game.getPassableEntities()) {
					ent.stopSound();
				}
			} else {
				music.pause(false);
				for(Entity ent : game.getEntities()) {
					ent.startSound();
				}
				for(Entity ent : game.getPassableEntities()) {
					ent.startSound();
				}
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
		if(mouseLocation != null) {
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
		if(checkBounds(pt, sdim.x, dim.x, sdim.y, dim.y)) view.zoomView(pt, zm);
	}
	
	@Override
	protected void onMouseMoved(MouseEvent e) {
		game.onMouseMoved(e);
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
	
	private void fadeIn() {
		fadeCount = 255;
		fadeRect.changeColor(new Color(0, 0, 0, 255));
	}
	
	private void die() {
		fadeIn();
	}
}
