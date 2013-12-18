package iebaker.tac.world;

import iebaker.argon.world.Sprite;
import iebaker.argon.world.GridGraph;
import iebaker.argon.world.PerlinSampler;
import iebaker.argon.world.Universe;
import iebaker.argon.world.Place;
import iebaker.argon.world.Entity;
import iebaker.argon.world.Creature;
import iebaker.argon.world.Metric;
import iebaker.argon.world.Heuristic;
import iebaker.argon.world.Vertex;
import iebaker.argon.world.Edge;
import iebaker.argon.world.Sprites;
import iebaker.argon.core.Artist;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class TacUniverse extends Universe {
	{
		try {
			Sprites.init("lib/sprites.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Sprite deep_water_sprite;
	private Sprite shallow_water_sprite;
	private Sprite sand_sprite;
	private Sprite dirt_sprite;
	private Sprite grass_sprite;


	private PerlinSampler my_sampler;

	public TacUniverse(GridGraph g, float gridwidth, int samplingfactor) {
		super(g, gridwidth);

		my_sampler = new PerlinSampler(this.my_gridwidth * (float)samplingfactor,
			(int) Math.ceil(this.getTotalWidth()/samplingfactor) + 1,
			(int) Math.ceil(this.getTotalHeight()/samplingfactor) + 1, 2);

		deep_water_sprite = Sprites.group("terrain").sprite("deep_water");
		shallow_water_sprite = Sprites.group("terrain").sprite("shallow_water");
		sand_sprite = Sprites.group("terrain").sprite("sand");
		dirt_sprite = Sprites.group("terrain").sprite("dirt");
		grass_sprite = Sprites.group("terrain").sprite("grass");

		makeTerrain();	
		addStoneEntities();	
	}

	private void addStoneEntities() {
		Place p;
		while(true) {
			p = my_gridgraph.getRandomPlace();
			if(Terrain.getTerrainType(p).equals(Terrain.DEEP_WATER)) {
				continue;
			}
			break;
		}
		addEntity(p, new StoneBuilder());
		while(true) {
			p = my_gridgraph.getRandomPlace();
			if(Terrain.getTerrainType(p).equals(Terrain.DEEP_WATER)) {
				continue;
			}
			break;
		}
		addEntity(p, new StickBuilder());
	}

	private void makeTerrain() {

		for(int x = 0; x < my_gridgraph.getXSize(); ++x) {
			for(int y = 0; y < my_gridgraph.getYSize(); ++y) {

				Place current = (Place) this.my_gridgraph.getVertex(GridGraph.coordString(x, y));

				float sample_x_val = x * my_gridwidth + my_gridwidth/2;
				float sample_y_val = y * my_gridwidth + my_gridwidth/2;

				float sampled_value = my_sampler.sample(sample_x_val, sample_y_val);
				current.decorate("TERRAIN_VALUE", sampled_value + "");

				if(Terrain.getTerrainType(sampled_value).equals(Terrain.DEEP_WATER))
					current.setActive(false);
			} 
		}

	}

	@Override
	public void onDraw(Artist a, Graphics2D g) {

		a.strokeOff();

		int x_dp = 0;
		int y_dp = 0;
		int gwi = (int) my_gridwidth;

		for(int x = 0; x < my_gridgraph.getXSize(); ++x) {
			y_dp=0;
			for(int y = 0; y < my_gridgraph.getYSize(); ++y) {
				Place current = (Place) this.my_gridgraph.getVertex(GridGraph.coordString(x, y));

				float terrain_value = 0.0f;

				try {
					terrain_value = Float.parseFloat(current.decoration("TERRAIN_VALUE"));
				} catch (NumberFormatException e) {
					System.err.println("Error in parsing terrain value at (" + x + ", " + y + ").");
				}

				String terrain_type = Terrain.getTerrainType(terrain_value);
				switch(terrain_type) {
					case Terrain.DEEP_WATER:
						deep_water_sprite.frame(0).drawSelf(g, x_dp, y_dp, gwi, gwi);
						break;

					case Terrain.SHALLOW_WATER:
						shallow_water_sprite.frame(0).drawSelf(g, x_dp, y_dp, gwi, gwi);
						break;

					case Terrain.DIRT:
						dirt_sprite.frame(0).drawSelf(g, x_dp, y_dp, gwi, gwi);
						break;

					case Terrain.GRASS:
						grass_sprite.frame(0).drawSelf(g, x_dp, y_dp, gwi, gwi);
						break;

					case Terrain.SAND:
						sand_sprite.frame(0).drawSelf(g, x_dp, y_dp, gwi, gwi);
						break;
				}

				// try {
				// 	a.setTextAlign(a.TOP, a.LEFT);
				// 	a.setFontSize(50);
				// 	a.setFillPaint(Color.BLACK);
				// 	String asp_str = current.decoration("A_STAR_F");
				// 	if(asp_str != null) {
				// 		a.text(g, asp_str, (float)x_dp, (float)y_dp);
				// 	}
				// } catch (NumberFormatException e) {
				// 	System.err.println("oops");
				// }
				y_dp+=gwi;
			}
			x_dp+=gwi;
		}
		super.onDraw(a, g);
	}

	@Override
	public void onTick(long nanos) {
		spawnThings();
		super.onTick(nanos);
	}

	private void spawnThings() {
		Place random = this.getGridGraph().getRandomPlace();
		String type = Terrain.getTerrainType(random);
		if(!type.equals(Terrain.DEEP_WATER) && !type.equals(Terrain.SHALLOW_WATER)) {
			if(new java.util.Random().nextFloat() > 0.01) return;
			float choice = new java.util.Random().nextFloat();
			if(choice > 0.6) {
				this.addEntity(random, new Sticks());
			} else if(choice > 0.3) {
				this.addEntity(random, new Stones());
			} else {
				this.addEntity(random, new Ammo());
			}
		}	
	}

	public void onKeyPressed(KeyEvent e) {
		Entity ent = this.getSelectedEntity();
		if(ent != null && ent instanceof Creature) {
			((Creature)ent).clearTargets();
		}
	}

	@Override
	public boolean onMouseClicked(Place p, MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON3) {

			Entity ent = this.getSelectedEntity();

			if(ent != null && this.getGridGraph().isRealPlace(p)) {
				Creature c = (Creature) ent;
				c.addTargetPlace(p);
				return true;
			}
			return false;

		} else {
			return super.onMouseClicked(p, e);
		}
	}

	public int countStickEntities() {
		int ret = 0;
		for(Entity e : my_gridgraph.getAllEntities()) {
			if(e instanceof TacCreature) {
				TacCreature t = (TacCreature) e;
				if(t.getTeam() == "stick") ++ret;
			}
		}
		return ret;
	}

	public int countStoneEntities() {
		int ret = 0;
		for(Entity e : my_gridgraph.getAllEntities()) {
			if(e instanceof TacCreature) {
				TacCreature t = (TacCreature) e;
				if(t.getTeam() == "stone") ++ret;
			}
		}
		return ret;
	}
}