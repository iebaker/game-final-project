package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.LevelData.ConnectionData;
import cs195n.LevelData.EntityData;
import cs195n.Vec2f;
import engine.connections.Connection;
import engine.connections.Input;
import engine.connections.Output;
import engine.entity.Entity;
import engine.entity.PlayerEntity;

/**
 * Abstract class for a Game World
 * 
 * @author dgattey
 * 
 */
public abstract class World {
	
	public Viewport					v;
	protected Vec2f					dim;
	protected Vec2f					sDim;
	protected List<Entity>			entityStack;
	private List<Entity>			removeList;
	private List<Entity>			addList;
	private Color					bgColor;
	private PlayerEntity			player;
	private HashMap<String, Entity>	entityMap;
	
	/**
	 * Constructor, taking an end dimension (start dimension is always (0,0))
	 * 
	 * @param dim
	 */
	public World(Vec2f dim) {
		this.dim = dim;
		this.sDim = new Vec2f(0, 0);
		this.removeList = new ArrayList<Entity>();
		this.addList = new ArrayList<Entity>();
		this.bgColor = new Color(255, 255, 255);
		this.entityMap = new HashMap<String, Entity>();
	}
	
	/**
	 * Loads level from file given as arg, creating entities, etc that way
	 * 
	 * @param fileName
	 */
	public void loadLevelFromFile(String fileName, HashMap<String, Class<? extends Entity>> classes, World world) {
		try {
			LevelData data = CS195NLevelReader.readLevel(new File(fileName));
			String[] bg = this.create("backgroundColor", "255,255,255", data).split("[,]");
			this.bgColor = new Color(Integer.parseInt(bg[0]), Integer.parseInt(bg[1]), Integer.parseInt(bg[2]));
			
			// Entities!!
			for (EntityData ed : data.getEntities()) {
				
				// Make it!
				Class<?> clazz = classes.get(ed.getEntityClass());
				Entity e = (Entity) clazz.newInstance();
				e.setProperties(ed, world);
				
				// Put in the entity map for connections
				entityMap.put(ed.getName(), e);
				this.addEntity(e);
			}
			
			// Connections!!
			for (ConnectionData cd : data.getConnections()) {
				
				// The entities in question
				Entity source = entityMap.get(cd.getSource());
				Entity target = entityMap.get(cd.getTarget());
				
				// Connect the inputs and outputs
				Output o = source.getOutputByName(cd.getSourceOutput());
				Input i = target.getInputByName(cd.getTargetInput());
				Connection conx = new Connection(i, cd.getProperties());
				o.connect(conx);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find level file!");
			e.printStackTrace();
		} catch (InvalidLevelException e) {
			System.out.println("Invalid level!");
			e.printStackTrace();
		} catch (SecurityException e1) {
			System.out.println("Security exception!");
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			System.out.println("Bad arg");
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			System.out.println("Instantiation problem");
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			System.out.println("Illegal access");
			e1.printStackTrace();
		}
	}
	
	/**
	 * Makes a new game - override in child
	 */
	public abstract void newGame();
	
	/**
	 * Sets a win for the game
	 */
	public abstract void setWin();
	
	/**
	 * Sets a lose for the game with a message
	 */
	public abstract void setLose(String msg);
	
	/**
	 * Creates a string with a key, initial value, and level data
	 * 
	 * @param key
	 * @param initialVal
	 * @param l
	 * @return
	 */
	public String create(String key, String initialVal, LevelData l) {
		String toReturn = initialVal;
		if (l.getProperties().containsKey(key)) {
			toReturn = l.getProperties().get(key);
		}
		return toReturn;
	}
	
	/**
	 * Draws all entities to screen
	 * 
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (Entity e : entityStack) {
			e.onDraw(g);
		}
	}
	
	/**
	 * First removes & adds all items needed to prevent ConcurrentModificationException, then goes and passes the onTick
	 * to each of the entities to respond to
	 * 
	 * @param nanosSinceLastTick
	 */
	public void onTick(float secs) {
		for (Entity e : removeList) {
			if (entityStack.contains(e)) entityStack.remove(e);
		}
		for (Entity e : addList) {
			entityStack.add(e);
		}
		removeList = new ArrayList<Entity>();
		addList = new ArrayList<Entity>();
		for (Entity e : entityStack) {
			e.onTick(secs);
		}
	}
	
	/**
	 * Sets viewport
	 * 
	 * @param v
	 */
	protected void setPort(Viewport v) {
		this.v = v;
	}
	
	/**
	 * Getter for dim
	 * 
	 * @return
	 */
	public Vec2f getDim() {
		return dim;
	}
	
	/**
	 * Add an entity to the entity stack
	 * 
	 * @param entity
	 */
	public void addEntity(Entity entity) {
		addList.add(entity);
	}
	
	/**
	 * Remove an entity from the entity stack
	 * 
	 * @param entity
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	
	/**
	 * Checks to see if a vector is within the bounds of the game
	 * 
	 * @param vec
	 * @return If vec is within game world
	 */
	public boolean checkBounds(Vec2f vec) {
		return ((vec.x > sDim.x && vec.x < dim.x) && (vec.y > sDim.y && vec.y < dim.y));
	}
	
	/**
	 * Public getter for player
	 * 
	 * @return the player
	 */
	public PlayerEntity getPlayer() {
		return player;
	}
	
	/**
	 * Public setter for the player
	 * 
	 * @param playerEntity
	 */
	public abstract void setPlayer(Entity playerEntity);
	
	/**
	 * Public getter for all entities
	 * 
	 * @return all entities in a list
	 */
	public List<Entity> getEntities() {
		return entityStack;
	}
	
	/**
	 * Public getter for the background color
	 * 
	 * @return the background color set by the level editor
	 */
	public Color getBGColor() {
		return bgColor;
	}
	
	/**
	 * Returns gravity value for the world (-x for up, x for down)
	 * 
	 * @return the gravity value for this world
	 */
	public abstract float gravity();
	
	/**
	 * Flips the gravity of this world
	 */
	public abstract void flipGravity();
	
}
