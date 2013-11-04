package smt3.gameengine.other;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.Vec2f;
import cs195n.LevelData.EntityData;
import cs195n.LevelData.ShapeData;
import cs195n.LevelData.ShapeData.Type;
import smt3.gameengine.physics.Circle;
import smt3.gameengine.physics.ConvexPolygon;
import smt3.gameengine.physics.Entity;
import smt3.gameengine.physics.Rectangle;
import smt3.gameengine.physics.Shape;
import smt3.gameengine.ui.Application;
import smt3.gameengine.ui.Background;
import smt3.m.Bound;
import smt3.m.Player;

/**
 * A world class. Populates the world, controls a basic array of entities in the world, and stores information like world size
 * @author Sawyer
 *
 */
public abstract class World {
	private Background _bg;
	private ArrayList<Entity> _entities = new ArrayList<Entity>();;
	private Vec2f _gameCoords;
	private Vec2f _gameDims;
	private ArrayList<Entity> _toRemove = new ArrayList<Entity>();;
	private ArrayList<Entity> _toAdd = new ArrayList<Entity>();;
	private Application _app;
	private Viewport _vp;
	private HashMap<String, Entity> _entityMap = new HashMap<String, Entity>();
	
	/**
	 * Populates the world
	 * @param gameCoords
	 * @param gameDims
	 * @param app
	 */
	public World(Vec2f gameCoords, Vec2f gameDims, Application app) {
		_gameCoords = gameCoords;
		_gameDims = gameDims;
		_app = app;
		
		LevelData data;
		try {
			data = CS195NLevelReader.readLevel(new File("BasicLevel.nlf"));		
			//Find and set the background color
			String[] bgColors = this.initialize("bgColor", "255,255,255", data).split("[,]");
			this.setBackground(new Background(this, new Color(Integer.parseInt(bgColors[0]),Integer.parseInt(bgColors[1]),Integer.parseInt(bgColors[2]))));
			
			//Create the walls
			String[] wallColors = this.initialize("wallColor", "0,0,0", data).split("[,]");
			Color wallColor = new Color(Integer.parseInt(wallColors[0]), Integer.parseInt(wallColors[1]), Integer.parseInt(wallColors[2]));
			float wallRestitution = Float.parseFloat(this.initialize("wallRestitution", "0.1", data));
			this.addEntity(new Bound(new Vec2f(-300, -300), new Vec2f(this.getGameDims().x+600, 325), this, wallColor, wallRestitution));
			this.addEntity(new Bound(new Vec2f(-300, this.getGameDims().y-25), new Vec2f(this.getGameDims().x+600, 325), this, wallColor, wallRestitution));
			this.addEntity(new Bound(new Vec2f(this.getGameDims().x - 25, 0), new Vec2f(325, this.getGameDims().y + 300), this, wallColor, wallRestitution));
			this.addEntity(new Bound(new Vec2f(-300, -300), new Vec2f(325, this.getGameDims().y + 300), this, wallColor, wallRestitution));
			
			for(EntityData e : data.getEntities()) {
				Shape s = null;
				ShapeData sd = e.getShapes().get(0);
				Type es = sd.getType();
				String[] shapeRGBs = sd.getProperties().get("color").split("[,]");
				Color c = new Color(Integer.parseInt(shapeRGBs[0]), Integer.parseInt(shapeRGBs[1]), Integer.parseInt(shapeRGBs[2]));
				switch(es) {
					case CIRCLE:
						s = new Circle(sd.getMin(), sd.getRadius()*2, c);
						break;
					case BOX:
						s = new Rectangle(sd.getMin(), new Vec2f(sd.getWidth(), sd.getHeight()), c);
						break;
					case POLY:
						s = new ConvexPolygon(c, sd.getVerts().toArray(new Vec2f[sd.getVerts().size()]));
						break;
					default:
						break;
				}
				
				EntTypes et = EntTypes.valueOf(e.getEntityClass().toUpperCase());
				float density = Float.parseFloat(this.initialize("density", "1", e));
				float restitution = Float.parseFloat(this.initialize("restitution", "1", e));
				Entity toAdd = null;
				switch(et) {
				case PLAYER:
					toAdd = this.addPlayer(s, density, restitution);
					break;
				case STATIC:
					toAdd = new Entity(s, this, density, restitution);
					toAdd.setStatic(true);
					break;
				case DYNAMIC:
					toAdd = new Entity(s, this, density, restitution);
					break;
				default:
					break;
				
				}
				_entityMap.put(e.getName(), toAdd);
				this.addEntity(toAdd);
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (InvalidLevelException e1) {
			e1.printStackTrace();
		}
	}
	
	public enum EntTypes {
		PLAYER,
		STATIC,
		DYNAMIC
	}
	
	//helper method
	public String initialize(String key, String initialVal, EntityData e) {
		String toReturn = initialVal;
		if(e.getProperties().containsKey(key)) {
			toReturn = e.getProperties().get(key);
		}
		return toReturn;
	}
	
	//helper method
	public String initialize(String key, String initialVal, LevelData l) {
		String toReturn = initialVal;
		if(l.getProperties().containsKey(key)) {
			toReturn = l.getProperties().get(key);
		}
		return toReturn;
	}

	public void addEntity(Entity e) {
		_toAdd.add(e);
	}
	
	/**
	 * Draws each entity
	 * @param g
	 */
	public void onDraw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		_bg.onDraw(g);
		for(Entity e : _entities) {
			e.onDraw(g);
		}
	}
	
	/**
	 * Ticks each entity
	 * @param nanosSinceLastTick
	 */
	public void onTick(long nanosSinceLastTick) {
		for(Entity e : _entities) {
			e.onTick(nanosSinceLastTick);
		}
		for(Entity e : _toAdd) {
			_entities.add(e);
		}
		for(Entity e : _toRemove) {
			_entities.remove(e);
		}
		_toAdd.clear();
		_toRemove.clear();
	}
	
	public void setBackground(Background bg) {
		_bg = bg;
	}
	
	public void removeEntity(Entity e) {
		_toRemove.add(e);
	}
	
	public ArrayList<Entity> getEntities() {
		return _entities;
	}
	
	public Vec2f getGameCoords() {
		return _gameCoords;
	}
	
	public void setGameCoords(Vec2f coords) {
		_gameCoords = coords;
	}
	
	public Vec2f getGameDims() {
		return _gameDims;
	}
	
	public void setGameDims(Vec2f dims) {
		_gameDims = dims;
	}
	
	public void setVP(Viewport vp) {
		_vp = vp;
	}
	
	public Viewport getVP() {
		return _vp;
	}
	
	public void onKeyPressed(KeyEvent e) {}

	public void onKeyReleased(KeyEvent e) {}
	
	public void onMousePressed(MouseEvent e) {}
	
	public void onMouseDragged(MouseEvent e) {}
	
	public void onMouseReleased(MouseEvent e) {}
	
	public void onMouseMoved(MouseEvent e) {}
	
	public Application getApp() {
		return _app;
	}

	public abstract void loseGame();
	
	public abstract void winGame();
	
	public abstract Player addPlayer(Shape s, float density, float restitution);
}
