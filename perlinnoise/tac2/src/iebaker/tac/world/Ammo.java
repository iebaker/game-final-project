package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;

public class Ammo extends Entity {
	public Ammo() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("ammo"));
	}
}