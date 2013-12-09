package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs195n.Vec2f;
import engine.Saver;
import engine.Viewport;
import engine.World;
import engine.collision.AAB;
import engine.collision.Circle;
import engine.collision.CollisionInfo;
import engine.collision.CollisionShape;
import engine.collision.Poly;
import engine.collision.Ray;
import engine.entity.EnemyEntity;
import engine.entity.Entity;
import engine.entity.PassableEntity;
import engine.entity.RelayEntity;
import engine.entity.SensorEntity;
import engine.entity.StaticEntity;
import engine.lighting.LightSource;
import engine.lighting.LightWorld;
import engine.lighting.LightingEngine;
import engine.lighting.Vec2fPair;
import engine.sound.Sound;
import engine.ui.TextBox;
import game.entities.LightCrystal;
import game.entities.Player;
import game.entities.ShadowEnemy;
import game.entities.StartCrystal;
import game.entities.WaterEntity;
import game.entities.WinEntity;
import game.entities.spawners.ArmadilloSpawner;
import game.entities.spawners.BatSpawner;
import game.entities.spawners.EnemySpawner;
import game.entities.spawners.FrogSpawner;
import game.flora.trees.FanTree;
import game.flora.trees.MapleTree;
import game.flora.trees.PineTree;

/**
 * GameWorld for M
 * 
 * @author dgattey
 */
public class GameWorld extends World implements LightWorld {
	
	private static HashMap<String, Entity>					defaults;
	private static WorldTrigger								wt					= new WorldTrigger();
	static {
		GameWorld.defaults = new HashMap<String, Entity>();
		GameWorld.defaults.put("world", GameWorld.wt);
	}
	
	public static final String								saveFile			= System.getProperty("user.home")
																						+ "/save.gme";
	private static final long								serialVersionUID	= 6619354971290257104L;
	public static final Color								DUSKY_VIOLET		= new Color(126, 126, 191);
	public static final Color								DARK_LAVENDER		= new Color(45, 30, 50);
	private static final float								TICK_LENGTH			= 0.005f;
	public static final String								LEVEL_NAME			= "lib/Level1.nlf";
	private final HashMap<String, Class<? extends Entity>>	classes;
	private boolean											cutsceneActive;
	private float											gravity;
	private float											hp;
	private double											leftoverTime;
	public Level											level;
	private Vec2f											line;
	private float											laserCooldown		= 0;
	private int												lineCt;
	private boolean											lose;
	private String											message;
	private boolean											paused;
	private Player											player;
	private final String									soundFile			= "sounds.xml";
	private boolean											transferredEntities	= false;
	private transient LightSource							lightSource;
	public transient LightingEngine							lightEngine			= new LightingEngine();
	private boolean											win;
	private transient ArrayList<Sound>						allSounds			= new ArrayList<Sound>();
	private StartCrystal									startCrystal;
	private ArrayList<BackgroundLight>						bgLights			= new ArrayList<BackgroundLight>();
	private float											saveCooldown		= 0;
	private boolean											gameOver			= false;
	private transient Tooltip								tooltip;
	private ArrayList<EnemySpawner> spawners = new ArrayList<EnemySpawner>();
	
	/**
	 * Constructor for a world that starts a new game
	 * 
	 * @param dim
	 * @param tb
	 */
	public GameWorld(Vec2f dim, TextBox tb) {
		super(dim, tb, GameWorld.defaults);
		GameWorld.wt.setWorld(this);
		textBox = tb;
		tb.setWorld(this);
		tooltip = new Tooltip(this);
		
		// Classes map
		classes = new HashMap<String, Class<? extends Entity>>();
		classes.put("PlayerEntity", Player.class);
		classes.put("StaticEntity", StaticEntity.class);
		classes.put("EnemyEntity", EnemyEntity.class);
		classes.put("Entity", Entity.class);
		classes.put("Sensor", SensorEntity.class);
		classes.put("Relay", RelayEntity.class);
		classes.put("WinEntity", WinEntity.class);
		classes.put("PassableEntity", PassableEntity.class);
		classes.put("Water", WaterEntity.class);
		classes.put("DarkFrog", FrogSpawner.class);
		classes.put("LightCrystal", LightCrystal.class);
		classes.put("ArmadilloOfDarkness", ArmadilloSpawner.class);
		classes.put("StartCrystal", StartCrystal.class);
		classes.put("BackgroundLight", BackgroundLight.class);
		classes.put("MapleTree", MapleTree.class);
		classes.put("PineTree", PineTree.class);
		classes.put("FanTree", FanTree.class);
		classes.put("DuskBat", BatSpawner.class);
		
		newGame();
	}
	
	/**
	 * Checks collisions by way of double iteration through all elements - does special things for certain entities
	 */
	public void checkCollisions() {
		for(int i = 0; i < entityStack.size(); i++) {
			Entity a = entityStack.get(i);
			for(int j = i + 1; j < entityStack.size(); j++) {
				Entity b = entityStack.get(j);
				
				if(a instanceof EnemyEntity && b instanceof Player && a.collideWithEntity(b)) {
					if(((EnemyEntity) a).drains()) {
						a.heal(((Player) b).damage(((EnemyEntity) a).getDamage()));
					} else {
						((Player) b).damage(((EnemyEntity) a).getDamage());
					}
				} else if(b instanceof EnemyEntity && a instanceof Player && b.collideWithEntity(a)) {
					if(((EnemyEntity) b).drains()) {
						b.heal(((Player) a).damage(((EnemyEntity) b).getDamage()));
					} else {
						((Player) a).damage(((EnemyEntity) b).getDamage());
					}
				}
				
				if(a instanceof LightCrystal && b instanceof Player && a.collideWithEntity(b)) {
					((LightCrystal) a).destroy();
					((Player) b).addCrystal();
				} else if(b instanceof LightCrystal && a instanceof Player && b.collideWithEntity(a)) {
					((LightCrystal) b).destroy();
					((Player) a).addCrystal();
				}
				
				else if(a.collideWithEntity(b)) {
					CollisionInfo aCol = new CollisionInfo(a, b);
					if(aCol.mtv != null && !aCol.mtv.isZero()) a.onCollide(aCol);
					a.afterCollision(b);
					b.afterCollision(a);
				} else if(b.collideWithEntity(a)) {
					a.afterCollision(b);
					b.afterCollision(a);
				}
				
			}
			for(PassableEntity e : passList) {
				if(e.canCollide() && e.collideWithEntity(a)) {
					e.onCollide(new CollisionInfo(e, a));
					a.afterCollision(e);
					e.afterCollision(a);
				}
			}
		}
	}
	
	@Override
	/**
	 * Enters cutscene mode
	 */
	public void enterCutscene() {
		cutsceneActive = true;
	}
	
	/**
	 * Fires a bullet from the player to dest (mouse location)
	 * 
	 * @param dest
	 *            the destination of the ray
	 */
	private void fireBullet(Vec2f dest) {
		if(player.laserUnlocked() && !paused && !cutsceneActive && player != null) {
			Vec2f src = player.shape.getCenter();
			Ray ray = new Ray(src, dest);
			Entity affected = null;
			Vec2f castTo = null;
			for(Entity e : entityStack) {
				if(!e.equals(player) && e.shape != null) {
					Vec2f tmp = e.shape.cast(ray);
					if(castTo == null
							|| (castTo != null && tmp != null && tmp.minus(src).mag() < castTo.minus(src).mag())) {
						affected = e;
						castTo = tmp;
					}
				}
			}
			if(castTo != null) {
				affected.applyImpulse(ray.getDirection().smult(5000f));
				if(affected instanceof ShadowEnemy) {
					affected.damage(10);
				}
				// For breakable blocks
				if(affected.isShootable()) {
					affected.setShotsNeeded(affected.getShotsNeeded() - 1);
					if(affected.getShotsNeeded() < 1) removeEntity(affected);
				}
				line = castTo;
			}
			laserCooldown = 0.1f;
		}
	}
	
	@Override
	public void flipGravity() {
		gravity = -gravity;
	}
	
	/**
	 * Public getter for message
	 * 
	 * @return
	 */
	public String getCurrentMessage() {
		return message;
	}
	
	/**
	 * Public getter for health
	 * 
	 * @return
	 */
	public float getHealth() {
		if(player == null)
			return 100;
		else
			return player.getHP();
	}
	
	/**
	 * Retrieves the world's lighting engine
	 */
	public LightingEngine getLightingEngineForTesting() {
		return lightEngine;
	}
	
	@Override
	/**
	 * Returns a list of all light sources in the world.  For testing purposes, this
	 * just moves the single lightsource to the Player's location (actually, reassigns
	 * the variable lightsource to a new lightsource at that location) and returns
	 * a list containing just that
	 *
	 * @return 		A list of all light sources in the world
	 */
	public List<LightSource> getLightSources() {
		List<LightSource> ret = new ArrayList<LightSource>();
		if(player != null) {
			lightSource = new LightSource(player.shape.getCenter());
			lightSource.setBrightness(player.getHP() / 100);
			ret.add(lightSource);
		}
		return ret;
	}
	
	@Override
	public Entity getPlayer() {
		return player;
	}
	
	@Override
	/**
	 * This is hacky.  Fair warning.  Takes in a point which is the source point (for
	 * circle point calculations) and also a list which will be modified to contain all
	 * the points in the world with respect to sourcepoint.  Returns a list of Vec2fPair
	 * which represents all pairings of points in the world
	 *
	 * @param 	sourcePoint  	The point from which we calculate all points
	 * @param   points 			A (hopefully) empty list which will be modified by this function
	 * @return 					A list containing all the pairs of points in the world
	 */
	public List<Vec2fPair> getPointsAndPairs(Vec2f sourcePoint, List<Vec2f> points) {
		List<Vec2fPair> pointPairs = new ArrayList<Vec2fPair>();
		
		for(Entity e : getEntities()) {
			if(!e.stopsLight()) continue;
			CollisionShape shape = e.shape;
			
			if(shape instanceof AAB) {
				AAB a = (AAB) shape;
				
				// Construct points of AAB in order
				Vec2f p1 = a.getMin();
				Vec2f p2 = new Vec2f(a.getMin().x, a.getMax().y);
				Vec2f p3 = a.getMax();
				Vec2f p4 = new Vec2f(a.getMax().x, a.getMin().y);
				
				// Add to points list
				points.add(p1);
				points.add(p2);
				points.add(p3);
				points.add(p4);
				
				// Add to point pairs list
				pointPairs.add(new Vec2fPair(p1, p2));
				pointPairs.add(new Vec2fPair(p2, p3));
				pointPairs.add(new Vec2fPair(p3, p4));
				pointPairs.add(new Vec2fPair(p4, p1));
				
			} else if(shape instanceof Circle) {
				
				Circle circle = (Circle) shape;
				Vec2f circleToSource = circle.getCenter().minus(sourcePoint);
				
				float d = circleToSource.mag();
				float r = circle.getRadius();
				
				float theta = (float) Math.acos(r / d);
				
				float out = r * (float) Math.cos(theta);
				float wide = r * (float) Math.sin(theta);
				
				Vec2f normC2C = circleToSource.normalized();
				Vec2f temp = circle.getCenter().minus(normC2C.smult(out));
				
				Vec2f finalvec = new Vec2f(-normC2C.y, normC2C.x).smult(wide);
				
				// Find points
				Vec2f p1 = temp.plus(finalvec);
				Vec2f p2 = temp.minus(finalvec);
				
				// Add points
				points.add(p1);
				points.add(p2);
				
				// Add single point pair
				pointPairs.add(new Vec2fPair(p1, p2));
				
			} else if(shape instanceof Poly) {
				
				// Make polygon out of shape
				Poly p = (Poly) shape;
				
				// Add all points to "points"
				List<Vec2f> polyPoints = p.getPoints();
				points.addAll(polyPoints);
				
				// Add proper point pairs
				polyPoints.add(polyPoints.get(0));
				for(int i = 0; i < polyPoints.size() - 1; ++i) {
					pointPairs.add(new Vec2fPair(polyPoints.get(i), polyPoints.get(i + 1)));
				}
				
			}
		}
		
		return pointPairs;
	}
	
	@Override
	public String getSoundFile() {
		return soundFile;
	}
	
	@Override
	/**
	 * Returns the size of the world as a Vec2f
	 *
	 * @return 		Exactly what I just said
	 */
	public Vec2f getWorldSize() {
		return getDim();
	}
	
	@Override
	/**
	 * Returns the gravity value for this world (-x is up, x is down)
	 */
	public float gravity() {
		return gravity;
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
		for(Sound s : allSounds) {
			s.stop();
			s.close();
		}
		allSounds.clear();
		player = null;
		level = new Level(lvl, 0f);
		if(v != null) v.viewHasChanged(true);
		leftoverTime = 0;
		line = null;
		win = false;
		lose = false;
		laserCooldown = 0;
		lineCt = 0;
		gravity = 300;
		entityStack = new ArrayList<Entity>();
		startCrystal = null;
		bgLights = new ArrayList<BackgroundLight>();
		passList = new ArrayList<PassableEntity>();
		transferredEntities = false;
		
		// Actually load the level
		loadLevelFromFile(GameWorld.LEVEL_NAME, classes, this);
		if(lvl == 1) setMessage("Game starts in 3", 3.5f);
		
		moveEntitiesToPassable();
		
		textBox.setVisible(false);
		cutsceneActive = false;
		
	}
	
	@Override
	/**
	 * Draws the entities and a line for the bullet if needed
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		// Draws the starting crystal on front of everything else
		if(startCrystal != null) {
			startCrystal.onDraw(g);
		}
		
		// Draws the line for bullets
		if(line != null && player != null && laserCooldown > 0) {
			g.setColor(new Color(0.7f, 0.7f, 1f, 0.6f));
			g.setStroke(new BasicStroke(Viewport.gameFloatToScreen(10f)));
			Vec2f p1 = Viewport.gamePtToScreen(player.shape.getCenter());
			Vec2f p2 = Viewport.gamePtToScreen(line);
			g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
		} else if(laserCooldown <= 0) {
			lineCt++;
		} else if(lineCt >= 20) {
			lineCt = 0;
			line = null;
		}
		
		for(BackgroundLight light : bgLights) {
			light.onDraw(g);
		}
		
		super.onDraw(g); // draws all entities
		tooltip.onDraw(g);
	}
	
	/**
	 * Responders for key presses
	 * 
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		lightEngine.onKeyPressed(e);
		int keyCode = e.getKeyCode();
		if(cutsceneActive) {
			if(textBox.hasNextLine()) {
				textBox.displayNext();
			} else {
				cutsceneActive = false;
				textBox.setVisible(false);
			}
		}
		switch(keyCode) {
		case (KeyEvent.VK_P): // Pause
			if(player != null && !lose && !win)
				paused = !paused;
			else
				paused = false;
			break;
		default:
			break;
		}
		if(player != null && !lose && !win) {
			player.onKeyPressed(e);
		}
	}
	
	/**
	 * Resets goal velocity when key released
	 * 
	 * @param e
	 */
	public void onKeyReleased(KeyEvent e) {
		if(player != null && !lose && !win) player.onKeyReleased(e);
	}
	
	/**
	 * Responder for mouse release - shoots bullet to mouse location with left button if in game and shoots grenade if
	 * right button or ctrl-left mouse button
	 * 
	 * @param e
	 */
	public void onMouseClicked(MouseEvent e) {
		Vec2f pt = Viewport.screenPtToGame(new Vec2f(e.getX(), e.getY()));
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(checkBounds(pt)) {
				fireBullet(pt);
			}
			if(checkBounds(pt)) fireBullet(pt);
		}
	}
	
	public void onMouseMoved(MouseEvent e) {
		Vec2f pt = Viewport.screenPtToGame(new Vec2f(e.getX(), e.getY()));
		tooltip.setLocation(pt);
	}
	
	/**
	 * Does a lot of stuff every tick - see breakdown comments in code below - but splits tick into multiple iterations
	 * of 0.005 seconds for consistency
	 */
	@Override
	public void onTick(float secs) {
		// Calculates standard tick - how many + leftover time to counter for later
		double timeSteps = (secs / GameWorld.TICK_LENGTH) + leftoverTime;
		long steps = (long) timeSteps;
		leftoverTime = timeSteps - steps;
		for(int i = 0; i < steps; i++) {
			
			// Shows paused message
			if(paused)
				message = "Game paused";
			
			// Ticks through each entity and checks collisions
			else if(!cutsceneActive) {
				message = "";
				super.onTick(GameWorld.TICK_LENGTH);
				checkCollisions();
				level.onTick(GameWorld.TICK_LENGTH);
			}
		}
		if(!transferredEntities) {
			transferredEntities = true;
			moveEntitiesToPassable();
			moveEntitesToSpawners();
		}
		if(laserCooldown > 0) {
			float secs2 = secs;
			if(secs2 > laserCooldown) {
				secs2 = laserCooldown;
			}
			laserCooldown -= secs2;
			player.flatDamage(secs2 * 20);
		}
		
		if(saveCooldown > 0) {
			saveCooldown -= secs;
		}
	}
	
	private void moveEntitesToSpawners() {
		for (Entity e : entityStack) {
			if (e instanceof EnemySpawner) {
				removeEntity(e);
				this.spawners.add((EnemySpawner) e);
			}
		}
	}

	@Override
	/**
	 * Sets a lose with a message
	 */
	public void setLose(String msg) {}
	
	/**
	 * Set the message for the message display with a countdown or just pause the game
	 * 
	 * @param msg
	 * @param ct
	 */
	private void setMessage(String msg, float ct) {
		message = msg;
	}
	
	@Override
	public void setPlayer(Entity p) {
		player = (Player) p;
		if(hp > 0) player.heal(hp);
	}
	
	public void unlockJump() {
		player.unlockJump();
	}
	
	@Override
	public void resetOffset() {
		// Resets viewport to be the correct offset based on player location
		if(v != null && player != null)
			v.setOffset(player.shape.getLocation().minus(Viewport.screenPtToGameForOffset(v.getDim().sdiv(2))));
	}
	
	@Override
	public void reload() {
		for(Entity e : getEntities()) {
			e.reloadTransientData();
		}
		for(Entity e : getPassableEntities()) {
			e.reloadTransientData();
		}
		tooltip = new Tooltip(this);
		allSounds = new ArrayList<Sound>();
		lightEngine = new LightingEngine();
	}
	
	@Override
	public void addSound(Sound s) {
		allSounds.add(s);
	}
	
	public void mute() {
		for(Sound s : allSounds) {
			s.pause(true);
		}
	}
	
	public void unmute() {
		for(Sound s : allSounds) {
			s.pause(false);
		}
	}
	
	public void addStartCrystal(StartCrystal start) {
		removeEntity(start);
		startCrystal = start;
	}
	
	public StartCrystal getStartCrystal() {
		return startCrystal;
	}
	
	public void addBackgroundLight(BackgroundLight backgroundLight) {
		removeEntity(backgroundLight);
		bgLights.add(backgroundLight);
	}
	
	/**
	 * For when the player dies
	 */
	public void die() {
		// TODO add nice transition here
		gameOver = true;
	}
	
	@Override
	public void save() {
		if(saveCooldown <= 0) {
			Saver.saveGame(GameWorld.saveFile, this);
			saveCooldown = 5;
		}
	}
	
	public boolean isOver() {
		return gameOver;
	}
	
	@Override
	public void setWin() {
		// TODO Auto-generated method stub
		
	}
	
	public Tooltip getTooltip() {
		return tooltip;
	}

	public void reloadEnemies() {
		for(EnemySpawner s : spawners) {
			s.makeEnemy();
		}
	}
}