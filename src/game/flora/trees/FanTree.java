package game.flora.trees;

import game.flora.Rule;
import game.flora.Transformation;
import game.flora.Tree;

public class FanTree extends Tree {
	@Override
	public void init() {

		Transformation left_turn = new Transformation(-Transformation.FIFTH_PI, 0.5f, 1f);
		Transformation right_turn = new Transformation(Transformation.FIFTH_PI, 0.5f, 1f);

		Transformation left_base = new Transformation(Transformation.FOURTH_PI, 0.6f, 0f);
		Transformation right_base = new Transformation(-Transformation.FOURTH_PI, 0.7f, 0f);

		Transformation t1 = new Transformation(Transformation.FIFTH_PI, 0.3f, 0.2f);
		Transformation t2 = new Transformation(-Transformation.FIFTH_PI, 0.3f, 0.4f);
		Transformation t3 = new Transformation(Transformation.FIFTH_PI, 0.3f, 0.6f);
		Transformation t4 = new Transformation(-Transformation.FIFTH_PI, 0.3f, 0.8f);
		Transformation t5 = new Transformation(0, 0.4f, 1f);

		this.addRule(new Rule(t1, t2, t3, t4, t5));

		for(int i = 0; i < 2; ++i) {
			Rule r2 = new java.util.Random().nextBoolean() ? new Rule(left_turn, right_turn) : new Rule(t1, t2, t3, t4, t5);
			this.addRule(r2);
			this.addRule(new Rule(left_turn, right_turn));
		}
	}
}