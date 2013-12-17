package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;

public class StickWall extends Entity {
	public StickWall() {
		super(Place.Heading.NORTH, Sprites.group("entities").sprite("stick wall"));
	}
}