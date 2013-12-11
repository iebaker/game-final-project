package engine.collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import cs195n.Vec2f;
import engine.Vec2fPair;
import engine.entity.Entity;

public class QuadTree {

	private int CAPACITY = 8;
	private int DEPTH = 15;
	private int my_depth;
	private List<Entity> my_entities = new ArrayList<Entity>();
	private Vec2f my_min_pt;
	private Vec2f my_max_pt;
	private QuadTree[] my_children;
	private boolean is_leaf = true;

	public QuadTree(Vec2f min, Vec2f max) {
		my_depth = 0;
		my_min_pt = min;
		my_max_pt = max;
		my_children = new QuadTree[4];
	}

	public QuadTree(int depth, Vec2f min, Vec2f max) {
		my_depth = depth;
		my_min_pt = min;
		my_max_pt = max;
		my_children = new QuadTree[4];
	}

	public void clear() {
		my_entities = new ArrayList<Entity>();
		is_leaf = true;

		for(int i = 0; i < 4; ++i) {
			if(!(my_children[i] == null)) my_children[i].clear();
			my_children[i] = null;
		}
	}

	public void insert(Entity e) {
		//System.out.println("In insert!");
		if(!is_leaf) {
			for(Integer i : indicesOf(e)) {
				my_children[i].insert(e);
			}
			return;
		}

		my_entities.add(e);

		if(my_entities.size() > CAPACITY && !(my_depth >= DEPTH)) {
			split();
		}
	}

	public void split() {
		is_leaf = false;

		float hMid = (my_min_pt.x + my_max_pt.x) / 2;
		float vMid = (my_min_pt.y + my_max_pt.y) / 2;

		my_children[0] = new QuadTree(my_depth + 1, new Vec2f(hMid, my_min_pt.y), new Vec2f(my_max_pt.x, vMid));
		my_children[1] = new QuadTree(my_depth + 1, my_min_pt, new Vec2f(hMid, vMid));
		my_children[2] = new QuadTree(my_depth + 1, new Vec2f(my_min_pt.x, vMid), new Vec2f(hMid, my_max_pt.y));
		my_children[3] = new QuadTree(my_depth + 1, new Vec2f(hMid, vMid), my_max_pt);

		for(Entity e : my_entities) {
			insert(e);
		}
		
		my_entities = new ArrayList<Entity>();
	}

	public List<Integer> indicesOf(Entity e) {
		List<Integer> indices = new ArrayList<Integer>();
		if(e.shape == null) return new ArrayList<Integer>();
		Vec2fPair points = e.shape.getBoundingBox();

		Vec2f min = points.getP1();
		Vec2f max = points.getP2();

		float hMid = (my_min_pt.x + my_max_pt.x) / 2;
		float vMid = (my_min_pt.y + my_max_pt.y) / 2;

		if(overlaps(min, max, new Vec2f(hMid, my_min_pt.y), new Vec2f(my_max_pt.x, vMid))) {
			indices.add(0);
		}

		if(overlaps(min, max, my_min_pt, new Vec2f(hMid, vMid))) {
			indices.add(1);
		}

		if(overlaps(min, max, new Vec2f(my_min_pt.x, vMid), new Vec2f(hMid, my_max_pt.y))) {
			indices.add(2);
		}

		if(overlaps(min, max, new Vec2f(hMid, vMid), my_max_pt)) {
			indices.add(3);
		}

		return indices;
	}

	public boolean overlaps(Vec2f min1, Vec2f max1, Vec2f min2, Vec2f max2) {
		boolean overlapX = min1.x < max2.x && max1.x > min2.x;
		boolean overlapY = min1.y < max2.y && max1.y > min2.y;
		return overlapX && overlapY;
	}

	public Collection<Entity> getPotentialCollisions(Entity e) {
		//System.out.println("In getpotentialcollisions!");
		if(is_leaf) {
			return my_entities;
		} else {
			Collection<Entity> ret_val = new HashSet<Entity>();
			for(Integer i : indicesOf(e)) {
				ret_val.addAll(my_children[i].getPotentialCollisions(e));
			}
			return ret_val;
		}
	}

	public boolean isLeaf() {
		return is_leaf;
	}

	public List<Entity> getEntities() {
		return my_entities;
	}

	public void remove(Entity a) {
		if(is_leaf) {
			my_entities.remove(a);
			return;
		}
		
		for(Integer i : indicesOf(a)) {
			my_children[i].remove(a);
		}
	}
}