package game.flora.trees;

import game.flora.Rule;
import game.flora.Transformation;
import game.flora.Tree;

public class MapleTree extends Tree {
	
	private static final long	serialVersionUID	= 1617617412421888417L;
	
	@Override
	public void init() {
		
		float fourth_pi = (float) Math.PI / 4;
		float third_pi = (float) Math.PI / 3;
		
		Transformation t1 = new Transformation(third_pi, 0.4f, 0.2f);
		Transformation t2 = new Transformation(-third_pi, 0.4f, 0.2f);
		Transformation t3 = new Transformation(fourth_pi, 0.4f, 0.6f);
		Transformation t4 = new Transformation(-fourth_pi, 0.4f, 0.6f);
		Transformation t5 = new Transformation(0, 0.4f, 1f);
		
		Transformation t6 = new Transformation(fourth_pi, 0.8f, 1f);
		Transformation t7 = new Transformation(-fourth_pi, 0.5f, 1f);
		Transformation t8 = new Transformation(fourth_pi, 0.5f, 1f);
		Transformation t9 = new Transformation(-fourth_pi, 0.8f, 1f);
		
		addRule(new Rule(t1, t2, t3, t4, t5));
		
		for(int i = 0; i < 3; ++i) {
			addRule(new Rule(t6, t7));
			addRule(new Rule(t8, t9));
		}
	}
}