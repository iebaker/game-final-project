package iebaker.krypton.world.continuous;

import java.util.HashSet;

public class CompoundShape implements Shape {
	private java.util.Set<Shape> my_shapes = new HashSet<Shape>();
	public CompoundShape(Shape... constituents) {
		for(Shape s : constituents) {
			my_shapes.add(s);
		}
	}

	public boolean checkCollision(Shape s) {
		for(Shape subshape : my_shapes) {
			if(subshape.checkCollision(s)) return true;
		}
		return false;
	}

	public boolean checkCircleCollision(Circle c) {
		for(Shape subshape : my_shapes) {
			if(subshape.checkCircleCollision(c)) return true;
		}
		return false;
	}

	public boolean checkAABCollision(AAB a) {
		for(Shape subshape : my_shapes) {
			if(subshape.checkAABCollision(a)) return true;
		}
		return false;
	}

	public boolean checkPolygonCollision(Polygon p) {
		for(Shape subshape : my_shapes) {
			if(subshape.checkPolygonCollision(p)) return true;
		}
		return false;
	}
}
