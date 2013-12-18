package iebaker.argon.world;

import java.lang.Class;
import java.util.ArrayList;
import iebaker.argon.core.Artist;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.Color;
import cs195n.*;

public class Universe {
	protected GridGraph my_gridgraph;
	protected float my_gridwidth;
	private java.util.List<Class<?>> my_entity_classes = new ArrayList<Class<?>>();
	private Entity selected_entity;
	private long steplength = 1000;
	private long countdown = steplength;

	public Universe(GridGraph g, float gridwidth) {
		my_gridgraph = g;
		my_gridwidth = gridwidth;
	}

	public Entity addEntity(Place p, Entity e) {
		//if(my_gridgraph.getEntity(p) == null) {
			return my_gridgraph.addEntity(p, e);
		//}
		//return null;
	}

	public void updateEntity(Place p, Entity e) {
		my_gridgraph.updateEntity(p, e);
	}

	public Entity removeEntity(Place p) {
		return my_gridgraph.removeEntity(p);
	}

	public void addEntityClass(Class<?> c) {
		my_entity_classes.add(c);
	}

	public GridGraph getGridGraph() {
		return my_gridgraph;
	}

	public java.util.List<Class<?>> getEntityClasses() {
		return my_entity_classes;
	}

	public boolean onMouseClicked(Place p, MouseEvent e) {
		Entity ent = my_gridgraph.getEntity(p);
		if(ent != null && ent.selectable()) {
			selected_entity = ent;
			return true;
		}
		for(Entity trying : my_gridgraph.getNeighbors(p)) {
			if(trying != null && trying.selectable()) {
				selected_entity = trying;
				return true;
			}
		}
		return false;
	}

	public java.util.List<Entity> getEntitiesByClass(Class<?> c) {
		java.util.List<Entity> return_value = new ArrayList<Entity>();
		for(Place p : my_gridgraph.getEntityPlaces()) {
			Entity e = my_gridgraph.getEntity(p);
			if(e == null) System.out.println("GridGraph.getEntityPlaces() returned an unoccupied location.  Check that...");
			else return_value.add(e);
		}
		return return_value;
	}

	public Entity getSelectedEntity() {
		return selected_entity;
	}

	public float getGridwidth() {
		return my_gridwidth;
	}

	public float getTotalWidth() {
		return my_gridwidth * my_gridgraph.getXSize();
	}

	public float getTotalHeight() {
		return my_gridwidth * my_gridgraph.getYSize();
	}

	public Vec2f getStartingCenter() {
		return new Vec2f(getTotalWidth()/2, getTotalHeight()/2);
	}

	public float getStartingScale() {
		return 0.5f;
	}

	public void onTick(long nanos) {
		countdown -= nanos/1E6;
		if(countdown < 0) {
			my_gridgraph.clearPlannedPlaces();
			for(Entity e : my_gridgraph.getAllEntities()) {
				e.step();
			}
			countdown = steplength;
		}
	}

	public void onDraw(Artist a, Graphics2D g) {
		for(Place place : my_gridgraph.getEntityPlaces()) {
			Entity e = my_gridgraph.getEntity(place);
			if(e == null) continue;
			Place current = e.getPlace();
			if(e == selected_entity) {
				a.setFillPaint(new Color(0f,0f,1f,0.5f));
				a.rect(g, current.getX()*my_gridwidth, current.getY()*my_gridwidth, my_gridwidth, my_gridwidth);

				if(e.selectable() && e.showPath()) {
					for(Place p : ((Creature)e).getTargetPlaces()) {
						a.rect(g, p.getX()*my_gridwidth, p.getY()*my_gridwidth, my_gridwidth, my_gridwidth);
					}
				}
			}
			Place past = e.getPrevPlace();
			float anim_percent = ((float)steplength - (float)countdown)/(float)steplength;
			float transition_x = (float)past.getX() + anim_percent * (float)(current.getX() - past.getX());
			float transition_y = (float)past.getY() + anim_percent * (float)(current.getY() - past.getY());

			int frame = 0;
			if(!past.equals(current)) {
				frame = (int) Math.floor(anim_percent * (float) e.getSprite().frameCount());
			}
			if(frame == e.getSprite().frameCount()) {
				--frame;
			}
			e.getSprite().frame(frame).drawSelf(g, (int)(transition_x * my_gridwidth), (int)(transition_y * my_gridwidth), (int)my_gridwidth, (int)my_gridwidth);
		}
	}
}