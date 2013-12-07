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
	private static Tree btree;

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

		Transformation t6 = new Transformation(fourth_pi, 0.8f, 1f);
		Transformation t7 = new Transformation(-fourth_pi, 0.5f, 1f);
		Transformation t8 = new Transformation(fourth_pi, 0.5f, 1f);
		Transformation t9 = new Transformation(-fourth_pi, 0.8f, 1f);

		t.addRule(new Rule(t1, t2, t3, t4, t5));

		for(int i = 0; i < depth; ++i) {
			//t.addRule(new Rule(t1, t2, t3, t4, t5));
			t.addRule(new Rule(t6, t7));
			t.addRule(new Rule(t8, t9));
		}		

		t.populate();
		return t;
	}

	public static void treeTest(GameWorld world, java.awt.Graphics2D g) {
		if(world.getPlayer() == null) return;
		if(btree == null) {
			Vec2f loc1 = world.getPlayer().shape.getCenter();
			Vec2f loc2 = loc1.plus(new Vec2f(0, -200));

			Set<Branch> branches = new HashSet<Branch>();
			branches.add(new Branch(loc1, loc2));

			btree = new BinaryTree(4).newTree(branches);
		}
		btree.onTick(0);
		btree.onDraw(g);
	}
}