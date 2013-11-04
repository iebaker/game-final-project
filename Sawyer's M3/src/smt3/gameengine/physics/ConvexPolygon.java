package smt3.gameengine.physics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import cs195n.Vec2f;

public class ConvexPolygon extends Shape {

	private ArrayList<Vec2f> _points;
	private Color _color;
	private Vec2f _coords;
	private Vec2f _dims;
	
	public ConvexPolygon(Color color, Vec2f...points) {
		_points = new ArrayList<Vec2f>();
		for(Vec2f p : points) {
			_points.add(p);
		}
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxX = 0;
		float maxY = 0;
		for(Vec2f p : _points) {
			if(p.x < minX) {
				minX = p.x;
			}
			if(p.y < minY) {
				minY = p.y;
			}
			if(p.x > maxX) {
				maxX = p.x;
			}
			if(p.y > maxY) {
				maxY = p.y;
			}
		}
		_color = color;
		_coords = new Vec2f(minX, minY);
		_dims = new Vec2f(maxX-minX, maxY-minY);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(_color);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Path2D path = new Path2D.Float();	
		path.moveTo(_points.get(_points.size() - 1).x, _points.get(_points.size() - 1).y);
		for(Vec2f point : _points) {
			path.lineTo(point.x, point.y);	
		}	
		g.fill(path);	
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	@Override
	public Float raycast(Vec2f source, Vec2f dir) {
		Vec2f prev = _points.get(_points.size()-1);
		float minDist = Float.POSITIVE_INFINITY;
		for(Vec2f p : _points) {
			Float castLen = this.linecast(prev, p, dir, source);
			if(castLen != null) {
				Vec2f cast = dir.smult(castLen);
				if(cast.mag() < minDist) {
					minDist = cast.mag();
				}
			}
			prev = p;
		}
		if(Float.isInfinite(minDist)) {
			return null;
		}
		return minDist;
	}

	@Override
	public MTVHolder collide(Shape s) {
		return s.collidePolygon(this);
	}

	@Override
	public MTVHolder collideRect(Rectangle r) {
		ArrayList<SeparatingAxis> sa = new ArrayList<SeparatingAxis>();
		Vec2f prev = _points.get(_points.size()-1);
		for(Vec2f point : _points) {
			Vec2f vect = new Vec2f(point.x-prev.x, point.y-prev.y);
			sa.add(new SeparatingAxis(new Vec2f(vect.y, vect.x*-1).normalized()));
			prev = point;
		}
		
		sa.add(new SeparatingAxis(new Vec2f(0, -1)));
		sa.add(new SeparatingAxis(new Vec2f(1, 0)));
		
		Float minMagnitude = Float.MAX_VALUE;
		Vec2f mtv = null;
		for(SeparatingAxis axis : sa) {
			Float mtv1d = this.rangeMTV(axis.project(this), axis.project(r));
			if(mtv1d == null) {
				return new MTVHolder(null);
			}
			if(Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis._direction.smult(mtv1d);
			}
		}
		return new MTVHolder(mtv);
	}

	@Override
	public MTVHolder collideCircle(Circle c) {
		ArrayList<SeparatingAxis> sa = new ArrayList<SeparatingAxis>();
		Vec2f prev = _points.get(_points.size()-1);
		for(Vec2f point : _points) {
			Vec2f vect = new Vec2f(point.x-prev.x, point.y-prev.y);
			sa.add(new SeparatingAxis(new Vec2f(vect.y, vect.x*-1).normalized()));
			prev = point;
		}
		
		//find the closest point on the polygon to the center of the circle
		Vec2f cPoint = _points.get(0);
		float minDist = Float.MAX_VALUE;
		for(Vec2f point : _points) {
			if(point.dist(c.getCenter()) <= minDist) {
				minDist = point.dist(c.getCenter());
				cPoint = point;
			}
		}
		sa.add(new SeparatingAxis(cPoint.minus(c.getCenter()).normalized()));
		
		Float minMagnitude = Float.MAX_VALUE;
		Vec2f mtv = null;
		for(SeparatingAxis axis : sa) {
			//find the length of the mtv
			Float mtv1d = this.rangeMTV(axis.project(this), axis.project(c));
			//if there's no mtv, there's no collision
			if(mtv1d == null) {
				return new MTVHolder(null);
			}
			//if the current mtv is the smallest one so far, make it the actual 
			if(Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis._direction.smult(mtv1d);
			}
		}
		return new MTVHolder(mtv);
	}


	@Override
	public boolean contains(Vec2f p) {
		Vec2f prev = _points.get(_points.size()-1);
		for(Vec2f corner : _points) {
			Vec2f dist = corner.minus(prev);
			Vec2f toP = corner.minus(p);
			float cross = dist.cross(toP);
			prev = corner;
			if(cross < 0) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public MTVHolder collidePolygon(ConvexPolygon p) {
		ArrayList<SeparatingAxis> sa = new ArrayList<SeparatingAxis>();
		Vec2f prev = _points.get(_points.size()-1);
		for(Vec2f point : _points) {
			//constructs a vector in the direction of one side of the polygon
			Vec2f slope = point.minus(prev);
			//adds the perpendicular, normalized vector of that side to the list
			sa.add(new SeparatingAxis(new Vec2f(slope.y, slope.x*-1).normalized()));
			prev = point;
		}
		
		prev = p.getPoints().get(p.getPoints().size()-1);
		for(Vec2f point : p.getPoints()) {
			Vec2f slope = point.minus(prev);
			sa.add(new SeparatingAxis(new Vec2f(slope.y, slope.x*-1).normalized()));
			prev = point;
		}
		
		Float minMagnitude = Float.MAX_VALUE;
		Vec2f mtv = null;
		for(SeparatingAxis axis : sa) {
			//find the length of the mtv
			Float mtv1d = this.rangeMTV(axis.project(this), axis.project(p));
			//if there's no mtv, there's no collision
			if(mtv1d == null) {
				return new MTVHolder(null);
			}
			//if the current mtv is the smallest one so far, make it the actual 
			if(Math.abs(mtv1d) < minMagnitude) {
				minMagnitude = Math.abs(mtv1d);
				mtv = axis._direction.smult(mtv1d);
			}
		}
		return new MTVHolder(mtv);
	}

	@Override
	public void setColor(Color c) {
		_color = c;
	}

	@Override
	public void setCoords(Vec2f coords) {
		Vec2f dif = coords.minus(_coords);
		for(Vec2f p : _points) {
			p = p.plus(dif);
		}
		for(int i=0; i<_points.size(); i++) {
			_points.set(i, _points.get(i).plus(dif));
		}
		_coords = coords;
	}

	@Override
	public Vec2f getCoords() {
		return _coords;
	}

	@Override
	public Vec2f getDims() {
		return _dims;
	}
	
	public ArrayList<Vec2f> getPoints() {
		return _points;
	}
	
	@Override
	public Vec2f getCenter() {
		return _coords.plus(_dims.sdiv(2));
	}
}
