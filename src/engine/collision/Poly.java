package engine.collision;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import cs195n.Vec2f;
import engine.Shape;
import engine.Vec2fPair;
import engine.Viewport;

/**
 * Polygon shape class, supporting collision detection
 * 
 * @author dgattey
 * 
 */
public class Poly extends Shape implements CollisionShape {
	
	private static final long	serialVersionUID	= -4767626718090673350L;
	private final Vec2f[]		points;
	private Vec2f[]				pointsToDraw;
	private Vec2f				location;
	
	/**
	 * Public constructor, taking a list of Vec2fs representing the points of the polygon in counterclockwise order
	 * 
	 * @param points
	 *            The points of the polygon (in counterclockwise order!)
	 */
	public Poly(Color c, Vec2f[] points) {
		this.points = points;
		this.c = c;
		location = points[0];
	}
	
	/**
	 * Returns the edges of this polygon by way of iteration
	 * 
	 * @return an array representing the edges of this polygon
	 */
	protected ArrayList<Vec2f> getEdges() {
		ArrayList<Vec2f> toReturn = new ArrayList<Vec2f>();
		for (int i = 0; i < points.length; i++) {
			Vec2f edge = points[(i + 1 == points.length) ? 0 : i + 1].minus(points[i]); // 'wraps around'
			toReturn.add(edge);
		}
		return toReturn;
	}
	
	/**
	 * Returns the points of the polygon in an easy format
	 * 
	 * @return an array representing the points of this polygon
	 */
	public ArrayList<Vec2f> getPoints() {
		ArrayList<Vec2f> ret = new ArrayList<Vec2f>();
		for (Vec2f pt : points) {
			ret.add(pt);
		}
		return ret;
	}
	
	/**
	 * Returns the axes for this shape
	 * 
	 * @return the axes of this Poly
	 */
	public ArrayList<Vec2f> getAxes() {
		ArrayList<Vec2f> ax = new ArrayList<Vec2f>();
		for (Vec2f edge : getEdges()) {
			ax.add((new SeparatingAxis(edge)).getNormalizedAxis());
		}
		return ax;
	}
	
	@Override
	/**
	 * Draw the polygon by iterating over vertices and adding lines between
	 */
	public void drawShape(Graphics2D g) {
		Path2D path = null;
		for (Vec2f point : pointsToDraw) {
			if (path == null) {
				path = new Path2D.Float();
				path.moveTo(point.x, point.y);
			} else
				path.lineTo(point.x, point.y);
		}
		path.lineTo(pointsToDraw[0].x, pointsToDraw[0].y); // back to start
		g.draw(path);
	}
	
	@Override
	/**
	 * Draw and fill the polygon by iterating over vertices and adding lines between
	 */
	public void drawAndFillShape(Graphics2D g) {
		Path2D path = null;
		for (Vec2f point : pointsToDraw) {
			if (path == null) {
				path = new Path2D.Float();
				path.moveTo(point.x, point.y);
			} else
				path.lineTo(point.x, point.y);
		}
		g.fill(path);
	}
	
	@Override
	/**
	 * Returns the MTV of this and the otherShape
	 */
	public Vec2f getMTV(CollisionShape otherShape) {
		return new MTV().shapeMTV(this, otherShape);
	}
	
	@Override
	/**
	 * Collides a shape with this (a polygon)
	 */
	public boolean collides(CollisionShape o) {
		return o.collidesPoly(this);
	}
	
	@Override
	/**
	 * To find if a point p is in the polygon, works by computing dot products - if cross products ever negative, it's
	 * outside, but if all positive, it's inside
	 * 
	 * @param p
	 *            The point to check
	 * @return If the point is in the polygon
	 */
	public boolean collidesPoint(Vec2f p) {
		for (int i = 0; i < points.length; i++) {
			Vec2f v = points[(i + 1 == points.length) ? 0 : i + 1].minus(points[i]); // 'wraps around'
			if (v.cross(p) < 0) return false;
		}
		return true;
	}
	
	@Override
	/**
	 * Checks the projection of this polygon against the circle and vice versa - though there's just one axis for the circle
	 */
	public boolean collidesCircle(Circle c) {
		if (!checkPoly(this, c)) return false;
		float dist = Float.MAX_VALUE;
		Vec2f point = null;
		for (Vec2f pt : points) {
			float newD = c.center.minus(pt).mag2();
			if (newD < dist) {
				point = pt;
				dist = newD;
			}
		}
		SeparatingAxis circAxis = new SeparatingAxis(point.minus(c.center), false);
		return (checkRangesForOverlap(circAxis.project(this), circAxis.project(c)));
	}
	
	@Override
	/**
	 * Checks the projection of this polygon against the AAB and vice versa - though there's only two axes for the AAB
	 */
	public boolean collidesAAB(AAB aab) {
		if (!checkPoly(this, aab)) return false;
		SeparatingAxis x = new SeparatingAxis(new Vec2f(0, 1));
		if (!checkRangesForOverlap(x.project(this), x.project(aab))) return false;
		SeparatingAxis y = new SeparatingAxis(new Vec2f(1, 0));
		if (!checkRangesForOverlap(y.project(this), y.project(aab))) return false;
		return (true);
	}
	
	@Override
	/**
	 * Checks the projections of both polygons
	 */
	public boolean collidesPoly(Poly p) {
		return (checkPoly(this, p) && checkPoly(p, this));
	}
	
	@Override
	/**
	 * Raycasts onto this shape
	 */
	public Vec2f cast(Ray ray) {
		return ray.getIntersection(getPoints());
	}
	
	/**
	 * Iterates over the edges of q and checks the projection of q against the projection of p - if all projections
	 * overlap, return true
	 * 
	 * @param q
	 *            The Poly to make separating axes from
	 * @param p
	 *            The other Poly to project
	 * @return If the projections of q and p are overlapping on every axis of q
	 */
	private boolean checkPoly(Poly q, CollisionShape p) {
		for (Vec2f e : q.getEdges()) {
			SeparatingAxis sa = new SeparatingAxis(e);
			if (!(checkRangesForOverlap(sa.project(q), sa.project(p)))) {
				return false;
			}
		}
		return true; // q and p are overlapping on every axis
	}
	
	/**
	 * Takes two ranges and returns if they're overlapping
	 * 
	 * @param a
	 *            Range a
	 * @param b
	 *            Range b
	 * @return If range a and b are overlapping
	 */
	private boolean checkRangesForOverlap(Range a, Range b) {
		return (a.overlapping(b));
	}
	
	@Override
	/**
	 * Makes a new array, taking everything from the points array and translating it to screen points
	 */
	public void toScreen(Viewport v) {
		pointsToDraw = new Vec2f[points.length];
		for (int i = 0; i < points.length; i++) {
			pointsToDraw[i] = Viewport.gamePtToScreen(points[i]);
		}
	}
	
	@Override
	/**
	 * Changes position of points by calculating offset from "location" - the first point in the array
	 */
	public void changeLocation(Vec2f move) {
		for (int i = 0; i < points.length; i++) {
			Vec2f offset = points[i].minus(location);
			points[i] = move.plus(offset);
		}
		location = move;
	}
	
	@Override
	/**
	 * Updates position of the Poly based on previous location in game
	 */
	public void move(Vec2f move) {
		changeLocation(getLocation().plus(move));
	}
	
	@Override
	/**
	 * Returns current location (equal to first item in array)
	 */
	public Vec2f getLocation() {
		return location;
	}
	
	@Override
	/**
	 * Returns the center of this shape
	 */
	public Vec2f getCenter() {
		float xmin = Float.POSITIVE_INFINITY;
		float xmax = Float.NEGATIVE_INFINITY;
		float ymin = Float.POSITIVE_INFINITY;
		float ymax = Float.NEGATIVE_INFINITY;
		for (Vec2f pt : points) {
			if (pt.x > xmax) xmax = pt.x;
			if (pt.x < xmin) xmin = pt.x;
			if (pt.y > ymax) ymax = pt.y;
			if (pt.y < ymin) ymin = pt.y;
		}
		return new Vec2f(xmin + (xmax - xmin) / 2, ymin + (ymax - ymin) / 2);
	}
	
	/**
	 * String representation of the polygon
	 */
	@Override
	public String toString() {
		String s = "Poly<pts:";
		for (Vec2f pt : points) {
			s += pt;
			s += ", ";
		}
		return s + ">";
		
	}

	@Override
	public Color getColor() {
		return this.c;
	}

	@Override
	public void setColor(Color c) {
		this.c = c;
	}

	@Override
	public Vec2fPair getBoundingBox() {
		float xmin = Float.POSITIVE_INFINITY;
		float xmax = Float.NEGATIVE_INFINITY;
		float ymin = Float.POSITIVE_INFINITY;
		float ymax = Float.NEGATIVE_INFINITY;
		for (Vec2f pt : points) {
			if (pt.x > xmax) xmax = pt.x;
			if (pt.x < xmin) xmin = pt.x;
			if (pt.y > ymax) ymax = pt.y;
			if (pt.y < ymin) ymin = pt.y;
		}
		return new Vec2fPair(new Vec2f(xmin, ymin), new Vec2f(xmax, ymax));
	}
}
