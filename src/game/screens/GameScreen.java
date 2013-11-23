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
import engine.ui.TextBox;
import engine.ui.UIButton;
import engine.ui.UIRect;
import engine.ui.UIRoundRect;
import engine.ui.UIText;
import game.GameWorld;
import game.GameWorld.GameState;

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
	private GameState	gameStatus;
	private UIText		gameOverText;
	private UIRect		transOverlay;
	private UIRect		messageBG;
	private UIText		message;
	private UIRect		healthRect;
	private UIText		healthText;
	private UIRect		levelRect;
	private UIText		levelText;
	private boolean		gameOver;
	private TextBox textBox;
	
	/**
	 * Constructor creates relevant items and places them based on ratios
	 * 
	 * @param a
	 */
	public GameScreen(Application a) {
		super(a);
		/*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if(game != null && game.getEntities() != null) {
					for(Entity e : game.getEntities()) {
						e.stopSound();
					}
				}
			}
		}));*/
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
			this.gameStatus = GameState.PLAYING;
			this.gameStatusText = new UIText("Game status here", Color.white, zVec, 1);
			this.gameOverText = new UIText("Game Over", Color.white, zVec, 1);
			this.transOverlay = new UIRect(zVec, zVec, new Color(0, 0, 0, 130), new BasicStroke(0f));
			
			this.messageBG = new UIRect(zVec, zVec, new Color(0, 20, 0, 200), new BasicStroke(0f));
			this.healthRect = new UIRect(zVec, zVec, new Color(20, 0, 0, 120), new BasicStroke(0f));
			this.healthText = new UIText("Health: 100", Color.white, zVec, 1);
			this.levelRect = new UIRect(zVec, zVec, new Color(0, 0, 20, 120), new BasicStroke(0f));
			this.levelText = new UIText("Level 1", Color.white, zVec, 1);
			
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
	protected void onTick(long nanosSincePreviousTick) {
		GameState state = game.checkEndConditions();
		if (gameOver)
			return;
		else if (state != GameState.PLAYING && !gameOver)
			gameOver(state);
		else {
			this.message.updateText(game.getCurrentMessage());
			float secs = (float) (nanosSincePreviousTick / 1000000000.0);
			this.healthText.updateText("HP: " + (int) game.getHealth() + "/100");
			this.levelText.updateText("Level " + game.level.getLevel());
			game.onTick(secs);
		}
	}
	
	/**
	 * Shows game over stuff
	 * 
	 * @param state
	 */
	private void gameOver(GameState state) {
		gameStatus = state;
		gameStatusText.updateText(gameStatus.getMessage());
		gameOverText.updateText(gameStatus.getHeadline());
		message.updateText("");
		int hp = (game.getHealth() < 0) ? 0 : (int) game.getHealth();
		healthText.updateText("HP: " + hp + "/100");
		
		gameOver = true; // stops more updates from getting through
	}
	
	/**
	 * Draws the background, viewport, relevant rects and texts, and game over screen if needed
	 */
	protected void onDraw(Graphics2D g) {
		bkgrd.drawAndFillShape(g);
		view.onDraw(game, g);
		if (!message.isEmpty()) {
			messageBG.drawAndFillShape(g);
			message.drawShape(g);
		}
		healthRect.drawAndFillShape(g);
		healthText.drawShape(g);
		levelRect.drawAndFillShape(g);
		levelText.drawShape(g);
		if (gameStatus != GameState.PLAYING) {
			transOverlay.drawAndFillShape(g);
			gameStatusText.drawShape(g);
			gameOverText.drawShape(g);
			newGame.drawShape(g);
		}
		textBox.draw(g);
	}
	
	/**
	 * Resizes all objects in the screen based on their ratios to the screen size
	 * 
	 * @param newSize
	 *            The new size to use for the items onscreen
	 */
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
		levelRect.updatePosition(new Vec2f(0, 0), new Vec2f(w / 3, h / 8));
		levelText.resizeText(new Vec2f(w / 60, h / 10), h / 8);
		textBox.getRect().updatePosition(new Vec2f(10, h - h/4), new Vec2f(w-10, h-10));
		textBox.getText().resizeText(new Vec2f(20, h - h/8), h/16);
	}
	
	/**
	 * Gets the key press and checks for escape/Q or R for regenerate, otherwise passing it through to game
	 * 
	 * @param e
	 *            The KeyEvent corresponding to the press
	 */
	protected void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case (KeyEvent.VK_R):
			newGame(); // R pressed (new game)
			break;
		case (KeyEvent.VK_ESCAPE): // ESC pressed (quit)
		case (KeyEvent.VK_Q): // Q pressed (quit)
			for(Entity ent :game.getEntities()) {
				ent.stopSound();
			}
			a.popScreen();
			break;
		case (KeyEvent.VK_3): // 3 pressed, save game
			Saver.saveGame(System.getProperty("user.home") + "/save.gme", game);
			break;
		case (KeyEvent.VK_4): // 4 pressed, load game
			//if(!textBox.getVisible()) {
				GameWorld temp = (GameWorld) Saver.loadGame(System.getProperty("user.home") + "/save.gme", view, game);
				if (temp != null) {
					game = temp;
					textBox = game.getTextBox();
				}
			//}
			break;
		default:
			game.onKeyPressed(e);
			break;
		}
	}
	
	/**
	 * Passes event through to view.getGame()
	 */
	protected void onKeyReleased(KeyEvent e) {
		game.onKeyReleased(e);
	}
	
	/**
	 * Starting a new game if needed or passing it through to the game
	 * 
	 * @param e
	 *            The MouseEvent corresponding to the press
	 */
	protected void onMousePressed(MouseEvent e) {
		mouseLocation = new Vec2f(e.getX(), e.getY());
		if (gameStatus != GameState.PLAYING && newGame.hitTarget(e)) {
			newGame();
		} else if (gameStatus == GameState.PLAYING && mouseLocation != null) {
			game.onMouseClicked(e);
		}
	}
	
	/**
	 * Gets the mouse dragged event and saves the mouselocation so it doesn't jump strangely
	 * 
	 * @param e
	 *            The MouseEvent corresponding to the drag
	 */
	protected void onMouseDragged(MouseEvent e) {
		if (gameStatus == GameState.PLAYING) {
			Vec2f m = new Vec2f(e.getX(), e.getY());
			mouseLocation = m; // important to save it again so it doesn't jump strangely!
		}
	}
	
	/**
	 * Gets wheel movement, checks if mouse was in viewport, and zooms if so
	 * 
	 * @param e
	 *            The MouseWheelEvent representing the scroll
	 */
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		if (gameStatus == GameState.PLAYING) {
			float zm = e.getWheelRotation();
			Point p = MouseInfo.getPointerInfo().getLocation();
			SwingUtilities.convertPointFromScreen(p, e.getComponent());
			Vec2f pt = new Vec2f(p.x, p.y);
			Vec2f sdim = view.getSDim();
			Vec2f dim = view.getDim();
			if (checkBounds(pt, sdim.x, dim.x, sdim.y, dim.y)) view.zoomView(pt, zm);
		}
	}
	
	/**
	 * Begins a new game
	 */
	private void newGame() {
		gameStatus = GameState.PLAYING;
		gameOver = false;
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
