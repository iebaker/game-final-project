package engine.lighting;

import cs195n.Vec2f;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * A class which contains methods to actually perform lighting cone calculations for each
 * LightSource in a world. 
 */
public class LightingEngine {
	
	private List<Vec2f> points = new ArrayList<Vec2f>();
	private List<Segment> lineSegments = new ArrayList<Segment>();

	/**
	 * Public-facing function which loops through each light source and computes its light
	 * cones using a sweepline algorithm over the points and segments in the world.
	 *
	 * @param world  	The LightWorld object over which to perform lighting calculations
	 */
	public void run(LightWorld world) {
		Vec2f lightLocation = null;
		for(LightSource light : world.getLightSources()) {
			
			//Reset points and line segments.  They must be recalculated for each light source.
			points = new ArrayList<Vec2f>(); 
			lineSegments = new ArrayList<Segment>();

			//Create a comparator object which will be used to arrange the points
			lightLocation = light.getLocation();
			AngularComparator ac = new AngularComparator(lightLocation);

			//Get the points from the world and sort them according to their angle relative to the light source's sweepline
			points = world.getPoints(lightLocation);
			Collections.sort(points, ac);

			//Create line segments oriented by the direction in which the sweepline will pass over them
			for(Vec2fPair pair : world.getPointPairs(lightLocation)) {
				Segment segment = new Segment();

				Vec2f a = pair.getP1();
				Vec2f b = pair.getP2();

				int comp = ac.compare(a, b);

				if(comp <= 0) {
					segment.setBeginPoint(a);		//TODO: Make this work even with
					segment.setEndPoint(b);			//the case of a segment whose endpoint 
				}  else {							//and beginning point are on the wrong
					segment.setBeginPoint(b);		//sides of the list...
					segment.setEndPoint(a);
				}

				lineSegments.add(segment);
			}
		}

		this.sweep(lightLocation);
	}

	/**
	 * This method actually runs the RedBlob sweepline algorithm in order to calculate lighting cones
	 */
	private void sweep(Vec2f lightLocation) {
		
		//Set up preliminary values
		RayCastData rcd = this.doRayCast(lightLocation, points.get(0));
		Segment prevSegment = rcd.minSegment();
		List<LightCone> cones = new ArrayList<LightCone>();
		points.add(points.get(0));
		int i = 1;

		//Sweep!
		while(true) {
			if(i >= points.size()) break;

			Vec2f point = points.get(i);
			rcd = this.doRayCast(lightLocation, point);
			Segment closest = rcd.minSegment();

			if(point == prevSegment.getEndPoint()) {
				LightCone lc = new LightCone(lightLocation, point, prevSegment.getBeginPoint());
				cones.add(lc);

				if(point.isStart()) {
					++i;
				} else {
					rcd.removePoint(point);
					points.add(i + 1, rcd.minPoint());
					i += 2;
				}
				closest = rcd.minSegment();
			} else if(closest != prevSegment) {
				rcd.removePoint(point);
				LightCone lc = new LightCone(lightLocation, rcd.minPoint(), prevSegment.getBeginPoint());
				cones.add(lc);
				++i;
			}

			prevSegment = closest;
		}
	}

	/**
	 * Runs a raycast on the current world.
	 *
	 * @param sourcePoint	The source of the ray
	 * @param targetPoint	A point to aim the ray at (such that targetPoint - sourcePoint is the direction vector of this ray)
	 * @return 				A RayCastData object representing the result of the cast
	 */
	private RayCastData doRayCast(Vec2f sourcePoint, Vec2f targetPoint) {
		RayCastData rcd_return = new RayCastData();
		Vec2f direction = targetPoint.minus(sourcePoint);
		direction = direction.normalized().smult(100000);
		Vec2f intersection = new Vec2f(0,0);

		for(Segment segment : lineSegments) {
			if(intersect(sourcePoint, sourcePoint.plus(direction), segment.getBeginPoint(), segment.getEndPoint(), intersection)) {
				rcd_return.addIntersection(intersection, segment);
			}
		}
	}

	/**
	 * This function determines if two line segments intersect each other, and if they do, the value of
	 * the variable intersection is set to the intersection point of the two segments.
	 *
	 * @param A1 			The beginning of the first segment
	 * @param A2 			The end of the first segment
	 * @param B1			The beginning of the second segment
	 * @param B2			The end of the second segment
	 * @param intersection	A Vec2f which will be set 
	 * @return				True, if the points define intersecting segments, false otherwise
	 */
	private static boolean intersect(Vec2f A1, Vec2f A2, Vec2f B1, Vec2f B2, Vec2f intersection) {
		float mA = (A2.y - A1.y) / (A2.x - A1.x);
		float mB = (B2.y - B1.y) / (B2.x - B1.x);

		float intX = ((mA * A1.x) - A1.y - (mB * B1.x) + B1.y) / (mA - mB);
		float intY - (mA *(intX - A1.x)) + A1.y;

		if(	within(intX, A1.x, A2.x) && 
		   	within(intX, B1.x, B2.x) &&
		   	within(intY, A1.y, A2.y) &&
		   	within(intY, B1.y, B2.y)) {
			if(intersection != null) {
				intersection.x = intX;
				intersection.y = intY;
			}
			return true;
		}
		return false;
	}

	/**
	 * Determines whether a value is in between two other values, with no regards to the order the
	 * limiting values will be given.
	 *
	 * @param a 	The value in question
	 * @param E1	One of the bounds 
	 * @param E2	The other bound
	 * @return 		True, if a is between E1 and E2.
	 */
	private static boolean within(float a, float E1, float E2) {
		return a >= E1 && a <= E2 || a >= E2 && a <= E1;
	}
}

