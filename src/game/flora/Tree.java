package game.flora;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs195n.LevelData;
import cs195n.Vec2f;
import engine.Artist;
import engine.Viewport;
import engine.World;
import engine.collision.AAB;
import engine.collision.CollisionInfo;
import engine.entity.PassableEntity;
import engine.lighting.LightSource;
import game.GameWorld;


public class Tree extends PassableEntity {

	private static final long serialVersionUID = -7955399708850859349L;
	private final List<Rule>		rules;
	private final List<Set<Branch>>	branches;
	private final List<Set<Branch>>	grown		= new ArrayList<Set<Branch>>();
	private Set<Branch>				fringe		= new HashSet<Branch>();
	private boolean					populated	= false;
	private boolean 				enoughLight = false;
	private float					percent		= 1.0f;
	private final float				growthrate	= 0.2f;
	
	public Tree() {
		super();
		rules = new ArrayList<Rule>();
		branches = new ArrayList<Set<Branch>>();
		this.init();
	}
	
	@Override
	public void setProperties(LevelData.EntityData ed, World world) {
		super.setProperties(ed, world);

		AAB me = (AAB) this.shape;

		Vec2f min = me.getMin();
		Vec2f max = me.getMax();

		float xAvg = (min.x + max.x) / 2f;

		Branch b = new Branch(new Vec2f(xAvg, max.y), new Vec2f(xAvg, min.y));
		Set<Branch> bs = new HashSet<Branch>();
		bs.add(b);
		branches.add(bs);
		this.populate();
	}

	public void init() {
		return;
	}
	
	public void populate() {
		if (populated) return;
		
		Set<Branch> open = new HashSet<Branch>();
		if (!branches.isEmpty()) open.addAll(branches.get(0));
		for (Rule r : rules) {
			open = r.applyTo(open);
			branches.add(open);
		}
		populated = true;
	}
	
	@Override
	public void onTick(float seconds) {

		checkLightLevels();
		if(!enoughLight) return;

		if (percent >= 1) {
			
			if (!fringe.isEmpty()) grown.add(fringe);
			
			if (!branches.isEmpty()) {
				fringe = branches.get(0);
				branches.remove(0);
			} else {
				fringe = new HashSet<Branch>();
			}
			percent = 0.0f;
			
		} else {
			percent += growthrate;
		}
	}

	private void checkLightLevels() {
		if(this.shape == null) return;
		GameWorld gameworld = (GameWorld) this.world;

		Vec2f myLoc = ((AAB) this.shape).getMax();

		for(LightSource ls : gameworld.getLightSources()) {
			Vec2f loc = ls.getLocation();
			if(myLoc.dist(loc) <= 500) enoughLight = true;
		}
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		if (!populated) return;
		
		Artist a = new Artist();
		a.setStrokePaint(Color.BLACK);
		float x = 10;
		g.setStroke(new java.awt.BasicStroke(Viewport.gameFloatToScreen(x)));
		
		for (Set<Branch> current : grown) {
			x = 0.75f * x;
			for (Branch branch : current) {
				g.setStroke(new java.awt.BasicStroke(Viewport.gameFloatToScreen(x)));
				Vec2f start = Viewport.gamePtToScreen(branch.getStartPoint());
				Vec2f end = Viewport.gamePtToScreen(branch.getEndPoint());
				
				a.line(g, start.x, start.y, end.x, end.y);
			}
		}
		
		for (Branch branch : fringe) {
			Vec2f start = Viewport.gamePtToScreen(branch.getStartPoint());
			Vec2f end = Viewport.gamePtToScreen(branch.getStartPoint().lerpTo(branch.getEndPoint(), percent));
			
			a.line(g, start.x, start.y, end.x, end.y);
		}
	}
	
	public void addRule(Rule r) {
		rules.add(r);
	}
	
	@Override
	public void onCollide(CollisionInfo c) {};
}