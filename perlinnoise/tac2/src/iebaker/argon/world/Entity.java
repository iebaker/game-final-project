package iebaker.argon.world;

public class Entity {
	protected GridGraph my_gridgraph = null;
	protected Place my_place = new Place(-1, -1);
	protected Place my_prev_place = new Place(-1, -1);
	protected Place.Heading my_heading;
	protected boolean animate = true;
	protected boolean selectable = false;
	protected boolean passable = false;
	protected boolean showpath = true;
	protected Sprite my_sprite;

	public Entity(Place.Heading h, Sprite s) {
		my_heading = h;
		my_sprite = s;
	}

	public void setPlace(Place p) {
		my_prev_place = my_place;
		my_place = my_gridgraph.get(p);
		my_gridgraph.updateEntity(p, this);
	}

	public void setHeading(Place.Heading h) {
		my_heading = h;
	}

	public boolean selectable() {
		return selectable;
	}

	public Place.Heading getHeading() {
		return my_heading;
	}

	public void step() {
		return;
	}

	public void bind(GridGraph grid, Place p) {
		my_gridgraph = grid;
		my_place = p;
		my_prev_place = p;
	}

	public boolean isBound() {
		return my_gridgraph != null && my_place != null;
	}

	public void free() {
		my_gridgraph = null;
		my_place = null;
	}

	public Place getPlace() {
		return my_place;
	}

	public Place getPrevPlace() {
		return my_prev_place;
	}

	public GridGraph getGridGraph() {
		return my_gridgraph;
	}

	public void animateOff() {
		animate = false;
	}

	public void animateOn() {
		animate = true;
	}

	public boolean isAnimated() {
		return animate;
	}

	public Sprite getSprite() {
		return my_sprite;
	}

	public void dealWith(Entity e) {
		return;
	}

	public boolean isBullet() {
		return false;
	}

	public boolean isPassable() {
		return passable;
	}

	public boolean showPath() {
		return showpath;
	}
}
