package game.flora.trees;

import game.GameWorld;
import game.flora.Branch;
import game.flora.Rule;
import game.flora.Transformation;
import game.flora.Tree;

import java.util.HashSet;
import java.util.Set;

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

		float fourth_pi = (float)Math.PI/4;
		float third_pi = (float)Math.PI/3;
		float half_pi = (float)Math.PI/2;

		Transformation t1 = new Transformation(third_pi, 0.4f, 0.2f);
		Transformation t2 = new Transformation(-third_pi, 0.4f, 0.2f);
		Transformation t3 = new Transformation(fourth_pi, 0.4f, 0.6f);
		Transformation t4 = new Transformation(-fourth_pi, 0.4f, 0.6f);
		Transformation t5 = new Transformation(0, 0.4f, 1f);

		for(int i = 0; i < depth; ++i) {
			t.addRule(new Rule(t1, t2, t3, t4, t5));
		}		

		t.populate();
		return t;
	}

	public static void treeTest(GameWorld world, java.awt.Graphics2D g) {
		if(world.getPlayer() != null) {
			Vec2f loc1 = world.getPlayer().shape.getCenter();
			Vec2f loc2 = loc1.plus(new Vec2f(0, -250));
	
			Set<Branch> branches = new HashSet<Branch>();
			branches.add(new Branch(loc1, loc2));
	
			Tree btree = new BinaryTree(4).newTree(branches);
			btree.onDraw(g);
		}
	}
}