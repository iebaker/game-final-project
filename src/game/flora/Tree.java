package game.flora;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cs195n.Vec2f;
import engine.Artist;
import engine.Viewport;

public class Tree {
	
	private final List<Rule>		rules;
	private final List<Set<Branch>>	branches;
	private final List<Set<Branch>>	grown		= new ArrayList<Set<Branch>>();
	private Set<Branch>				fringe		= new HashSet<Branch>();
	private boolean					populated	= false;
	private float					percent		= 1.0f;
	private final float				growthrate	= 0.2f;
	
	public Tree() {
		rules = new ArrayList<Rule>();
		branches = new ArrayList<Set<Branch>>();
	}
	
	public Tree(Set<Branch> b) {
		rules = new ArrayList<Rule>();
		branches = new ArrayList<Set<Branch>>();
		branches.add(b);
	}
	
	public Tree(Branch b) {
		rules = new ArrayList<Rule>();
		branches = new ArrayList<Set<Branch>>();
		Set<Branch> branch = new HashSet<Branch>();
		branch.add(b);
		branches.add(branch);
	}
	
	public Tree newTree(Set<Branch> b) {
		return new Tree(b);
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
	
	public void onTick(long nanos) {
		// System.out.println();
		// System.out.println(percent);
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
		// System.out.println(percent);
	}
	
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
}