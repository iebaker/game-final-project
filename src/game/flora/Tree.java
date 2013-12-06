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
	private List<Set<Branch>> branches;
	private boolean populated = false;

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
		if(populated) return;

		Set<Branch> open = new HashSet<Branch>();
		if(!branches.isEmpty()) open.addAll(branches.get(0));
		for(Rule r : rules) {
			open = r.applyTo(open);
			branches.add(open);
		}
		populated = true;
	}

	public void onDraw(java.awt.Graphics2D g) {
		int width = branches.size();
		Artist a = new Artist();
		a.setStrokePaint(Color.BLACK);

		if(populated) {
			for(Set<Branch> bs : branches) {
				g.setStroke(new java.awt.BasicStroke(width));
				for(Branch b : bs) {
					Vec2f start = Viewport.gamePtToScreen(b.getStartPoint());
					Vec2f end = Viewport.gamePtToScreen(b.getEndPoint());

					a.line(g, start.x, start.y, end.x, end.y);
				}
				width--;
			}
		}
	}

	public void addRule(Rule r) {
		rules.add(r);
	}
}