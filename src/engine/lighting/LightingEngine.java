package engine.lighting;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs195n.Vec2f;
import engine.Artist;
import engine.Viewport;
import game.GameWorld;

/**
 * LightingEngine is a class whose main function is to actually perform the sweepline algorithm which computes for each
 * LightSource in the world a set of LightCone objects representing the areas lit by this LightSource. It also currently
 * contains debugging functions which can be used to visualize the raycasting and cone creation algorithms.
 * 
 * @author iebaker
 */
public class LightingEngine {
	
	private List<Segment>		lineSegments	= new ArrayList<Segment>();
	private List<Vec2f>			points			= new ArrayList<Vec2f>();
	private int					rayNum4Debug	= 0;
	
	/**
	 * Performs sweepline cone creation algorithm for every LightSource the world returns from its getLightSources()
	 * method. The LightSource objects themselves will be mutated, so this method does not return anything.
	 * 
	 * @param world
	 *            The LightWorld in which to run these calculations.
	 */
	public void run(LightWorld world) {
		for (LightSource light : world.getLightSources()) {
			setup(light, world);
			sweep(light);
		}
	}
	
	/**
	 * Populates instance variables of this class (points, lineSegments) by querying the world for point and pair
	 * information, ordering the points, and orienting the pairs into Segment objects by angle around the source point.
	 * 
	 * @param light
	 *            The LightSource about which to perform lighting calculations
	 * @param world
	 *            The LightWorld in which this LightSource lives
	 */
	private void setup(LightSource light, LightWorld world) {
		lineSegments = new ArrayList<Segment>();
		points = new ArrayList<Vec2f>();
		//builder.reset(light);
		
		Vec2f lightLocation = light.getLocation();
		AngularComparator ac = new AngularComparator(lightLocation);
		List<Vec2fPair> pointPairs = world.getPointsAndPairs(lightLocation, points);
		Collections.sort(points, ac);
		
		for (Vec2fPair pair : pointPairs) {
			Segment segment = new Segment();
			
			Vec2f a = pair.getP1();
			Vec2f b = pair.getP2();
			
			if (wrongWay(lightLocation, a, b)) {
				if (ac.compare(a, b) <= 0) {
					segment.setBeginPoint(b);
					segment.setEndPoint(a);
				} else {
					segment.setBeginPoint(a);
					segment.setEndPoint(b);
				}
				segment.flip();
			} else {
				if (ac.compare(a, b) <= 0) {
					segment.setBeginPoint(a);
					segment.setEndPoint(b);
				} else {
					segment.setBeginPoint(b);
					segment.setEndPoint(a);
				}
			}
			
			lineSegments.add(segment);
		}
	}
	
	/**
	 * Actually performs the sweepline algorithm, using the ConeBuilder object owned by this class (maybe should make
	 * those into static methods?) in order to create LightCones
	 * 
	 * @param light
	 *            The LightSource around which sweeping is performed. Mutated by the method.
	 */
	private void sweep(LightSource light) {
		if (points.isEmpty()) return;

		List<Vec2f> builder = new ArrayList<Vec2f>();
		
		RayCastData rcd = doRayCast(light.getLocation(), points.get(0));
		Segment closest;
		try {
			closest = rcd.minSegment();
		} catch (Exception e) {
			System.out.println("Error raycast (1) : " + rcd);
			return;
		}
		
		int i = 0;
		Vec2f first = null;
		points.add(points.get(0));
		while (i < points.size()) {
			
			Vec2f currentPoint = points.get(i);
			RayCastData currentRCD = doRayCast(light.getLocation(), currentPoint);
			Segment currentClosest;
			try {
				currentClosest = currentRCD.minSegment();
			} catch (Exception e) {
				System.out.println("Error raycast (2) : " + currentRCD);
				i++;
				continue;
			}
			
			if (builder.isEmpty()) {
				first = currentRCD.minPoint();
				builder.add(first);
				i++;
				continue;
			}
			
			boolean closestEnded = Segment.endingAt(currentPoint).contains(closest);
			boolean newClosest = currentClosest != closest;
			Vec2f testInt = LightingEngine.intersect(closest.getBeginPoint(), closest.getEndPoint(),
					currentClosest.getBeginPoint(), currentClosest.getEndPoint());
			boolean wasIntersection = testInt != null;
			
			if (closestEnded && newClosest && wasIntersection) {
				builder.add(testInt);
			} else if (closestEnded) {
				builder.add(currentPoint);
				
				if (!currentPoint.isStart()) {
					builder.add(currentRCD.minPoint());
				} 
			} else if (newClosest) {
				
				if (wasIntersection) {
					builder.add(testInt);
				} else {
					try {
						builder.add(currentRCD.getUniquePoints().get(1));
						builder.add(currentPoint);
					} catch (Exception e) {
						System.out.println("Error raycast (3) : " + currentRCD);
					}
				}
			}
			
			closest = currentClosest;
			i++;
		}
		
		builder.add(first);
		light.setPoly(builder);
	}
	
	private RayCastData doRayCast(Vec2f sourcePoint, Vec2f targetPoint) {
		List<Vec2f> collinears = getCollinearPoints(sourcePoint, targetPoint);
		
		for (Vec2f point : collinears) {
			Segment.ignoreEndingAt(point);
			Segment.ignoreBeginningAt(point);
		}
		
		RayCastData rcd_return = new RayCastData(sourcePoint);
		Vec2f direction = targetPoint.minus(sourcePoint);
		direction = direction.normalized().smult(10000000);
		
		for (Vec2f point : collinears) {
			for (Segment s : Segment.beginningAt(point)) {
				rcd_return.addIntersection(point, s);
			}
		}
		
		for (Segment segment : lineSegments) {
			if (segment.isIgnored()) continue;
			Vec2f newIntersection = LightingEngine.intersect(sourcePoint, sourcePoint.plus(direction),
					segment.getBeginPoint(), segment.getEndPoint());
			if (newIntersection != null) {
				rcd_return.addIntersection(newIntersection, segment);
			}
		}
		
		for (Vec2f point : collinears) {
			Segment.noticeEndingAt(point);
			Segment.noticeBeginningAt(point);
		}
		return rcd_return;
	}
	
	/* ============================================================================
	 * Debugging yay!
	 * ========================================================================= */
	
	public void debug(LightWorld world, Graphics2D g) {
		Artist a = new Artist();
		
		if (world.getLightSources().isEmpty()) return;
		
		run(world);
		LightSource source = world.getLightSources().get(0);
		
		a.setFillPaint(Color.BLACK);
		a.setStroke(false);
		for (Vec2f point : points) {
			a.ellipse(g, point.x, point.y, 2, 2);
		}
		
		for (Segment segment : lineSegments) {
			a.line(g, segment.getBeginPoint().x, segment.getBeginPoint().y, segment.getEndPoint().x,
					segment.getEndPoint().y);
		}
		
		List<LightCone> cones = source.getLightCones();
		for (LightCone cone : cones) {
			a.setFillPaint(cone.getColor());
			a.path(g, cone.getPoints());
		}
	}
	
	public void rayDebug(LightWorld world, Graphics2D g) {
		Segment.clear();
		
		Artist a = new Artist();
		
		if (world.getLightSources().isEmpty()) return;
		LightSource source = world.getLightSources().get(0);
		Vec2f convLSpoint = Viewport.gamePtToScreen(source.getLocation());
		
		setup(source, world);
		
		if (points.isEmpty()) return;
		
		Vec2f size = ((GameWorld) world).getWorldSize();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, size.x, size.y);
		
		a.setFillPaint(Color.BLACK);
		a.ellipse(g, convLSpoint.x - 5, convLSpoint.y - 5, 10, 10);
		
		for (Vec2f point : points) {
			a.ellipse(g, Viewport.gamePtToScreen(point).x, Viewport.gamePtToScreen(point).y, 2, 2);
		}
		
		for (Segment segment : lineSegments) {
			if (segment.isFlipped()) {
				g.setStroke(new BasicStroke(3));
			} else {
				g.setStroke(new BasicStroke(1));
			}
			Vec2f begin = Viewport.gamePtToScreen(segment.getBeginPoint());
			Vec2f end = Viewport.gamePtToScreen(segment.getEndPoint());
			Vec2f mid = LightingEngine.midpoint(begin, end);
			a.setStrokePaint(Color.RED);
			a.line(g, begin.x, begin.y, mid.x, mid.y);
			a.setStrokePaint(Color.BLUE);
			a.line(g, mid.x, mid.y, end.x, end.y);
		}
		
		for (int i = 0; i < points.size(); ++i) {
			Vec2f point = points.get(i);
			RayCastData rcd = doRayCast(source.getLocation(), point);
			Vec2f prev = Viewport.gamePtToScreen(source.getLocation());
			Vec2f convpt = Viewport.gamePtToScreen(point);
			a.setFillPaint(Color.RED);
			a.ellipse(g, convpt.x, convpt.y, 5, 5);
			Color color = Color.GREEN;
			g.setStroke(new BasicStroke(2));
			
			if (i == rayNum4Debug) {
				Segment min = rcd.minSegment();
				Vec2f p1 = Viewport.gamePtToScreen(min.getBeginPoint());
				Vec2f p2 = Viewport.gamePtToScreen(min.getEndPoint());
				
				a.setStrokePaint(new Color(0f, 0f, 0f, 0.5f));
				g.setStroke(new BasicStroke(6));
				a.line(g, p1.x, p1.y, p2.x, p2.y);
				a.line(g, prev.x, prev.y, convpt.x, convpt.y);
			}
			
			g.setStroke(new BasicStroke(1));
			for (Intersection intersection : rcd.getIntersections()) {
				a.setStrokePaint(color);
				Vec2f pt = Viewport.gamePtToScreen(intersection.getPoint());
				
				a.line(g, prev.x, prev.y, pt.x, pt.y);
				
				prev = pt;
				color = color == Color.GREEN ? Color.CYAN : color == Color.CYAN ? Color.MAGENTA : Color.GREEN;
			}
		}
	}
	
	public void coneDebug(LightWorld world, Graphics2D g) {
		Segment.clear();
		Artist a = new Artist();
		if (world.getLightSources().isEmpty()) return;
		LightSource source = world.getLightSources().get(0);
		
		setup(source, world);
		sweep(source);
		
		a.setStroke(false);
		a.setFillPaint(new RadialGradientPaint(Viewport.gamePtToScreen(source.getLocation()).x, Viewport
				.gamePtToScreen(source.getLocation()).y, Viewport.gameFloatToScreen(800f), new float[] { 0f, 1f },
				new Color[] { new Color(0.7f, 0.7f, 1f, 0.8f), new Color(0f, 0f, 0f, 0f) }));
		a.path(g, pointConvert(source.getPoly()));
	}
	
	public List<Vec2f> pointConvert(List<Vec2f> input) {
		List<Vec2f> return_value = new ArrayList<Vec2f>();
		for (Vec2f v : input) {
			return_value.add(Viewport.gamePtToScreen(v));
		}
		return return_value;
	}
	
	public void onKeyPressed(java.awt.event.KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		
		case java.awt.event.KeyEvent.VK_ENTER:
			if (rayNum4Debug == points.size() - 1) {
				rayNum4Debug = 0;
			} else {
				rayNum4Debug++;
			}
			break;
		
		case java.awt.event.KeyEvent.VK_SHIFT:
			if (rayNum4Debug == 0) {
				rayNum4Debug = points.size() - 1;
			} else {
				rayNum4Debug--;
			}
			break;
		}
	}
	
	public void onMouseClicked(GameWorld w, MouseEvent e) {
		Vec2f loc = new Vec2f(e.getX(), e.getY());
		
		w.getPlayer().shape.changeLocation(Viewport.screenPtToGame(loc));
		w.getPlayer().velocity = Vec2f.ZERO;
	}
	
	/* ============================================================================
	 * Utility methods for intersection/segment metrics
	 * ========================================================================= */
	
	public static Vec2f midpoint(Vec2f p1, Vec2f p2) {
		return new Vec2f((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
	private static Vec2f intersect(Vec2f A1, Vec2f A2, Vec2f B1, Vec2f B2) {
		Vec2f p = A1;
		Vec2f q = B1;
		
		Vec2f r = A2.minus(A1);
		Vec2f s = B2.minus(B1);
		
		float top = LightingEngine.twoDCross(q.minus(p), s);
		float bottom = LightingEngine.twoDCross(r, s);
		
		if (bottom == 0) return null;
		
		float t = top / bottom;
		
		top = LightingEngine.twoDCross(q.minus(p), r);
		
		float u = top / bottom;
		
		if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			return p.plus(r.smult(t));
		} else {
			return null;
		}
		
	}
	
	private static float twoDCross(Vec2f v, Vec2f w) {
		return (v.x * w.y) - (v.y * w.x);
	}
	
	@SuppressWarnings("unused")
	private static boolean within(float a, float E1, float E2) {
		return a >= E1 && a <= E2 || a >= E2 && a <= E1;
	}
	
	private float slope(Vec2f p1, Vec2f p2) {
		return (p1.y - p2.y) / (p1.x - p2.x);
	}
	
	private List<Vec2f> getCollinearPoints(Vec2f p1, Vec2f p2) {
		List<Vec2f> return_value = new ArrayList<Vec2f>();
		float slope = slope(p1, p2);
		for (Vec2f test : points) {
			if (slope == slope(p1, test)) {
				return_value.add(test);
			}
		}
		return return_value;
	}
	
	private boolean wrongWay(Vec2f location, Vec2f a, Vec2f b) {
		boolean aOnRight = a.x > location.x;
		boolean bOnRight = b.x > location.x;
		boolean aOnTop = a.y > location.y && b.y < location.y;
		boolean bOnTop = b.y > location.y && a.y < location.y;
		
		if (aOnRight && bOnRight) {
			return aOnTop || bOnTop;
		}
		
		if (aOnRight) {
			return bOnTop;
		}
		
		if (bOnRight) {
			return aOnTop;
		}
		
		return false;
	}
}
