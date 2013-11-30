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

			//Now the hard part... neither I2 or I1 are endpoints.

			Vec2f rayVector = p1.minus(sourcePoint);
			Vec2f v1 = s1.asVector();
			Vec2f v2 = s2.asVector();

			float theta1 = this.angleBetween(rayVector, v1);
			float theta2 = this.angleBetween(rayVector, v2);

			if(theta1 > theta2) {
				return -1;
			} else {
				return 1;
			}
		}
	}

	public float angleBetween(Vec2f v, Vec2f w) {
		float v_dot_w = v.dot(w);
		float mag_v = v.mag();
		float mag_w = w.mag();

		return (float) Math.acos(v_dot_w/(mag_v * mag_w));
	}
}
