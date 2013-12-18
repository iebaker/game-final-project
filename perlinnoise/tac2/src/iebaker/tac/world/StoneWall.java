package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;

public class StoneWall extends Entity {
	public StoneWall() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stone wall"));
	}
}