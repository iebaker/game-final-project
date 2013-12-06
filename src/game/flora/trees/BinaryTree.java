package game.flora.trees;

import game.flora.Rule;
import game.flora.Transformation;
import game.flora.Tree;
import game.flora.Branch;
import game.GameWorld;

import java.util.Set;
import java.util.HashSet;
import cs195n.Vec2f;

public class BinaryTree extends Tree {

	private float depth;

	public BinaryTree(int depth) {
		super();
		this.depth = depth;
	}

	@Override
	public Tree newTree(Set<Branch> b) {
		Tree t = new Tree(b);

		Transformation t1 = new Transformation((float)Math.PI/4, 0.5f, 0.4f);
		Transformation t2 = new Transformation(-(float)Math.PI/4, 0.5f, 0.6f);
		Transformation t3 = new Transformation((float)Math.PI/4, 0.5f, 0.8f);

		for(int i = 0; i < depth; ++i) {
			t.addRule(new Rule(t1, t2, t3));
			t.addRule(new Rule(t2, t3));
			t.addRule(new Rule(t3));
		}		

		t.grow();
		return t;
	}

	public static void treeTest(GameWorld world, java.awt.Graphics2D g) {
		Vec2f loc1 = world.getPlayer().shape.getCenter();
		Vec2f loc2 = loc1.plus(new Vec2f(0, -250));

		Set<Branch> branches = new HashSet<Branch>();
		branches.add(new Branch(loc1, loc2));

		Tree btree = new BinaryTree(3).newTree(branches);
		btree.onDraw(g);
	}
}