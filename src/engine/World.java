package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.LevelData.ConnectionData;
import cs195n.LevelData.EntityData;
import cs195n.Vec2f;
import engine.collision.QuadTree;
import engine.connections.Connection;
import engine.connections.Input;
import engine.connections.Output;
import engine.entity.Entity;
import engine.entity.PassableEntity;
import engine.sound.Sound;
import engine.ui.TextBox;

/**
 * Abstract class for a Game World
 * 
 * @author dgattey
 * 
 */
public abstract class World implements Serializable {
	
	private static final long				serialVersionUID	= 8819430167695167366L;
	private List<Entity>					addList				= new ArrayList<Entity>();
	private Color							bgColor				= new Color(255, 255, 255);
	protected Vec2f							dim;
	private final HashMap<String, Entity>	entityMap			= new HashMap<String, Entity>();
	protected List<Entity>					entityStack;
	protected List<PassableEntity>			passList			= new ArrayList<PassableEntity>();
	private List<Entity>					removeList			= new ArrayList<Entity>();
	protected Vec2f							sDim				= new Vec2f(0, 0);
	protected TextBox						textBox;
	public transient Viewport				v;
	protected boolean						stopped;
	protected transient QuadTree						entity_tree;
	
	/**
	 * Constructor, taking an end dimension (start dimension is always (0,0))
	 * 
	 * @param dim
	 */
	public World(Vec2f dim, TextBox tb, Map<String, Entity> defaults) {
		this.dim = dim;
		entity_tree = new QuadTree(new Vec2f(0,0), dim);
		entityMap.put("textBox", tb);
		for (Map.Entry<String, Entity> item : defaults.entrySet()) {
			entityMap.put(item.getKey(), item.getValue());
		}
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
	 * Adds a passable entity to the passable entity stack
	 * 
	 * @param passableEntity
	 */
	public void addPassableEntity(Entity entity) {
		passList.add((PassableEntity) entity);
	}
	
	/**
	 * Checks to see if a vector is within the bounds of the game
	 * 
	 * @param vec
	 * @return If vec is within game world
	 */
	public boolean checkGameBounds(Vec2f vec) {
		return ((vec.x > sDim.x && vec.x < dim.x) && (vec.y > sDim.y && vec.y < dim.y));
	}
	
	/**
	 * Checks to see if a vector is within the bounds of the passed in vectors
	 * 
	 * @param vec
	 * @return If vec is within given bounds
	 */
	public boolean checkBounds(Vec2f vec, Vec2f start, Vec2f end) {
		return ((vec.x > start.x && vec.x < end.x) && (vec.y > start.y && vec.y < end.y));
	}
	
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
	
	public abstract void enterCutscene();
	
	/**
	 * Flips the gravity of this world
	 */
	public abstract void flipGravity();
	
	/**
	 * Public getter for the background color
	 * 
	 * @return the background color set by the level editor
	 */
	public Color getBGColor() {
		return bgColor;
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
	 * Public getter for all entities
	 * 
	 * @return all entities in a list
	 */
	public List<Entity> getEntities() {
		return entityStack;
	}
	
	public List<PassableEntity> getPassableEntities() {
		return passList;
	}
	
	public abstract Entity getPlayer();
	
	public abstract String getSoundFile();
	
	public TextBox getTextBox() {
		return textBox;
	}
	
	/**
	 * Returns gravity value for the world (-x for up, x for down)
	 * 
	 * @return the gravity value for this world
	 */
	public abstract float gravity();
	
	/**
	 * Loads level from file given as arg, creating entities, etc that way
	 * 
	 * @param fileName
	 */
	public void loadLevelFromFile(String fileName, HashMap<String, Class<? extends Entity>> classes, World world) {
		try {
			LevelData data = CS195NLevelReader.readLevel(new File(fileName));
			String[] bg = create("backgroundColor", "255,255,255", data).split("[,]");
			bgColor = new Color(Integer.parseInt(bg[0]), Integer.parseInt(bg[1]), Integer.parseInt(bg[2]));
			
			// Entities!!
			for (EntityData ed : data.getEntities()) {
				
				// Make it!
				Class<?> clazz = classes.get(ed.getEntityClass());
				Entity e = (Entity) clazz.newInstance();
				e.setProperties(ed, world);
				
				// Put in the entity map for connections
				entityMap.put(ed.getName(), e);
				addEntity(e);
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
	 * Takes all entities and checks if they're passable, moving them from entity stack to passable list if so
	 */
	protected void moveEntitiesToPassable() {
		for (Entity e : entityStack) {
			if (e instanceof PassableEntity) {
				removeEntity(e);
				addPassableEntity(e);
			}
		}
	}
	
	/**
	 * Makes a new game - override in child
	 */
	public abstract void newGame();
	
	/**
	 * Draws all entities to screen
	 * 
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		if (getPlayer() != null) getPlayer().onDraw(g);
		for (Entity e : passList) {
			e.onDraw(g);
		}
		for (Entity e : entityStack) {
			if (!e.equals(getPlayer())) e.onDraw(g);
		}
	}
	
	/**
	 * First removes & adds all items needed to prevent ConcurrentModificationException, then goes and passes the onTick
	 * to each of the entities to respond to
	 * 
	 * @param nanosSinceLastTick
	 */
	public void onTick(float secs) {
		entity_tree = new QuadTree(new Vec2f(0,0), dim);
		if (!stopped) {
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
			for (Entity e : passList) {
				e.onTick(secs);
			}

			// Bin everything in the quadtree

			entity_tree.clear();
			for(Entity e : entityStack) {
				entity_tree.insert(e);
			}
		}
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
	 * Sets a lose for the game with a message
	 */
	public abstract void setLose(String msg);
	
	public abstract void setPlayer(Entity player);
	
	/**
	 * Sets viewport
	 * 
	 * @param v
	 */
	protected void setPort(Viewport v) {
		this.v = v;
	}
	
	/**
	 * Sets a win for the game
	 */
	public abstract void setWin();
	
	public abstract void resetOffset();
	
	/**
	 * When reloading from saved game
	 */
	public abstract void reload();
	
	public abstract void addSound(Sound s);
	
	public abstract void save();
	
	public void stopTicking() {
		stopped = true;
	}
	
	public void continueTicking() {
		stopped = false;
	}
	
}
