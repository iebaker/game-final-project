package engine.lighting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.Color;

import engine.Artist;

import engine.lighting.Segment;
import engine.lighting.Vec2fPair;

import cs195n.Vec2f;

/**
 * A class which contains methods to actually perform lighting cone calculations for each LightSource in a world.
 */
public class LightingEngine {
	
	private List<Vec2f>		points			= new ArrayList<Vec2f>();
	private List<Segment>	lineSegments	= new ArrayList<Segment>();
	
/* ============================================================================
 * "Main" method which sets up the world and calls out to sweepline
 * ========================================================================= */

	/**
	 * Public-facing function which loops through each light source and computes its light cones using a sweepline
	 * algorithm over the points and segments in the world.
	 * 
	 * @param world
	 *            The LightWorld object over which to perform lighting calculations
	 */
	public void run(LightWorld world) {
		Vec2f lightLocation = null;
		for (LightSource light : world.getLightSources()) {
			
			// Reset points and line segments. They must be recalculated for each light source.
			points = new ArrayList<Vec2f>();
			lineSegments = new ArrayList<Segment>();
			
			// Create a comparator object which will be used to arrange the points
			lightLocation = light.getLocation();
			AngularComparator ac = new AngularComparator(lightLocation);
			
			// Get the points from the world and sort them according to their angle relative to the light source's
			// sweepline
			List<Vec2fPair> pointPairs = world.getPointsAndPairs(lightLocation, points);
			Collections.sort(points, ac);
			
			// Create line segments oriented by the direction in which the sweepline will pass over them
			for (Vec2fPair pair : pointPairs) {
				Segment segment = new Segment();
				
				Vec2f a = pair.getP1();
				Vec2f b = pair.getP2();
				
				int comp = ac.compare(a, b);
				
				if (comp <= 0) {
					segment.setBeginPoint(a); 	// TODO: Make this work even with
					segment.setEndPoint(b); 	// the case of a segment whose endpoint
				} else { 						// and beginning point are on the wrong
					segment.setBeginPoint(b); 	// sides of the list...
					segment.setEndPoint(a);
				}
				
				lineSegments.add(segment);
			}
		}
		
		this.sweep(lightLocation);
	}
	
/* ============================================================================
 * Sweeping and raycasting
 * ========================================================================= */

	/**
	 * This method actually runs the RedBlob sweepline algorithm in order to calculate lighting cones
	 */
	private void sweep(Vec2f lightLocation) {

		// Set up preliminary values
		RayCastData rcd = this.doRayCast(lightLocation, points.get(0));
		Segment prevSegment = rcd.minSegment();
		List<LightCone> cones = new ArrayList<LightCone>();
		points.add(points.get(0));
		int i = 1;
		
		// Sweep!
		while (true) {
			if (i >= points.size()) break;
			
			Vec2f point = points.get(i);
			rcd = this.doRayCast(lightLocation, point);

			Segment closest = rcd.minSegment();
			
			if (point == prevSegment.getEndPoint()) {
				LightCone lc = new LightCone(lightLocation, point, prevSegment.getBeginPoint());
				cones.add(lc);
				
				if (point.isStart()) {
					++i;
				} else {
					rcd.removePoint(point);
					points.add(i + 1, rcd.minPoint());
					i += 2;
				}

				closest = rcd.minSegment();
				prevSegment = closest;

			} else if (closest != prevSegment) {

				Vec2f intersection = this.intersect(closest.getBeginPoint(), closest.getEndPoint(), prevSegment.getBeginPoint(), prevSegment.getEndPoint());
				LightCone lc;

				if(intersection != null) {
					lc = new LightCone(lightLocation, intersection, prevSegment.getBeginPoint());
					closest.resetBeginPoint(intersection);
				} else {
					rcd.removePoint(point);
					lc = new LightCone(lightLocation, rcd.minPoint(), prevSegment.getBeginPoint());
				}

				cones.add(lc);
				++i;
				prevSegment = closest;
			} else {
				++i;
			}
		}
	}
	
	/**
	 * Runs a raycast on the current world.
	 * 
	 * @param sourcePoint
	 *            The source of the ray
	 * @param targetPoint
	 *            A point to aim the ray at (such that targetPoint - sourcePoint is the direction vector of this ray)
	 * @return A RayCastData object representing the result of the cast
	 */
	private RayCastData doRayCast(Vec2f sourcePoint, Vec2f targetPoint) {
		RayCastData rcd_return = new RayCastData(sourcePoint);
		Vec2f direction = targetPoint.minus(sourcePoint);
		direction = direction.normalized().smult(10000000);
		
		for (Segment segment : lineSegments) {
			Vec2f newIntersection = intersect(sourcePoint, sourcePoint.plus(direction), segment.getBeginPoint(),
					segment.getEndPoint());
			if (newIntersection != null) {
				rcd_return.addIntersection(newIntersection, segment);
			}
		}
		if(rcd_return.getIntersections().size() == 0) {
			//System.out.println(targetPoint);
		}
		return rcd_return;
	}
	
/* ============================================================================
 * Utility methods for math calculations
 * ========================================================================= */

	/**
	 * This function determines if two line segments intersect each other, and if they do, the value of the variable
	 * intersection is set to the intersection point of the two segments.
	 * 
	 * @param A1
	 *            The beginning of the first segment
	 * @param A2
	 *            The end of the first segment
	 * @param B1
	 *            The beginning of the second segment
	 * @param B2
	 *            The end of the second segment
	 * @param intersection
	 *            A Vec2f which will be set
	 * @return True, if the points define intersecting segments, false otherwise
	 */
	private static Vec2f intersect(Vec2f A1, Vec2f A2, Vec2f B1, Vec2f B2) {
		float mA = (A2.y - A1.y) / (A2.x - A1.x);
		float mB = (B2.y - B1.y) / (B2.x - B1.x);
		
		float intX = ((mA * A1.x) - A1.y - (mB * B1.x) + B1.y) / (mA - mB);
		float intY = (mA * (intX - A1.x)) + A1.y;
		
		if (within(intX, A1.x, A2.x) && within(intX, B1.x, B2.x) && within(intY, A1.y, A2.y)
				&& within(intY, B1.y, B2.y)) {
			Vec2f vec = null;
			vec = new Vec2f(intX, intY);
			return vec;
		}
		return null;
	}
	
	/**
	 * Determines whether a value is in between two other values, with no regards to the order the limiting values will
	 * be given.
	 * 
	 * @param a
	 *            The value in question
	 * @param E1
	 *            One of the bounds
	 * @param E2
	 *            The other bound
	 * @return True, if a is between E1 and E2.
	 */
	private static boolean within(float a, float E1, float E2) {
		return a >= E1 && a <= E2 || a >= E2 && a <= E1;
	}

/* ============================================================================
 * ALL THIS IS DEBUGGING, ignore it.
 * ========================================================================= */

	/**
	 * Test for point and pair retrieval
	 */
	public void test1(LightWorld world) {
		System.out.println("[test1] This test will make sure that the world can correctly return points and pairs");
		List<Vec2f> points = new ArrayList<Vec2f>();

		System.out.println("[test1] Retrieving light source");
		LightSource source = world.getLightSources().get(0);
		System.out.println("[test1] Light source retrieved : " + source);

		System.out.println("[test1] Retrieving points and pairs");
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);
		System.out.println("[test1] Pairs and points retrieved.");

		System.out.println("[test1] POINTS:");
		for(Vec2f point : points) {
			System.out.println(point);
		}

		System.out.println("[test1] PAIRS:");
		for(Vec2fPair pair : pairs) {
			System.out.println(pair);
		}

		System.out.println("[test1] End test1");
	}

	/**
	 * Println based test of sorting (use if you have a small number of points and can easily check by hand)
	 */
	public void test2(LightWorld world) {
		System.out.println("[test2] This test will make sure that sorting works.");
		List<Vec2f> points = new ArrayList<Vec2f>();
		LightSource source = world.getLightSources().get(0);
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);

		System.out.println("[test2] Points and pairs retrieved, sorting points with AngularComparator");
		Collections.sort(points, new AngularComparator(source.getLocation()));

		System.out.println("[test2] SORTED POINTS:");
		for(int i = 0; i < points.size(); ++i) {
			System.out.println(i + ": " + points.get(i));
		}
		System.out.println("[test2] End test2");
	}

	/**
	 * Visual test of sorting (uses Graphics2D, must be called from onDraw)
	 */
	public void test3(LightWorld world, Graphics2D g) {
		List<Vec2f> points = new ArrayList<Vec2f>();
		LightSource source = world.getLightSources().get(0);
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);

		Collections.sort(points, new AngularComparator(source.getLocation()));
		Vec2f worldSize = world.getWorldSize();

		Artist a = new Artist();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, worldSize.x, worldSize.y);

		a.setFillPaint(Color.BLACK);

		for(int i = 0; i < points.size(); ++i) {
			a.text(g, i + "", (points.get(i).x - 3000)/4 - 150, (points.get(i).y - 2500)/5);
		}
	}

	public Vec2f approxPointConvert(Vec2f original) {
		return new Vec2f((original.x - 3000)/5 - 100, (original.y - 2500)/5);
	}

	/**
	 * Visual test of segment finding
	 */
	public void test4(LightWorld world, Graphics2D g) {
		List<Vec2f> points = new ArrayList<Vec2f>();
		LightSource source = world.getLightSources().get(0);
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);

		Collections.sort(points, new AngularComparator(source.getLocation()));
		Vec2f worldSize = world.getWorldSize();

		Artist a = new Artist();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, worldSize.x, worldSize.y);

		a.setStroke(false);
		a.setFillPaint(Color.BLACK);
		for(int i = 0; i < points.size(); ++i) {
			Vec2f goodPoint = approxPointConvert(points.get(i));
			a.ellipse(g, goodPoint.x, goodPoint.y, 3, 3);
		}

		a.setStroke(true);
		for(int i = 0; i < pairs.size(); ++i) {
		 	Vec2f point1 = approxPointConvert(pairs.get(i).getP1());
		 	Vec2f point2 = approxPointConvert(pairs.get(i).getP2());
		 	a.line(g,point1.x, point1.y, point2.x, point2.y);
		}
	}

	/**
	 * Test of segment ordering
	 */
	public void test5(LightWorld world, Graphics2D g) {
		List<Vec2f> points = new ArrayList<Vec2f>();
		LightSource source = world.getLightSources().get(0);
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);

		Collections.sort(points, new AngularComparator(source.getLocation()));
		Vec2f worldSize = world.getWorldSize();

		Artist a = new Artist();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, worldSize.x, worldSize.y);

		a.setFillPaint(Color.BLACK);

		List<Segment> segs = v2f2Segment(pairs, new AngularComparator(source.getLocation()));

		for(Segment s : segs) {
			Vec2f firstPoint = s.getBeginPoint();
			Vec2f secndPoint = s.getEndPoint();
			Vec2f midPoint = this.midpoint(firstPoint, secndPoint);

			g.setColor(Color.RED);
			g.drawLine((int)firstPoint.x, (int)firstPoint.y, (int)midPoint.x, (int)midPoint.y);
			g.setColor(Color.BLUE);
			g.drawLine((int)midPoint.x, (int)midPoint.y, (int)secndPoint.x, (int)secndPoint.y);
		}
	}

	public List<Segment> v2f2Segment(List<Vec2fPair> pairs, AngularComparator ac) {
		List<Segment> segments = new ArrayList<Segment>();

		for(Vec2fPair pair : pairs) {
			Segment segment = new Segment();
			Vec2f a = approxPointConvert(pair.getP1());
			Vec2f b = approxPointConvert(pair.getP2());

			int comp = ac.compare(pair.getP1(), pair.getP2());

			if(comp <= 0) {
				segment.setBeginPoint(a);
				segment.setEndPoint(b);
			} else {
				segment.setBeginPoint(b);
				segment.setEndPoint(a);
			}

			segments.add(segment);
		}

		return segments;
	}

	public Vec2f midpoint(Vec2f p1, Vec2f p2) {
		return new Vec2f((p1.x + p2.x)/2, (p1.y + p2.y)/2);
	}

/* ============================================================================
 * DEBUGGING RAYCASTING
 * ========================================================================= */

	public void test6(LightWorld world, Graphics2D g) {
		List<Vec2f> points = new ArrayList<Vec2f>();
		LightSource source = world.getLightSources().get(0);
		List<Vec2fPair> pairs = world.getPointsAndPairs(source.getLocation(), points);

		Collections.sort(points, new AngularComparator(source.getLocation()));
		Vec2f worldSize = world.getWorldSize();

		Artist a = new Artist();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, worldSize.x, worldSize.y);

		a.setFillPaint(Color.BLACK);

		List<Segment> segs = v2f2Segment(pairs, new AngularComparator(source.getLocation()));

		for(Segment s : segs) {
			Vec2f firstPoint = s.getBeginPoint();
			Vec2f secndPoint = s.getEndPoint();
			Vec2f midPoint = this.midpoint(firstPoint, secndPoint);

			g.setStroke(new java.awt.BasicStroke(2));
			g.setColor(Color.RED);
			g.drawLine((int)firstPoint.x, (int)firstPoint.y, (int)midPoint.x, (int)midPoint.y);
			g.setColor(Color.BLUE);
			g.drawLine((int)midPoint.x, (int)midPoint.y, (int)secndPoint.x, (int)secndPoint.y);
		}		

		g.setStroke(new java.awt.BasicStroke(1));
		a.setFillPaint(Color.GRAY);

		this.points = points;
		this.lineSegments = segs;
		Vec2f sp = approxPointConvert(source.getLocation());

		for(Vec2f p : points) {
			p = approxPointConvert(p);
			a.ellipse(g, p.x, p.y, 3, 3);

			RayCastData rcd = this.doRayCast(sp, p);
			Vec2f rcdmin = null;
			if(rcd.getIntersections().size() > 0) rcdmin = rcd.minPoint();
			if(rcdmin != null) {
				a.line(g, sp.x, sp.y, rcdmin.x, rcdmin.y);
			} else {
				g.setColor(Color.GREEN);
				a.line(g, sp.x, sp.y, p.x, p.y);
			}
		}

		Vec2f tester = this.intersect(new Vec2f(0,0), new Vec2f(0,1), new Vec2f(-1,0.5f), new Vec2f(1,0.5f));
		//System.out.println(tester);
	}

	public void walls(float width, float height, Vec2f sourcePoint) {

		Vec2f p1 = new Vec2f(0,0);
		Vec2f p2 = new Vec2f(0, height);
		Vec2f p3 = new Vec2f(width, height);
		Vec2f p4 = new Vec2f(width, 0);

		this.points.add(p1);
		this.points.add(p2);
		this.points.add(p3);
		this.points.add(p4);

		List<Vec2fPair> temp = new ArrayList<Vec2fPair>();

		temp.add(new Vec2fPair(p1, p2));
		temp.add(new Vec2fPair(p2, p3));
		temp.add(new Vec2fPair(p3, p4));
		temp.add(new Vec2fPair(p4, p1));

		this.lineSegments = v2f2Segment(temp, new AngularComparator(sourcePoint));
	}

}