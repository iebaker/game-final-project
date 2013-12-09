package game.flora.trees;

import game.flora.Rule;
import game.flora.Transformation;
import game.flora.Tree;

public class PineTree extends Tree {
	private static final long	serialVersionUID	= 7446859587442962093L;
	
	@Override
	public void init() {
		
		Transformation t1 = new Transformation(Transformation.FIFTH_PI, 0.5f, 0.2f);
		Transformation t2 = new Transformation(-Transformation.FIFTH_PI, 0.5f, 0.4f);
		Transformation t3 = new Transformation(Transformation.FIFTH_PI, 0.5f, 0.6f);
		Transformation t4 = new Transformation(-Transformation.FIFTH_PI, 0.5f, 0.8f);
		Transformation t5 = new Transformation(0, 0.4f, 1f);
		
		for(int i = 0; i < 5; ++i) {
			addRule(new Rule(t1, t2, t3, t4, t5));
		}
	}
}