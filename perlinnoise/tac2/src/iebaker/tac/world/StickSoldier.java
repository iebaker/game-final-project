package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import java.util.List;
import java.util.ArrayList;

public class StickSoldier extends TacCreature {
	
	private List<Place> path = new ArrayList<Place>();

	public StickSoldier() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stick soldier"));
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
	public void dealWith(Entity e) {
		if(e instanceof Sticks) {
			System.out.println("Sticks!");
		} else if(e instanceof Stones) {
			System.out.println("Stones!");
		}
	}
}