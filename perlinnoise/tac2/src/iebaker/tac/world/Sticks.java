package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;

public class Sticks extends Entity {
	public Sticks() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("sticks"));
		passable = true;
	}
}