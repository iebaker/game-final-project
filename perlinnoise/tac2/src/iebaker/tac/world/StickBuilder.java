package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import iebaker.argon.world.Sprite;
import java.util.List;
import java.util.ArrayList;


/**
 * Stickbuilders don't work... I dont' fucking know why.  I tried for several hours.  There's something wrong somewhere in Astar or Graph or
 * idk any of the 10000000 other things happening. It's not a requirement so I give up.  I'm losing my mind.  I never want to think about, or
 * be around, or hear about, or see a tactical strategy game ever again in my life.  Platformers ftw.  
 */
public class StickBuilder extends TacCreature {
	
	private List<Place> path = new ArrayList<Place>();
	private boolean moved = false;

	public StickBuilder() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stick builder north"));
	}

	@Override
	public void step() {
		moved = false;
		if(!target_places.isEmpty()) {
			if(my_place.equals(target_places.get(0))) {
				moved = true;
				target_places.remove(0);
				if(target_places.isEmpty()) {
					path.add(my_place.getNextPlace(my_heading));
				}
			} 
			if(path.isEmpty()) {
				System.out.println("This will either print once, or forever...");
				setPath(pathTo(target_places.get(0)));
			}
		}
		super.step();
	}

	@Override
	public Place selectNextPlace(List<Place> move_locations) {

		if(path.isEmpty()) {
			return my_place;
		} 

		Place target = path.get(0);
		if(move_locations.contains(target)) {
			path.remove(0);
			return target;
		} else {
			if(move_locations.isEmpty()) {
				return my_place;
			} else {
				path = new ArrayList<Place>();
				return my_place;
			}
		}
	}

	@Override
	public void spawn() {
		if(moved) {
			my_gridgraph.addEntity(my_prev_place, new StickWall());
		} 
	}

	@Override
	public void setPath(List<Place> path) {
		this.path = path;
	}

	@Override
	public Sprite getSprite() {
		switch(my_heading) {
			case NORTH:
				return Sprites.group("entities").sprite("stick builder north");
			case SOUTH:
				return Sprites.group("entities").sprite("stick builder south");
			case EAST:
				return Sprites.group("entities").sprite("stick builder east");
			case WEST:
				return Sprites.group("entities").sprite("stick builder west");
		}
		return my_sprite;
	}

	@Override
	public void dealWith(Entity e) {
		if(e instanceof Sticks) {
			System.out.println("Sticks!");
		} else if(e instanceof Stones) {
			System.out.println("Stones!");
		}
	}
}