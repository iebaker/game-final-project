package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import java.util.List;
import java.util.ArrayList;

public class StickSoldier extends TacCreature {
	
	protected List<Place> path = new ArrayList<Place>();
	protected boolean corner = false;
	protected int targetnum = -1;

	public StickSoldier() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stick soldier"));
		passable = true;
	}

	@Override
	public void step() {
		corner = false;
		if(!target_places.isEmpty()) {
			if(my_place.equals(target_places.get(0))) {
				++targetnum;
				target_places.add(target_places.remove(0));
				corner = true;
			}
			if(path.isEmpty()) {
				setPath(pathTo(target_places.get(0)));
			}
		}
		super.step();
	}

	@Override
	public Place selectNextPlace(List<Place> move_locations) {
		if(path.isEmpty()) {
			return my_place;
		} else {
			Place target = path.get(0);
			if(move_locations.contains(target)) {
				path.remove(0);
				return target;
			} else {
				return my_place;
			}
		}
	}

	@Override
	public void setPath(List<Place> path) {
		this.path = path;
	}

	@Override
	public void spawn() {
		if(new java.util.Random().nextFloat() > 0.5) return;
		if(corner || target_places.isEmpty()) return;

		Place.Heading left = my_heading.plus(Place.Turn.LEFT);
		Place.Heading right = my_heading.plus(Place.Turn.RIGHT);

		Place n_left = my_place.getNextPlace(left);
		Place n_right = my_place.getNextPlace(right);

		if(my_gridgraph.isRealPlace(n_left)) my_gridgraph.addEntity(n_left, new StickBullet(left));
		if(my_gridgraph.isRealPlace(n_right)) my_gridgraph.addEntity(n_right, new StickBullet(right));
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