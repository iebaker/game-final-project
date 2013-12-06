package game.flora;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Color;

import engine.Viewport;
import engine.Artist;
import cs195n.Vec2f;

public class Tree {

	private List<Rule> rules;
	private Set<Branch> branches;
	private boolean grown = false;

	public Tree() {
		rules = new ArrayList<Rule>();
		branches = new HashSet<Branch>();
	}

	public Tree(Set<Branch> b) {
		rules = new ArrayList<Rule>();
		branches = b;
	}

	public Tree(Branch b) {
		rules = new ArrayList<Rule>();
		branches = new HashSet<Branch>();
		branches.add(b);
	}

	public Tree newTree(Set<Branch> b) {
		return new Tree(b);
	}

	public void grow() {
		if(grown) return;

		Set<Branch> open = new HashSet<Branch>(branches);
		for(Rule r : rules) {
			open = r.applyTo(open);
			branches.addAll(open);
		}
		grown = true;
	}

	public void onDraw(java.awt.Graphics2D g) {
		g.setStroke(new java.awt.BasicStroke(2));
		Artist a = new Artist();
		a.setStrokePaint(Color.BLACK);

		if(grown) {
			for(Branch b : branches) {
				Vec2f start = Viewport.gamePtToScreen(b.getStartPoint());
				Vec2f end = Viewport.gamePtToScreen(b.getEndPoint());

				a.line(g, start.x, start.y, end.x, end.y);
			}
		}
	}

	public void addRule(Rule r) {
		rules.add(r);
	}
}