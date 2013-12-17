package iebaker.argon.world;

import java.util.ArrayList;
import java.util.List;

public class Creature extends Entity {

	protected List<Place> target_places = new ArrayList<Place>();

	public Creature(Place.Heading h, Sprite s) {
		super(h, s);
		selectable = true;
	}

	@Override
	public void step() {
		dealWithEntities(getRelevantEntities());
		Place p = selectNextPlace(getPossibleMoveLocations());
		moveToPlace(p);
		spawn();
	}

	public void spawn() {
		return;
	}

	public java.util.List<Entity> getRelevantEntities() {
		return new ArrayList<Entity>();
	}

	public void dealWithEntities(java.util.List<Entity> entities) {
		return;
	}

	public Place selectNextPlace(java.util.List<Place> move_locations) {
		return my_place;
	}

	public java.util.List<Place> getPossibleMoveLocations() {
		return new ArrayList<Place>();
	}

	public void moveToPlace(Place p) {
		Place.Heading newHeading = my_place.approxHeadingTo(p);
		this.setHeading(newHeading);
		this.setPlace(p);
	}

	public void setPath(java.util.List<Place> path) {
		return;
	}

	public void addTargetPlace(Place p) {
		target_places.add(p);
	}

	public List<Place> getTargetPlaces() {
		return target_places;
	}
}