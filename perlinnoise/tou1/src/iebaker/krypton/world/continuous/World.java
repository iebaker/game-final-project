package iebaker.krypton.world.continuous;

import java.util.HashSet;
import java.awt.Graphics2D;
import iebaker.krypton.core.Artist;
import java.awt.Color;

import cs195n.Vec2f;

public class World {
	private java.util.Set<Entity> born = new HashSet<Entity>();
	private java.util.Set<Entity> my_entities = new HashSet<Entity>();
	private java.util.Set<Entity> dead = new HashSet<Entity>();
	private Vec2f my_size;

	public World(Vec2f size) {
		my_size = size;
	}

	public void onTick(long nanos) {
		for(Entity e : dead) {
			my_entities.remove(e);
		}
		for(Entity e : born) {
			my_entities.add(e);
		}
		born = new HashSet<Entity>();
		dead = new HashSet<Entity>();
		checkInteractions();
		checkEdges();
		for(Entity pe : my_entities) {
			pe.onTick(nanos);
		}
	}	

	public Vec2f getSize() {
		return my_size;
	}		

	public void onDraw(Graphics2D g) {
		//Artist a = new Artist();
		//a.setFillPaint(Color.RED);
		//a.ellipse(g, 25, 25, 100, 100);
		for(Entity pe : my_entities) {
			pe.onDraw(g);
		}
	}

	public boolean bindEntity(Entity pe) {
		return born.add(pe);
	}

	public boolean freeEntity(Entity pe) {
		return dead.add(pe);
	}

	public boolean freeAll() {
		return dead.addAll(my_entities);
	}

	private void checkInteractions() {
		for(Entity pe1 : my_entities) {
			for(Entity pe2 : my_entities) {
				if(pe1 != pe2) {
					pe1.onCollisionCheck(pe2);
					pe1.actOn(pe2);
				}
			}
		}
	}

	private void checkEdges() {
		for(Entity e : my_entities) {
			e.onEdgeCheck();
		}
	}

	public Vec2f getRandomLocation() {
		float x = (float)(Math.random() * my_size.x);
		float y = (float)(Math.random() * my_size.y);
		return new Vec2f(x, y);
	}


}
