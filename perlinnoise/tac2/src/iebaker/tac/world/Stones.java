package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;

public class Stones extends Entity {
	public Stones() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stones"));
		passable = true;
	}
}