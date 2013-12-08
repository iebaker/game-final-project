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
	private static Tree btree;

	public BinaryTree(int depth) {
		super();
		this.depth = depth;
	}

	public BinaryTree() {
		super();
		this.depth = 4;
	}

	@Override
	public void init() {

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

		this.addRule(new Rule(t1, t2, t3, t4, t5));

		for(int i = 0; i < depth; ++i) {
			this.addRule(new Rule(t6, t7));
			this.addRule(new Rule(t8, t9));
		}		
	}
}