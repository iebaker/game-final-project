package iebaker.xenon.core;

import cs195n.Vec2f;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics2D;
import iebaker.xenon.geom.Shape;
import iebaker.xenon.util.Collisions;
import iebaker.xenon.util.Intersection;
import iebaker.xenon.util.Ray;

public class World implements Viewable {
	protected java.util.List<Entity> my_entities = new ArrayList<Entity>();
	protected java.util.Map<String, Entity> my_entity_map = new HashMap<String, Entity>();
	private java.util.List<Entity> births = new ArrayList<Entity>();
	private java.util.List<Entity> deaths = new ArrayList<Entity>();
	private Vec2f my_size;

	public World(Vec2f size) {
		my_size = size;
	}

	public void init() {
		return;
	}

	public void update(float dt) {
		this.updateEntities(dt);
		this.collideEntities();
		this.mediateEffects();
		this.updateEnvironment(dt);
		this.birth();
		this.reap();
	}

	public void updateEntities(float dt) {
		for(Entity e : my_entities) {
			e.update(dt);
		}
	}

	public void collideEntities() {
		for(int i = 0; i < my_entities.size(); ++i) {
			for(int j = i; j < my_entities.size(); ++j) {
				Collisions.resolve(my_entities.get(i), my_entities.get(j));
			}
		}
	}

	public void mediateEffects() {
		for(Entity e : my_entities) {
			e.affectWorld();
			this.affect(e);
		}
	}

	public void render(Artist a, Graphics2D g) {
		this.renderEnvironment(a, g);
		this.renderEntities(a, g);
	}

	public void affect(Entity e1) {
		return;
	}

	protected void updateEnvironment(float dt) {
		return;
	}

	private void renderEnvironment(Artist a, Graphics2D g) {
		return;
	}

	private void renderEntities(Artist a, Graphics2D g) {
		for(Entity e : my_entities) {
			e.render(a, g);
		}
	}

	protected void birth() {
		for(Entity e : births) {
			my_entities.add(e);
			my_entity_map.put(e.getName(), e);
		}
		births = new ArrayList<Entity>();
	}

	protected void reap() {
		for(Entity dead : deaths) {
			my_entities.remove(dead);
			my_entity_map.remove(dead.getName());
		}
		deaths = new ArrayList<Entity>();
	}

	public void addEntity(Entity e) {
		births.add(e);
	}

	public Entity getEntity(String name) {
		return my_entity_map.get(name);
	}

	public void removeEntity(Entity e) {
		deaths.add(e);
	}

	public java.util.List<Entity> getEntities() {
		return my_entities;
	}

	public Ray castRay(Entity entity, Vec2f source, Vec2f direction, float mag) {

		Ray r = new Ray(entity, source, direction, mag);
		
		for(Entity e : my_entities) {

			Vec2f intersect = Collisions.checkRayCast(r, e.getShape());

			if(intersect != null) {
				Intersection temp = new Intersection(e, intersect);
				if(temp != null) {
					r.addPair(temp.getIntersect().dist(source), temp);
				}
			}
		}
		return r;
	}

	public Vec2f getRandomPoint() {
		float x = (float) (Math.random() * my_size.x);
		float y = (float) (Math.random() * my_size.y);
		return new Vec2f(x, y);
	}

	public Vec2f getInitialCenter() {
		return new Vec2f(my_size.x/2, my_size.y/2);
	}

	public float getInitialScale() {
		return 1f;
	}
}
