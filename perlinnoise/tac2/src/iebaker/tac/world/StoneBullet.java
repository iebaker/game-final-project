package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import iebaker.argon.world.Creature;
import java.util.List;
import java.util.ArrayList;

public class StoneBullet extends Creature {
	public StoneBullet(Place.Heading h) {
		super(h, Sprites.group("entities").sprite("stone bullet"));
		passable = true;
	}

	@Override
	public Place selectNextPlace(List<Place> move_locations) {
		Place next = my_place.getNextPlace(my_heading);
		return next;
	}

	@Override
	public void dealWith(Entity e) {
		if(e instanceof TacCreature) {
			TacCreature tc = (TacCreature) e;
			if(tc.getTeam() == "stick") tc.damage(1);
		}
	}

	@Override
	public boolean isBullet() {  //I won't tell if you don't
		return true;
	}
}