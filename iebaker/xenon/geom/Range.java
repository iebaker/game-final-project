package iebaker.xenon.geom;

import cs195n.Vec2f;
import iebaker.xenon.util.SuperFloat;
import iebaker.xenon.util.Utils;

public class Range {
	private SeparatingAxis my_axis;
	private Vec2f my_anchor;
	private float min;
	private float max;
	private Vec2f last_point;
	private boolean is_vertical;

	public Range(SeparatingAxis a, Vec2f v) {
		my_axis = a;
		my_anchor = v;
		last_point = v;
		is_vertical = a.getDirection().x == 0;
		max = 0;
		min = 0;
	}

	public void update(Vec2f new_vector) {
		Vec2f next = last_point.plus(new_vector);
		float distance = my_anchor.dist(next);

		if(next.x < my_anchor.x || (is_vertical && (next.y < my_anchor.y))) {
			if(-distance < min) {
				min = -distance;
			}
		} else {
			if(distance > max) {
				max = distance;
			}
		}

		last_point = next;
	}

	public SeparatingAxis getAxis() {
		return my_axis;
	}

	public Vec2f getAnchor() {
		return my_anchor;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public boolean overlaps(Range other, SuperFloat amt) {
		if(other.getAxis() == my_axis) {

			Vec2f linking = other.getAnchor().minus(my_anchor);
			Vec2f projected = linking.projectOnto(my_axis.getDirection());
			float distance = projected.mag();
			Vec2f comparator = my_anchor.plus(projected);

			if(comparator.x < my_anchor.x || (is_vertical && (comparator.y < my_anchor.y))) {
				distance = -distance;
			}
			float other_max = other.getMax() + distance;
			float other_min = other.getMin() + distance;

			if(min < other_max && other_min < max) {
				amt.setVal((float)Math.abs(Utils.intervalMTV(min, max, other_min, other_max)));
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	public boolean overlaps(Range other) {
		return this.overlaps(other, null);
	}

	@Override
	public String toString() {
		return "[RANGE anchor:" + my_anchor + ", min:" + min + ", max:" + max + "]";
	}
}
