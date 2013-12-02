package engine.lighting;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.RadialGradientPaint;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.BasicStroke;

import cs195n.Vec2f;
import engine.Artist;
import engine.Viewport;

import game.GameWorld;

/**
 * LightingEngine is a class whose main function is to actually perform the sweepline algorithm which 
 * computes for each LightSource in the world a set of LightCone objects representing the areas lit
 * by this LightSource.  It also currently contains debugging functions which can be used to visualize
 * the raycasting and cone creation algorithms.
 *
 * @author iebaker
 */
public class LightingEngine {

	private List<Segment> lineSegments = new ArrayList<Segment>();
	private List<Vec2f> points = new ArrayList<Vec2f>();
	private ConeBuilder builder = new ConeBuilder();
	private int rayNum4Debug = 0;

	/**
	 * Performs sweepline cone creation algorithm for every LightSource the world returns from its
	 * getLightSources() method.  The LightSource objects themselves will be mutated, so this
	 * method does not return anything.
	 *
	 * @param world  	The LightWorld in which to run these calculations.
	 */
	public void run(LightWorld world) {
		for(LightSource light : world.getLightSources()) {
			this.setup(light, world);
			this.sweep(light);
		}
	}

	/**
	 * Populates instance variables of this class (points, lineSegments) by querying the world
	 * for point and pair information, ordering the points, and orienting the pairs into Segment
	 * objects by angle around the source point. 
	 *
	 * @param light 	The LightSource about which to perform lighting calculations
	 * @param world 	The LightWorld in which this LightSource lives
	 */
	private void setup(LightSource light, LightWorld world) {
		this.lineSegments = new ArrayList<Segment>();
		this.points = new ArrayList<Vec2f>();
		this.builder.reset(light);

		Vec2f lightLocation = light.getLocation();
		AngularComparator ac = new AngularComparator(lightLocation);
		List<Vec2fPair> pointPairs = world.getPointsAndPairs(lightLocation, this.points);
		Collections.sort(this.points, ac);

		for(Vec2fPair pair : pointPairs) {
			Segment segment = new Segment();

			Vec2f a = pair.getP1();
			Vec2f b = pair.getP2();

			if(this.wrongWay(lightLocation, a, b)) {
				if(ac.compare(a, b) <= 0) {
					segment.setBeginPoint(b);
					segment.setEndPoint(a);
				} else {
					segment.setBeginPoint(a);
					segment.setEndPoint(b);
				}
				segment.flip();
			} else {
				if(ac.compare(a, b) <= 0) {
					segment.setBeginPoint(a);
					segment.setEndPoint(b);
				} else {
					segment.setBeginPoint(b);
					segment.setEndPoint(a);
				}
			}

			this.lineSegments.add(segment);
		}
	}

	/**
	 * Actually performs the sweepline algorithm, using the ConeBuilder object owned by 
	 * this class (maybe should make those into static methods?) in order to create LightCones
	 *
	 * @param light 	The LightSource around which sweeping is performed.  Mutated by the method.
	 */
	private void sweep(LightSource light) {
		if(this.points.isEmpty()) return;

		RayCastData rcd = this.doRayCast(light.getLocation(), this.points.get(0));
		Segment closest;
		try {
			closest = rcd.minSegment();
		} catch(Exception e) {
			System.out.println("Error raycast (1) : " + rcd);
			return;
		}
		AngularComparator comp = new AngularComparator(light.getLocation());

		int i = 0;
		Vec2f first = null;
		while(i < this.points.size()) {

			Vec2f currentPoint = points.get(i);
			RayCastData currentRCD = this.doRayCast(light.getLocation(), currentPoint);
			Segment currentClosest;
			try {
				currentClosest = currentRCD.minSegment();
			} catch (Exception e) {
				System.out.println("Error raycast (2) : " + currentRCD);
				i++;
				continue;
			}

			if(this.builder.unStarted()) {
				first = currentRCD.minPoint();
				this.builder.open(first);
				i++;
				continue;
			}

			boolean closestEnded = Segment.endingAt(currentPoint).contains(closest);
			boolean newClosest = currentClosest != closest;
			Vec2f testInt = LightingEngine.intersect(closest.getBeginPoint(), closest.getEndPoint(), currentClosest.getBeginPoint(), currentClosest.getEndPoint());
			boolean wasIntersection = testInt != null;


			if(closestEnded && newClosest && wasIntersection) {
				this.builder.close(testInt);
				this.builder.open(testInt);
			}
			else if(closestEnded) {
				this.builder.close(currentPoint);

				if(currentPoint.isStart()) {
					this.builder.open(currentPoint);
				} else {
					this.builder.open(currentRCD.minPoint());
				}
			} else if(newClosest) {
				
				if(wasIntersection) {
					this.builder.close(testInt);
					this.builder.open(testInt);
				}	else {
					try {
						this.builder.close(currentRCD.getUniquePoints().get(1));
						this.builder.open(currentPoint);
					} catch(Exception e) {
						System.out.println("Error raycast (3) : " + currentRCD);
					}
				}
			}

			closest = currentClosest;
			i++;
		}

		this.builder.close(first);
		light.setLightCones(this.builder.getCones());
	}

	private RayCastData doRayCast(Vec2f sourcePoint, Vec2f targetPoint) {
		List<Vec2f> collinears = this.getCollinearPoints(sourcePoint, targetPoint);

		for(Vec2f point : collinears) {
			Segment.ignoreEndingAt(point);
			Segment.ignoreBeginningAt(point);
		}

		RayCastData rcd_return = new RayCastData(sourcePoint);
		Vec2f direction = targetPoint.minus(sourcePoint);
		direction = direction.normalized().smult(10000000);

		for(Vec2f point : collinears) {
			for(Segment s : Segment.beginningAt(point)) {
				rcd_return.addIntersection(point, s);
			}
		}

		for (Segment segment : lineSegments) {
			if(segment.isIgnored()) continue;
			Vec2f newIntersection = LightingEngine.intersect(sourcePoint, sourcePoint.plus(direction),
					segment.getBeginPoint(), segment.getEndPoint());
			if (newIntersection != null) {
				rcd_return.addIntersection(newIntersection, segment);
			}
		}
		
		for(Vec2f point : collinears) {
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

		if(world.getLightSources().isEmpty()) return;

		this.run(world);
		LightSource source = world.getLightSources().get(0);

		a.setFillPaint(Color.BLACK);
		a.setStroke(false);
		for(Vec2f point : this.points) {
			a.ellipse(g, point.x, point.y, 2, 2);
		}

		for(Segment segment : this.lineSegments) {
			a.line(g, segment.getBeginPoint().x, segment.getBeginPoint().y, segment.getEndPoint().x, segment.getEndPoint().y);
		}

		List<LightCone> cones = source.getLightCones();
		for(LightCone cone : cones) {
			a.setFillPaint(cone.getColor());
			a.path(g, cone.getPoints());
		}
	}

	public void rayDebug(LightWorld world, Graphics2D g) {
		Segment.clear();

		Artist a = new Artist();

		if(world.getLightSources().isEmpty()) return;
		LightSource source = world.getLightSources().get(0);
		Vec2f convLSpoint = Viewport.gamePtToScreen(source.getLocation());

		this.setup(source, world);

		if(this.points.isEmpty()) return;

		Vec2f size = ((GameWorld)world).getWorldSize();
		a.setFillPaint(Color.WHITE);
		a.rect(g, 0, 0, size.x, size.y);

		a.setFillPaint(Color.BLACK);
		a.ellipse(g, convLSpoint.x - 5, convLSpoint.y - 5, 10, 10);

		for(Vec2f point : this.points) {
			a.ellipse(g, Viewport.gamePtToScreen(point).x, Viewport.gamePtToScreen(point).y, 2, 2);
		}

		for(Segment segment : this.lineSegments) {
			if(segment.isFlipped()) {
				g.setStroke(new BasicStroke(3));
			} else {
				g.setStroke(new BasicStroke(1));
			}
			Vec2f begin = Viewport.gamePtToScreen(segment.getBeginPoint());
			Vec2f end = Viewport.gamePtToScreen(segment.getEndPoint());
			Vec2f mid = this.midpoint(begin, end);
			a.setStrokePaint(Color.RED);
			a.line(g, begin.x, begin.y, mid.x, mid.y);
			a.setStrokePaint(Color.BLUE);
			a.line(g, mid.x, mid.y, end.x, end.y);
		}

		for(int i = 0; i < this.points.size(); ++i) {
			Vec2f point = this.points.get(i);
			RayCastData rcd = this.doRayCast(source.getLocation(), point);
			Vec2f prev = Viewport.gamePtToScreen(source.getLocation());
			Vec2f convpt = Viewport.gamePtToScreen(point);
			a.setFillPaint(Color.RED);
			a.ellipse(g, convpt.x, convpt.y, 5, 5);
			Color color = Color.GREEN;
			g.setStroke(new BasicStroke(2));

			if(i == this.rayNum4Debug) {
				Segment min = rcd.minSegment();
				Vec2f p1 = Viewport.gamePtToScreen(min.getBeginPoint());
				Vec2f p2 = Viewport.gamePtToScreen(min.getEndPoint());
				
				a.setStrokePaint(new Color(0f, 0f, 0f, 0.5f));
				g.setStroke(new BasicStroke(6));
				a.line(g, p1.x, p1.y, p2.x, p2.y);
				a.line(g, prev.x, prev.y, convpt.x, convpt.y);
			}

			g.setStroke(new BasicStroke(1));
			for(Intersection intersection : rcd.getIntersections()) {
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
		if(world.getLightSources().isEmpty()) return;
		LightSource source = world.getLightSources().get(0);

		this.setup(source, world);
		this.sweep(source);

		a.setStroke(false);		
		for(LightCone cone : source.getLightCones()) {
			a.setFillPaint(new RadialGradientPaint(Viewport.gamePtToScreen(source.getLocation()).x, 
				Viewport.gamePtToScreen(source.getLocation()).y, 300f, new float[]{0f, 1f}, 
				new Color[]{new Color(1f, 1f, 0f, 0.6f), new Color(0f, 0f, 0f, 0f)}));
			//a.setFillPaint(new Color(1f, 1f, 0f, 0.5f));
			//a.setFillPaint(Color.YELLOW);
			a.path(g, this.pointConvert(cone.getPoints()));
		}
	}

	public List<Vec2f> pointConvert(List<Vec2f> input) {
		List<Vec2f> return_value = new ArrayList<Vec2f>();
		for(Vec2f v : input) {
			return_value.add(Viewport.gamePtToScreen(v));
		}
		return return_value;
	}

	public void onKeyPressed(java.awt.event.KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {

			case java.awt.event.KeyEvent.VK_ENTER:
				if(this.rayNum4Debug == this.points.size() - 1) {
					this.rayNum4Debug = 0;
				} else {
          			this.rayNum4Debug++;
				}
				break;

			case java.awt.event.KeyEvent.VK_SHIFT:
				if(this.rayNum4Debug == 0) {
					this.rayNum4Debug = this.points.size() - 1;
				} else {
					this.rayNum4Debug--;
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
		return new Vec2f((p1.x + p2.x)/2, (p1.y + p2.y)/2);
	}

	private static Vec2f intersect(Vec2f A1, Vec2f A2, Vec2f B1, Vec2f B2) {
		Vec2f p = A1;
		Vec2f q = B1;

		Vec2f r = A2.minus(A1);
		Vec2f s = B2.minus(B1);

		float top = LightingEngine.twoDCross(q.minus(p), s);
		float bottom = LightingEngine.twoDCross(r, s);

		if(bottom == 0) return null;

		float t = top/bottom;

		top = LightingEngine.twoDCross(q.minus(p), r);

		float u = top/bottom;

		if(t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			return p.plus(r.smult(t));
		} else {
			return null;
		}

	}

	private static float twoDCross(Vec2f v, Vec2f w) {
		return (v.x * w.y) - (v.y * w.x);
	}

	private static boolean within(float a, float E1, float E2) {
		return a >= E1 && a <= E2 || a >= E2 && a <= E1;
	}

	private float slope(Vec2f p1, Vec2f p2) {
		return (p1.y - p2.y) / (p1.x - p2.x);
	}

	private List<Vec2f> getCollinearPoints(Vec2f p1, Vec2f p2) {
		List<Vec2f> return_value = new ArrayList<Vec2f>();
		float slope = this.slope(p1, p2);
		for(Vec2f test : this.points) {
			if(slope == this.slope(p1, test)) {
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

		if(aOnRight && bOnRight) {
			return aOnTop || bOnTop;
		}

		if(aOnRight) {
			return bOnTop;
		}

		if(bOnRight) {
			return aOnTop;
		}

		return false;
	}

/* ============================================================================
 * ConeBuilder class
 * ========================================================================= */

	private class ConeBuilder {
		private List<LightCone> cones = new ArrayList<LightCone>();
		private Vec2f openPoint = null;
		private Vec2f sourcePoint = null;

		public void reset(LightSource source) {
			cones = new ArrayList<LightCone>();
			openPoint = null;
			sourcePoint = source.getLocation();
		}

		public void open(Vec2f point) {
			if(openPoint == null) {
				openPoint = point;
			} else {
				System.err.println("Cannot open new cone, already unclosed cone!");
			}
		}

		public void close(Vec2f point) {
			if(openPoint != null) {
				LightCone cone = new LightCone(new Color(1f, 1f, 0f, 0.5f), sourcePoint, point, openPoint);
				cones.add(cone);
				openPoint = null;
			} else {
				System.err.println("Cannot close cone, no open cone to close");
			}
		}

		public boolean unStarted() {
			return this.cones.isEmpty() && this.openPoint == null;
		}

		public List<LightCone> getCones() {
			return this.cones;
		}
	}
}
