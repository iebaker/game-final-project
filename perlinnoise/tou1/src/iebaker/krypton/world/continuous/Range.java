package iebaker.krypton.world.continuous;

import cs195n.*;

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
		//System.out.println("Updating range " + this + " with new vector " + new_vector);
		Vec2f next = last_point.plus(new_vector);
		float distance = my_anchor.dist(next);

		//System.out.println("Distance calculated from anchor to next is " + distance);

		if(next.x < my_anchor.x || (is_vertical && (next.y < my_anchor.y))) {
			//System.out.println("This vector was less than anchor");
			if(-distance < min) {
				min = -distance;
			}
		} else {
			//System.out.println("This vector was more than anchor");
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

	public boolean overlaps(Range other) {
		//System.out.println("!!! Checking for overlap with range " + this + ", and " + other);
		if(other.getAxis() == my_axis) {
			Vec2f linking = other.getAnchor().minus(my_anchor);

			//System.out.println("!!! Linking vector calculated to be " + linking);
			Vec2f projected = linking.projectOnto(my_axis.getDirection());

			//System.out.println("!!! Projected vector calculated to be " + projected);
			float distance = projected.mag();

			//System.out.println("!!! Length of this vector is " + distance);
			Vec2f comparator = my_anchor.plus(projected);

			//System.out.println("!!! The comparator point calculated is " + comparator);
			if(comparator.x < my_anchor.x || (is_vertical && (comparator.y < my_anchor.y))) {
				//System.out.println("!!! This point was less than the anchor, so we flipped the distance");
				distance = -distance;
			}
			float other_max = other.getMax() + distance;
			float other_min = other.getMin() + distance;
			//System.out.println("The updated range of the other Range is [" + other_min + ", " + other_max + "]");
			//System.out.println("Verdict: " + (min <= other_max && other_min <= max));
			return min <= other_max && other_min <= max;
		} else {
			//System.out.println("Verdict: false");
			return false;
		}
	}

	@Override
	public String toString() {
		return "[RANGE anchor:" + my_anchor + ", min:" + min + ", max:" + max + "]";
	}
}