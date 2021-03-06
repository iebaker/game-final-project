package engine.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cs195n.LevelData.EntityData;
import cs195n.LevelData.ShapeData;
import cs195n.Vec2f;
import engine.World;
import engine.collision.AAB;
import engine.collision.Circle;
import engine.collision.CollisionInfo;
import engine.collision.CollisionShape;
import engine.collision.Poly;
import engine.connections.Input;
import engine.connections.Output;
import engine.sound.Sound;
import engine.sound.SoundHolder;
import game.MuteHolder;

/**
 * Abstract Entity class for GameWorld
 * 
 * @author dgattey
 * 
 */
public abstract class Entity implements Serializable {
	
	private static final long			serialVersionUID	= -427222487694569405L;
	protected Color						c;
	protected float						contactDelay		= 0;
	private transient ArrayList<Sound>	currentSounds		= new ArrayList<Sound>();
	protected float						disappearing;
	private Vec2f						force;
	protected float						friction;
	protected float						height;
	protected float						hp;
	protected float						maxHP				= 100;
	private Vec2f						impulse;
	protected Map<String, Input>		inputs;
	public boolean						isStatic;
	protected Vec2f						lastMTV				= new Vec2f(0, 0);
	protected float						mass;
	protected Map<String, Output>		outputs;
	protected float						restitution;
	public CollisionShape				shape;
	private boolean						shootable;
	private int							shotsNeeded;
	private Vec2f						velocity;
	protected float						width;
	protected World						world;
	protected boolean					stopsLight			= true;
	protected float						damageCooldown		= 0;
	protected float						healCooldown		= 0;
	protected boolean					gravityImmune		= false;
	
	/**
	 * Emptyoonstructor - sets default values
	 */
	public Entity() {
		force = new Vec2f(0, 0);
		impulse = new Vec2f(0, 0);
		velocity = new Vec2f(0, 0);
		hp = maxHP;
		inputs = new HashMap<String, Input>();
		outputs = new HashMap<String, Output>();
		inputs.put("playSound", new Input() {
			
			private static final long	serialVersionUID	= -6139328109470836482L;
			private transient Sound		thisSound			= null;
			
			@Override
			public void run(Map<String, String> args) {
				// gets the sound file passed as an argument and plays it.
				if ((thisSound == null || !currentSounds.contains(thisSound)) && SoundHolder.soundTable != null) {
					Sound tmp = SoundHolder.soundTable.get(args.get("sound"));
					if (tmp != null)
						thisSound = tmp.duplicate();
					else
						return;
					if (args.containsKey("dist")) {
						thisSound.setDist(Float.parseFloat(args.get("dist")));
					}
					if (!MuteHolder.muted) {
						thisSound.loop();
					}
					currentSounds.add(thisSound);
					world.addSound(thisSound);
				}
			}
		});
		outputs.put("onTick", new Output());
	}
	
	/**
	 * Constructor for Entity with all fields (calculates mass automatically)
	 * 
	 * @param shape
	 *            the shape of the entity
	 * @param width
	 *            the width of the entity
	 * @param height
	 *            the height of the entity
	 * @param density
	 *            the given density of the entity (between 0 and 1)
	 * @param restitution
	 *            restitution value of the entity (between 0 and 1)
	 * @param isStatic
	 *            if the entity should be immovable
	 * @param c
	 *            the color to give the entity
	 * @param world
	 *            the world the entity appears in
	 */
	public Entity(CollisionShape shape, float width, float height, float density, float restitution, boolean isStatic,
			Color c, World world) {
		this(); // sets defaults
		this.width = width;
		this.height = height;
		this.isStatic = isStatic;
		this.restitution = restitution;
		mass = density * (new Float(Math.sqrt(width * height)));
		this.c = c;
		this.world = world;
	}
	
	/**
	 * Does nothing except for in sensors that send out a signal
	 */
	public void afterCollision(Entity other) {
		
	}
	
	/**
	 * Applies f force to the entity
	 * 
	 * @param f
	 *            the force to apply
	 */
	public void applyForce(Vec2f f) {
		if (!isStatic) force = force.plus(f);
	}
	
	/**
	 * Applies p impulse to the entity
	 * 
	 * @param p
	 *            the impulse to apply
	 */
	public void applyImpulse(Vec2f p) {
		if (!isStatic) impulse = impulse.plus(p);
	}
	
	/**
	 * Returns if this is colliding with the given entity e
	 * 
	 * @param e
	 *            an entity to collide with
	 * @return if this is colliding with entity e
	 */
	public boolean collideWithEntity(Entity e) {
		return ((e.shape != null && shape != null) ? shape.collides(e.shape) : false);
	}
	
	/**
	 * Creates a string with a key, initial value, and entity data
	 * 
	 * @param key
	 *            the key to check for in the properties of e
	 * @param defaultVal
	 *            the value to set if nothing there
	 * @param e
	 *            the entityData to look at
	 * @return the string value to return
	 */
	public String create(String key, String defaultVal, EntityData e) {
		String toReturn = defaultVal;
		if (e.getProperties().containsKey(key)) {
			toReturn = e.getProperties().get(key);
		}
		return toReturn;
	}
	
	/**
	 * Creates a string with a key, initial value, and shape data
	 * 
	 * @param key
	 *            the key to check for in the properties of e
	 * @param defaultVal
	 *            the value to set if nothing there
	 * @param e
	 *            the entityData to look at
	 * @return the string value to return
	 */
	public String create(String key, String defaultVal, ShapeData e) {
		String toReturn = defaultVal;
		if (e.getProperties().containsKey(key)) {
			toReturn = e.getProperties().get(key);
		}
		return toReturn;
	}
	
	/**
	 * Set full HP value - default 100
	 * 
	 * @return a value for full hp
	 */
	public float getMaxHP() {
		return maxHP;
	}
	
	public void setMaxHP(float hp) {
		maxHP = hp;
	}
	
	/**
	 * Gets an input from the map, given a name
	 * 
	 * @param s
	 *            the string representing the input name
	 * @return the input given by s
	 */
	public Input getInputByName(String s) {
		return inputs.get(s);
	}
	
	/**
	 * Gets an output from the map, given a name
	 * 
	 * @param s
	 *            the string representing the output name
	 * @return the output given by s
	 */
	public Output getOutputByName(String s) {
		return outputs.get(s);
	}
	
	/**
	 * Public getter for shotsNeeded
	 * 
	 * @return how many shots needed until entity dies
	 */
	public int getShotsNeeded() {
		return shotsNeeded;
	}
	
	/**
	 * Returns velocity for the entity (0 if static)
	 * 
	 * @return
	 */
	protected Vec2f getVelocity() {
		if (isStatic)
			return new Vec2f(0, 0);
		else
			return velocity;
	}
	
	/**
	 * Returns if the entity is shootable
	 * 
	 * @return if entity is shootable
	 */
	public boolean isShootable() {
		return shootable;
	}
	
	/**
	 * Collision response: translates entity out of collision, applies impulse, and does the same for the other shape -
	 * note: if Static, the translation is double for the non static entity for smoothness
	 * 
	 * @param collisionInfo
	 */
	public void onCollide(CollisionInfo collisionInfo) {
		CollisionShape s1 = collisionInfo.thisShape;
		CollisionShape s2 = collisionInfo.otherShape;
		Entity o1 = this;
		Entity o2 = collisionInfo.other;
		Vec2f mtv = collisionInfo.mtv;
		lastMTV = mtv;
		contactDelay = 0.2f;
		
		// Translation
		if (!o1.isStatic) {
			if (!o2.isStatic)
				s1.move(mtv.smult(0.5f));
			else
				s1.move(mtv);
		}
		if (!o2.isStatic) {
			if (!o1.isStatic)
				s2.move(mtv.smult(-0.5f));
			else
				s2.move(mtv.smult(-1));
		}
		
		// Impulse - constants
		float COR = (float) Math.sqrt(o1.restitution * o2.restitution);
		Vec2f ua = o1.getVelocity().projectOnto(mtv.normalized());
		Vec2f ub = o2.getVelocity().projectOnto(mtv.normalized());
		
		// Impulse - find k factor
		float k;
		if (o1.isStatic) {
			k = o2.mass * (1 + COR);
		} else if (o2.isStatic) {
			k = o1.mass * (1 + COR);
		} else
			k = (o1.mass * o2.mass * (1 + COR)) / (o1.mass + o2.mass);
		
		// Impulse - final values
		Vec2f impA = (ub.minus(ua)).smult(k);
		Vec2f impB = (ua.minus(ub)).smult(k);
		
		// Impulse - apply
		o1.applyImpulse(impA);
		o2.applyImpulse(impB);
		
		// Friction
		float COF = (float) Math.sqrt(o1.friction * o2.friction);
		float uaf = o1.getVelocity().dot(mtv.normalized().perpendicular());
		float ubf = o2.getVelocity().dot(mtv.normalized().perpendicular());
		float urel = ubf - uaf;
		float k2 = 20f;
		Vec2f f = mtv.normalized().perpendicular().smult((k2 * COF) * impA.mag() * (Math.signum(urel)));
		assert (f.normalized().perpendicular().equals(mtv.normalized()));
		
		// Friction - apply
		o1.applyForce(f);
		o2.applyForce(f.smult(-1));
	}
	
	/**
	 * Makes correct color based off HP
	 * 
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		if (world != null && shape != null) {
			g.setColor(c);
			toScreen();
			shape.drawAndFillShape(g);
			
		}
	}
	
	/**
	 * Applies gravity, updates velocity, then position, then resets force and impulse Also updates active sounds and
	 * removes completed tracks
	 * 
	 * @param t
	 *            Nanoseconds since last tick
	 */
	public void onTick(float t) {
		if (damageCooldown > 0) {
			damageCooldown -= t;
		}
		
		if (healCooldown > 0) {
			healCooldown -= t;
		}
		
		contactDelay -= t;
		if (!gravityImmune) {
			applyForce(new Vec2f(0, world.gravity() * mass)); // apply gravity
		}
		if (mass != 0) {
			velocity = getVelocity().plus(force.sdiv(mass).smult(t).plus(impulse.sdiv(mass))); // new velocity
			if (shape != null) {
				shape.move(velocity.smult(t)); // new position
			}
		}
		
		force = new Vec2f(0, 0);
		impulse = new Vec2f(0, 0);
		
		// see if new sounds should be played
		if (outputs.get("onTick").hasConnection()) {
			outputs.get("onTick").run();
		}
		
		if (currentSounds == null) currentSounds = new ArrayList<Sound>();
		if (!currentSounds.isEmpty() && world.getPlayer() != null) {
			for (int i = currentSounds.size() - 1; i >= 0; i--) {
				Sound s = currentSounds.get(i);
				// calculate how far the source of the sound is from the player
				Float dist = world.getPlayer().shape.getCenter().minus(shape.getCenter()).mag();
				if (dist < s.getDist()) {
					s.setVolume(1 - Math.sqrt(.00033 * dist));
				} else {
					s.setVolume(0);
				}
			}
		}
	}
	
	/**
	 * Reloads all sounds. Used when loading a save file.
	 */
	public void reloadTransientData() {
		currentSounds = new ArrayList<Sound>();
	}
	
	/**
	 * Resets the X part of the current velocity
	 */
	public void resetX() {
		velocity = new Vec2f(0, velocity.y);
		impulse = new Vec2f(0, impulse.y);
	}
	
	/**
	 * Resets the Y part of the current velocity
	 */
	public void resetY() {
		velocity = new Vec2f(velocity.x, 0);
		impulse = new Vec2f(impulse.x, 0);
	}
	
	/**
	 * Method to set all fields of the entity based on EntityData
	 * 
	 * @param ed
	 *            the EntityData to parse and create a new entity out of
	 * @param world
	 *            the GameWorld in which this entity should be placed
	 */
	public void setProperties(EntityData ed, World world) {
		if (!ed.getShapes().isEmpty()) {
			ShapeData shapeData = ed.getShapes().get(0);
			String[] shapeColor = new String[] { "0", "0", "0" };
			String alpha = create("alpha", "255", shapeData);
			if (shapeData.getProperties().get("color") != null) {
				shapeColor = shapeData.getProperties().get("color").split("[,]");
			}
			Vec2f min = shapeData.getMin();
			
			switch (shapeData.getType()) {
			case CIRCLE:
				shape = new Circle(min, shapeData.getRadius(), c);
				break;
			case BOX:
				shape = new AAB(min, min.plus(new Vec2f(shapeData.getWidth(), shapeData.getHeight())), c);
				break;
			case POLY:
				shape = new Poly(c, shapeData.getVerts().toArray(new Vec2f[shapeData.getVerts().size()]));
				break;
			default:
				break;
			}
			c = new Color(Integer.parseInt(shapeColor[0]), Integer.parseInt(shapeColor[1]),
					Integer.parseInt(shapeColor[2]), Integer.parseInt(alpha));
			width = shapeData.getWidth();
			height = shapeData.getHeight();
			
			// Will set static for anything that contains Static in name or has a property static: true
			String stat = ed.getProperties().get("static");
			isStatic = ((stat != null && stat.equals("true")) || ed.getEntityClass().contains("Static"));
		}
		restitution = Float.parseFloat(create("restitution", "0.5", ed));
		friction = Float.parseFloat(create("friction", "0.5", ed));
		mass = (float) (Float.parseFloat(create("density", "1", ed)) * (Math.sqrt(width * height)));
		disappearing = Float.parseFloat(create("disappearing", "0", ed));
		shotsNeeded = Integer.parseInt(create("shotsNeeded", "0", ed));
		shootable = (getShotsNeeded() > 0);
		this.world = world;
	}
	
	/**
	 * Sets shotsNeeded
	 * 
	 * @param shotsNeeded
	 *            how many shots needed until entity dies
	 */
	public void setShotsNeeded(int shotsNeeded) {
		this.shotsNeeded = shotsNeeded;
	}
	
	/**
	 * Stops all sounds.
	 */
	public void stopSound() {
		for (Sound s : currentSounds) {
			s.stop();
		}
	}
	
	/**
	 * Starts all sounds.
	 */
	public void startSound() {
		for (Sound s : currentSounds) {
			s.loop();
		}
	}
	
	/**
	 * Translates the game coordinates to onscreen ones
	 */
	public void toScreen() {
		shape.toScreen(world.v);
	}
	
	/**
	 * String representation for Entities, including shape, restitution, and mass
	 */
	@Override
	public String toString() {
		return "Entity<sh:" + shape + " rst:" + restitution + " mass:" + mass + ">";
	}
	
	public void setVelocity(Vec2f velocity) {
		this.velocity = velocity;
	}
	
	public boolean stopsLight() {
		return stopsLight;
	}
	
	public float damage(float damage) {
		if (damageCooldown <= 0) {
			hp -= damage;
			if (hp <= 0) {
				die();
			}
			damageCooldown = 0.5f;
			return damage;
		}
		return 0;
	}
	
	public float heal(float toHeal) {
		if (healCooldown <= 0) {
			healCooldown = 0.5f;
			hp += toHeal;
			if (hp > maxHP) {
				hp = maxHP;
				return 1;
			}
			return toHeal;
		}
		return 0;
	}
	
	/**
	 * Heals with no cooldown
	 */
	public void flatHeal(float toHeal) {
		hp += toHeal;
		if (hp > maxHP) {
			hp = maxHP;
		}
	}
	
	/**
	 * Damages with no cooldown
	 * 
	 * @param toDamage
	 */
	public void flatDamage(float toDamage) {
		hp -= toDamage;
		if (hp <= 0) {
			die();
		}
	}
	
	public float getHP() {
		return hp;
	}
	
	/**
	 * Kills the entity. Override for special behavior.
	 */
	public void die() {
		world.removeEntity(this);
	}
	
	/**
	 * By default, name of entity is toString of it - can be overridden
	 * 
	 * @return Name of entity
	 */
	public String getName() {
		return toString();
	}
}