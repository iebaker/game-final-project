package iebaker.argon.world;

import java.util.ArrayList;

public class GridGraph extends Graph implements Grid {  //LOL
	private int my_width;
	private int my_height;
	private Entity[][] my_entities;
	private java.util.List<Place> my_entity_places;
	private java.util.List<Place> planned_places;

	public GridGraph(int width, int height) {
		my_width = width;
		my_height = height;
		my_entities = new Entity[my_width][my_height];
		my_entity_places = new ArrayList<Place>() {	//Overridden so that you can't add a place more than once
			@Override
			public boolean add(Place p) {
				for(Place place : this) {
					if(p.equals(place)) {
						return false;
					}
				}
				super.add(p);
				return true;
			}
		};
		planned_places = new ArrayList<Place>();
		stitchGraph();
	}

	private void stitchGraph() {
		for(int x = 0; x < my_width; ++x) {
			for(int y = 0; y < my_height; ++y) {
				Place v = new Place(x, y, coordString(x, y));
				v.decorate("x", x+"");
				v.decorate("y", y+"");
				this.addVertex(v);
			}
		}

		for(int x = 0; x < my_width; ++x) {
			for(int y = 0; y < my_height; ++y) {
				Place v = this.get(new Place(x, y));
				if(x > 0) {
					Place left = this.get(new Place(x-1,y));
					this.addEdge(new Edge(v, left));
					this.addEdge(new Edge(left, v));
				}
				if(y > 0) {
					Place above = this.get(new Place(x, y-1));
					this.addEdge(new Edge(v, above));
					this.addEdge(new Edge(above, v));
				}
			}
		}
	}

	public boolean isRealPlace(Place p) {
		if(p != null) {
			return p.getX() >= 0 && p.getY() >= 0 && p.getX() < my_width && p.getY() < my_height;
		}
		return false;
	}

	public Place get(Place p) {
		if(isRealPlace(p)) {
			return (Place) this.getVertex(coordString(p.getX(), p.getY()));	
		} 
		return null;
	}

	public Entity addEntity(Place p, Entity e) {
		Entity prev = this.getEntity(p);
		my_entities[p.getX()][p.getY()] = e;
		my_entity_places.add(p);
		e.bind(this, p);
		return prev;
	}

	public Entity removeEntity(Place p) {
		Entity prev = this.getEntity(p);
		my_entity_places.remove(p);
		my_entities[p.getX()][p.getY()] = null;
		prev.free();
		return prev;
	}

	public void updateEntity(Place p, Entity e) {
		if(e.getPrevPlace() != null) {
			my_entities[e.getPrevPlace().getX()][e.getPrevPlace().getY()] = null;
			if(my_entities[p.getX()][p.getY()] != null) e.dealWith(my_entities[p.getX()][p.getY()]);
			my_entities[p.getX()][p.getY()] = e;
			my_entity_places.remove(e.getPrevPlace());
			my_entity_places.add(p);
		}
	}

	public Entity getEntity(Place p) {
		if(isRealPlace(p))
			return my_entities[p.getX()][p.getY()];
		return null;
	}

	public java.util.List<Entity> getAllEntities() {
		java.util.List<Entity> return_value = new ArrayList<Entity>();
		for(Place p : getEntityPlaces()) {
			Entity e = getEntity(p);
			if(e != null) return_value.add(e);
		}
		return return_value;
	}

	public java.util.List<Place> getEntityPlaces() {
		return my_entity_places;
	}

	public void clearPlannedPlaces() {
		planned_places = new ArrayList<Place>();
	}

	public void addPlannedPlace(Place p) {
		planned_places.add(p);
	}

	public java.util.List<Place> getPlannedPlaces() {
		return planned_places;
	}

	public boolean isAvailable(Place p) {
		return !planned_places.contains(p);
	}

	public int getXSize() {
		return my_width;
	}

	public int getYSize() {
		return my_height;
	}

	public Place getRandomPlace() {
		int rx = (int) (new java.util.Random().nextFloat() * my_width);
		int ry = (int) (new java.util.Random().nextFloat() * my_height);
		return this.get(new Place(rx, ry));
	}

	public java.util.List<Entity> getNeighbors(Place p) {
		if(p == null) return new ArrayList<Entity>();
		java.util.List<Entity> result = new ArrayList<Entity>(); 
		for(Place place : getAdjacentPlaces(p)) {
			Entity e = getEntity(place);
			if(e != null) {
				result.add(e);
			}
		}
		return result;
	}

	public java.util.List<Place> getAdjacentPlaces(Place p) {
		java.util.List<Place> result = new ArrayList<Place>();
		for(int i = -1; i <= 1; ++i) {
			for(int j = -1; j <= 1; ++j) {
				if(!(i == 0 && j == 0) && (i == 0 || j == 0)) {
					Place place = this.get(new Place(p.getX() + i, p.getY() + j));
					if(isRealPlace(place) && place.isActive()) {
						result.add(place);
					}
				}
			}
		}

		return result;
	}

	public java.util.List<Place> getEmptyAdjacentPlaces(Place p) {
		java.util.List<Place> result = new ArrayList<Place>();
		for(Place place : getAdjacentPlaces(p)) {
			Entity e = getEntity(place);
			if(e == null) {
				result.add(place);
			}
		}
		return result;
	}

	public java.util.List<Place> getOccupiedAdjacentPlaces(Place p) {
		java.util.List<Place> result = new ArrayList<Place>();
		for(Place place : getAdjacentPlaces(p)) {
			Entity e = getEntity(place);
			if(e != null) {
				result.add(place);
			}
		}
		return result;
	}

	public static String coordString(int x, int y) {
		return "(" + x + ", " + y + ")";
	}

	public java.util.List<Place> aStarPath(Place start, Place end, Metric m, Heuristic h) {
		java.util.List<Vertex> vertices = super.aStarPath(this.get(start), this.get(end), m, h);
		java.util.List<Place> places = new ArrayList<Place>();
		if(vertices != null) {
			for(Vertex v : vertices) {
				places.add((Place) v);
			}
		}
		return places;
	}
}