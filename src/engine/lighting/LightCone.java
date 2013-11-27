package engine.lighting;

import cs195n.Vec2f;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;
/**
 * A class which stores a triple of numbers representing a single triangle of light cast 
 * against a single surface by a lighting object
 */
public class LightCone {
	private List<Vec2f> my_points = new ArrayList<Vec2f>();
	private Color my_color;

	/**
	 * Constructor.  Takes in the three points defining the triangle and adds them to
	 * the my_points list.
	 */
	public LightCone(Color c, Vec2f p1, Vec2f p2, Vec2f p3) {
		my_points.add(p1); my_points.add(p2); my_points.add(p3);
		my_color = c;
	}

	public Color getColor() {
		return my_color;
	}

	/**
	 * Accessor method for the points involved.
	 */
	public List<Vec2f> getPoints() {
		return my_points;
	}

	@Override
	/**
	 * Returns a useful String representation of this LightCone object
	 */
	public String toString() {
		return "[engine.lighting.LightCone P1=" + my_points.get(0) + " P2=" + my_points.get(1) + " P3=" + my_points.get(2) + "]";
	}
}