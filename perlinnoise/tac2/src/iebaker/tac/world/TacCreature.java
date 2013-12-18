package iebaker.tac.world;

import iebaker.argon.world.Place;
import iebaker.argon.world.Entity;
import iebaker.argon.world.Edge;
import iebaker.argon.world.Vertex;
import iebaker.argon.world.Metric;
import iebaker.argon.world.Heuristic;
import java.util.ArrayList; 
import iebaker.argon.world.Creature;
import iebaker.argon.world.Sprite;
import java.util.List;
import java.util.ArrayList;

public class TacCreature extends Creature {

	private int health = 5;

	public TacCreature(Place.Heading h, Sprite s) {
		super(h, s);
	}

	@Override 
	public java.util.List<Place> getPossibleMoveLocations() {

		java.util.List<Place> locations = my_gridgraph.getAdjacentPlaces(my_place);
		java.util.List<Place> return_value = new ArrayList<Place>();

		for(Place p : locations) {

			String terrain_value = Terrain.DEEP_WATER;

			try {
				terrain_value = Terrain.getTerrainType(Float.parseFloat(p.decoration("TERRAIN_VALUE")));
			} catch (Exception e) {
				System.err.println("Error in TacCreature.getPossibleMoveLocations() while checking TERRAIN_VALUE at place " + p);
			}

			Entity e = my_gridgraph.getEntity(p);
			boolean passable = (e instanceof Sticks) || (e instanceof Stones) || (e instanceof Ammo) || e == null;
			if(!terrain_value.equals(Terrain.DEEP_WATER) && my_gridgraph.isAvailable(p) && passable) {
				return_value.add(p);
			}
		}

		return return_value;
	}

	/**
	 * Ooh I'll make a flexible A star implementation tee hee anonymous classes this will be so much fun to debug 
	 *
	 *
	 * SAID NO ONE.  Fuck.
	 */
	public List<Place> pathTo(Place p) {
		List<Place> return_val = my_gridgraph.aStarPath(
			my_place,
			p,
			new Metric() {
				@Override
				public float measure(Edge e) {
					Place place = my_gridgraph.get((Place)e.getHead());
					Entity ent = my_gridgraph.getEntity((Place)e.getHead());
					//System.out.println(ent);
					boolean passable = (ent instanceof Sticks) || (ent instanceof Stones) || (ent instanceof Ammo) || ent == null;
					//System.out.println(passable);
					try {
						String terrain_type = Terrain.getTerrainType(place);
						//System.out.println(terrain_type);
						if(terrain_type.equals(Terrain.DEEP_WATER) || !passable) {
							//System.out.println("returning 100000");
							return 100000;
						}
					} catch(NumberFormatException exception) {
						System.err.println("(measure) A* Pathfinding in TacUniverse.onMouseClicked() failed because God hates you");
			 			return 100000;
					}
					//System.out.println("Returnin 1");
					return 1f;
				}
			},
			new Heuristic() {
				@Override
				public float score(Vertex v1, Vertex v2) {
					Place p1 = my_gridgraph.get((Place) v1);
					Place p2 = my_gridgraph.get((Place) v2);
					int x1 = p1.getX();
					int x2 = p2.getX();
					int y1 = p1.getY();
					int y2 = p2.getY();
					int dist = Math.abs(x2 - x1) + Math.abs(y2 - y1);
					return (float) dist;
				}
			}
		);
		if(!return_val.isEmpty()) return_val.remove(0);  //LOL NOT LIKE THIS IS A HACK just cause I don't want to touch Astar
		return return_val;
	}

	public void damage(int d) {
		health -= d;
	}

	public int getHealth() {

		return health;
	}

	@Override
	public void step() {
		if(health < 0) {
			my_gridgraph.removeEntity(my_place);
			return;
		}
		super.step();
	}

	public String getTeam() {
		return "stick";
	}
}