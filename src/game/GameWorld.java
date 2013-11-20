package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Timer;

import cs195n.Vec2f;
import engine.World;
import engine.collision.CollisionInfo;
import engine.collision.Ray;
import engine.entity.EnemyEntity;
import engine.entity.Entity;
import engine.entity.RelayEntity;
import engine.entity.SensorEntity;
import engine.entity.StaticEntity;
import engine.ui.TextBox;

/**
 * GameWorld for M
 * 
 * @author dgattey
 */
public class GameWorld extends World {
	
	private static final long							serialVersionUID	= 6619354971290257104L;
	
	private static final float							TICK_LENGTH			= 0.005f;
	public Level										level;
	private String										message;
	private float										countdown;
	private boolean										paused;
	private boolean										cutsceneActive;
	private boolean										textChangeReady;
	private double										leftoverTime;
	private Vec2f										line;
	private int											lineCt;
	private boolean										win;
	private boolean										lose;
	private float										gravity;
	private int											numLevels;
	private HashMap<String, Class<? extends Entity>>	classes;
	private float										hp;
	private Player										player;
	private String										soundFile			= "sounds.xml";
	
	/**
	 * Constructor for a world that starts a new game
	 * 
	 * @param dim
	 * @param tb
	 */
	public GameWorld(Vec2f dim, TextBox tb) {
		super(dim, tb);
		numLevels = 2;
		textBox = tb;
		tb.setWorld(this);
		// Classes map
		classes = new HashMap<String, Class<? extends Entity>>();
		classes.put("PlayerEntity", Player.class);
		classes.put("StaticEntity", StaticEntity.class);
		classes.put("EnemyEntity", EnemyEntity.class);
		classes.put("Entity", Entity.class);
		classes.put("Sensor", SensorEntity.class);
		classes.put("Relay", RelayEntity.class);
		classes.put("WinEntity", WinEntity.class);
		
		newGame();
	}
	
	@Override
	public void newGame() {
		hp = 0;
		newGame(1);
	}
	
	/**
	 * Begins a new game, making a new level, setting a countdown message for when the game starts, and loading the
	 * level from file
	 * 
	 * @param restitution
	 *            the restitution to give all entities
	 */
	public void newGame(int lvl) {
		if (this.getEntities() != null) {
			for (Entity e : this.getEntities()) {
				e.stopSound();
			}
		}
		player = null;
		level = new Level(lvl, 0f);
		if (v != null) v.viewHasChanged(true);
		leftoverTime = 0;
		line = null;
		win = false;
		lose = false;
		lineCt = 0;
		gravity = 300;
		entityStack = new ArrayList<Entity>();
		
		// Actually load the level
		loadLevelFromFile("lib/Level" + lvl + ".nlf", classes, this);
		if (lvl == 1) setMessage("Game starts in 3", 0.5f);
		
		textBox.setVisible(false);
		cutsceneActive = false;
	}
	
	/**
	 * Set the message for the message display with a countdown or just pause the game
	 * 
	 * @param msg
	 * @param ct
	 */
	private void setMessage(String msg, float ct) {
		message = msg;
		if (ct > 0)
			countdown = ct;
		else if (ct == 0) paused = true;
	}
	
	@Override
	/**
	 * Draws the entities and a line for the bullet if needed
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		// Resets viewport to be the correct offset based on player location
		if (v != null && player != null)
			this.v.setOffset(player.shape.getLocation().minus(v.screenPtToGameForOffset(v.getDim().sdiv(2))));
		
		// Draws the line for bullets
		if (line != null && player != null && lineCt < 20) {
			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(5f));
			Vec2f p1 = v.gamePtToScreen(player.shape.getCenter());
			Vec2f p2 = v.gamePtToScreen(line);
			g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
			lineCt++;
		} else if (lineCt >= 20) {
			lineCt = 0;
			line = null;
		}
		
		super.onDraw(g); // draws all entities
	}
	
	/**
	 * Does a lot of stuff every tick - see breakdown comments in code below - but splits tick into multiple iterations
	 * of 0.005 seconds for consistency
	 */
	@Override
	public void onTick(float secs) {
		
		// Calculates standard tick - how many + leftover time to counter for later
		double timeSteps = (secs / TICK_LENGTH) + leftoverTime;
		long steps = (long) timeSteps;
		leftoverTime = timeSteps - steps;
		for (int i = 0; i < steps; i++) {
			
			// Updates message while there's a countdown
			if (countdown > 1) {
				setMessage(message.subSequence(0, message.length() - 1).toString() + (int) countdown, countdown
						- TICK_LENGTH);
			}
			
			// Shows paused message
			else if (paused)
				message = "Game paused";
			
			// Ticks through each entity and checks collisions
			else if (!cutsceneActive) {
				message = "";
				super.onTick(TICK_LENGTH);
				checkCollisions();
				level.onTick(TICK_LENGTH);
				// System.out.println("Ticking " + System.currentTimeMillis());
			}
		}
	}
	
	/**
	 * Checks collisions by way of double iteration through all elements - does special things for certain entities
	 */
	public void checkCollisions() {
		for (int i = 0; i < entityStack.size(); i++) {
			for (int j = i + 1; j < entityStack.size(); j++) {
				Entity a = entityStack.get(i);
				Entity b = entityStack.get(j);
				
				if (a instanceof EnemyEntity && b instanceof Player && a.collideWithEntity(b)) {
					b.hp -= ((EnemyEntity) a).getDamage();
				} else if (b instanceof EnemyEntity && a instanceof Player && b.collideWithEntity(a)) {
					a.hp -= ((EnemyEntity) b).getDamage();
				}
				if (a.collideWithEntity(b)) {
					CollisionInfo aCol = new CollisionInfo(a, b);
					if (aCol.mtv != null && !aCol.mtv.isZero()) a.onCollide(aCol);
					a.afterCollision();
					b.afterCollision();
				} else if (b.collideWithEntity(a)) {
					a.afterCollision();
					b.afterCollision();
				}
				
			}
		}
	}
	
	/**
	 * Fires a bullet from the player to dest (mouse location)
	 * 
	 * @param dest
	 *            the destination of the ray
	 */
	private void fireBullet(Vec2f dest) {
		if (!paused && !cutsceneActive && player != null) {
			Vec2f src = player.shape.getCenter();
			Ray ray = new Ray(src, dest);
			Entity affected = null;
			Vec2f castTo = null;
			for (Entity e : entityStack) {
				if (!e.equals(player) && e.shape != null) {
					Vec2f tmp = e.shape.cast(ray);
					if (castTo == null
							|| (castTo != null && tmp != null && tmp.minus(src).mag() < castTo.minus(src).mag())) {
						affected = e;
						castTo = tmp;
					}
				}
			}
			if (castTo != null) {
				affected.applyImpulse(ray.getDirection().smult(5000f));
				
				// For breakable blocks
				if (affected.isShootable()) {
					affected.setShotsNeeded(affected.getShotsNeeded() - 1);
					if (affected.getShotsNeeded() < 1) removeEntity(affected);
				}
				line = castTo;
			}
		}
	}
	
	/**
	 * Checks the game state - win after level 5 - but could be extended!
	 * 
	 * @return
	 */
	public GameState checkEndConditions() {
		if (lose)
			return GameState.LOSE;
		else if (win)
			return GameState.WIN;
		else
			return GameState.PLAYING;
	}
	
	@Override
	/**
	 * Sets a win if over, or goes to next level if there's one more
	 */
	public void setWin() {
		if (level.getLevel() < numLevels) {
			hp = player.hp;
			setMessage("Level " + (level.getLevel() + 1) + " starts in 3", 3.5f);
			newGame(level.getLevel() + 1);
		} else
			win = true;
	}
	
	@Override
	/**
	 * Sets a lose with a message
	 */
	public void setLose(String msg) {
		lose = true;
		if (player.hp < 0) player.hp = 0;
		GameState.LOSE.setMessage(msg);
		player = null;
	}
	
	/**
	 * Responder for mouse release - shoots bullet to mouse location with left button if in game and shoots grenade if
	 * right button or ctrl-left mouse button
	 * 
	 * @param e
	 */
	public void onMouseClicked(MouseEvent e) {
		Vec2f pt = v.screenPtToGame(new Vec2f(e.getX(), e.getY()));
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (checkBounds(pt)) fireBullet(pt);
		}
	}
	
	/**
	 * Responders for key presses
	 * 
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (textChangeReady
				&& (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S)) {
			textChangeReady = false;
			if (textBox.hasNextLine()) {
				final Timer t = new Timer(100, null);
				t.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						GameWorld.this.textChangeReady = true;
						t.removeActionListener(this);
					}
				});
				t.start();
				textBox.displayNext();
			} else {
				cutsceneActive = false;
				textBox.setVisible(false);
			}
		}
		switch (keyCode) {
		case (KeyEvent.VK_1): // 1 - Load level 1
			if (player != null) {
				newGame(1);
			}
			break;
		case (KeyEvent.VK_2): // 2 - Load level 2
			if (player != null) {
				newGame(2);
			}
			break;
		case (KeyEvent.VK_P): // Pause
			if (player != null && !lose && !win)
				paused = !paused;
			else
				paused = false;
			break;
		case (KeyEvent.VK_W): // W
		case (KeyEvent.VK_SPACE): // Jump
			if (player != null && !lose && !win && player.canJump()) {
				player.jump();
			}
			break;
		case (KeyEvent.VK_A): // A
		case (KeyEvent.VK_LEFT): // Left
			if (player != null && !lose && !win) player.goalVelocity = new Vec2f(-800, 0);
			break;
		case (KeyEvent.VK_D): // D
		case (KeyEvent.VK_RIGHT): // Right
			if (player != null && !lose && !win) player.goalVelocity = new Vec2f(800, 0);
			break;
		default:
			break;
		}
	}
	
	/**
	 * Resets goal velocity when key released
	 * 
	 * @param e
	 */
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case (KeyEvent.VK_LEFT): // left
		case (KeyEvent.VK_RIGHT): // Right
		case (KeyEvent.VK_A): // A
		case (KeyEvent.VK_D): // D
			if (player != null && !lose && !win) player.goalVelocity = new Vec2f(0, 0);
			break;
		default:
			break;
		}
	}
	
	@Override
	/**
	 * Returns the gravity value for this world (-x is up, x is down)
	 */
	public float gravity() {
		return gravity;
	}
	
	@Override
	public void flipGravity() {
		this.gravity = -gravity;
	}
	
	/**
	 * Public getter for health
	 * 
	 * @return
	 */
	public float getHealth() {
		if (player == null)
			return 100;
		else
			return player.hp;
	}
	
	/**
	 * Public getter for message
	 * 
	 * @return
	 */
	public String getCurrentMessage() {
		return message;
	}
	
	@Override
	public Entity getPlayer() {
		return player;
	}
	
	@Override
	public void setPlayer(Entity p) {
		this.player = (Player) p;
		if (hp > 0) player.hp = hp;
	}
	
	@Override
	public String getSoundFile() {
		return soundFile;
	}
	
	/**
	 * Enum for game state that displays a message
	 * 
	 * @author dgattey
	 * 
	 */
	public static enum GameState {
		WIN("Nice Work!", "You won"), LOSE("Game Over", "You lost... Try again"), PLAYING("...", "Still in play");
		
		private String			message;
		private final String	headline;
		
		/**
		 * Private constructor with headline and message
		 * 
		 * @param par
		 * @param msg
		 */
		private GameState(String head, String msg) {
			this.message = msg;
			this.headline = head;
		}
		
		/**
		 * Public getter for the message
		 * 
		 * @return
		 */
		public String getMessage() {
			return this.message;
		}
		
		/**
		 * Public setter for the message
		 * 
		 * @param msg
		 *            the message to set
		 */
		public void setMessage(String msg) {
			this.message = msg;
		}
		
		/**
		 * Public getter for the headline
		 * 
		 * @return
		 */
		public String getHeadline() {
			return this.headline;
		}
	}
	
	@Override
	/**
	 * Enters cutscene mode
	 */
	public void enterCutscene() {
		this.cutsceneActive = true;
		// Keeps player from accidentally skipping text immediately
		final Timer t = new Timer(500, null);
		t.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				GameWorld.this.textChangeReady = true;
				t.removeActionListener(this);
			}
		});
		t.start();
	}
}
