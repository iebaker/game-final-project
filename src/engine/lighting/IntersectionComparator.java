package engine.lighting;

import java.util.Comparator;
import cs195n.Vec2f;

public class IntersectionComparator implements Comparator<Intersection> {
	private Vec2f sourcePoint;	

	public IntersectionComparator(Vec2f sourcePoint) {
		this.sourcePoint = sourcePoint;
	}
	
	@Override
	public int compare(Intersection i1, Intersection i2) {
		Vec2f p1 = i1.getPoint(); Segment s1 = i1.getSegment();
		Vec2f p2 = i2.getPoint(); Segment s2 = i2.getSegment();

		float dist1 = sourcePoint.dist(p1);
		float dist2 = sourcePoint.dist(p2);

		if(dist1 < dist2) {
			return -1;		//I1 is closer							
		} else if(dist1 > dist2) {
			return 1;			//I2 is closer
		} else {
			if(p1.equals(s1.getEndPoint())) {
				return 1; 		//I1 is an endpoint for S1, so I2 is closer
			}

			if(p2.equals(s2.getEndPoint())) {
				return -1;		//I2 is an endpoint for S2, so I1 is closer
			}

			//Now the hard part... neither I2 or I1 are endpoints.  WLOG select I1 as our
			//test intersection.

			Vec2f test = LightingEngine.intersect(sourcePoint, s1.getEndPoint(), p2, s2.getEndPoint());
			if(test != null) {
				return 1; 			//The line from the source to I1's segment's end point crosses I2's segment.  I2 is closer.
			}

			return -1;				//The only option left is I1 being closer.
		}
	}
}
