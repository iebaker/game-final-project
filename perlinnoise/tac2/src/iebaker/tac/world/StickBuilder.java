package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import iebaker.argon.world.Sprite;
import java.util.List;
import java.util.ArrayList;




public class StickBuilder extends TacCreature {
	
	protected List<Place> path = new ArrayList<Place>();
	protected boolean moved = false;
	protected boolean atloc = false;

	public StickBuilder() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stick builder north"));
		passable = true;
	}

	@Override
	public void step() {
		moved = false; atloc = false;
		if(!target_places.isEmpty()) {
			if(my_place.equals(target_places.get(0))) {
				atloc = true;
				target_places.remove(0);
				if(target_places.isEmpty()) {
					path.add(my_place.getNextPlace(my_heading));
				}
			} 
			if(path.isEmpty()) {
				List<Place> path = pathTo(target_places.get(0));
				if(!path.isEmpty()) {
					setPath(pathTo(target_places.get(0)));
				} else {
					target_places.clear();
				}
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
			moved = true;
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
		if(moved && atloc) {
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