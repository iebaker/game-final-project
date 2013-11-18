package src.engine.lighting;

import cs195n.Vec2f;
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
		for(LightSource light : world.getLightSources()) {
			
			//Reset points and line segments.  They must be recalculated for each light source.
			points = new ArrayList<Vec2f>(); 
			lineSegments = new ArrayList<Segment>();

			//Create a comparator object which will be used to arrange the points
			Vec2f lightLocation = light.getLocation();
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

		this.sweep();
	}

	/**
	 * This method actually runs the RedBlob sweepline algorithm in order to calculate lighting cones
	 */
	private void sweep() {
		
		//Set up preliminary values
		RayCastData rcd = this.doRayCast(lightLocation, points.get(0));
		Segment prevSegment = rcd.minSegment();
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

				if(point.isBeginPoint()) {
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
		//TODO: Implement raycasting over this segment world model
	}
}

