package smt3.gameengine.physics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import cs195n.Vec2f;

public class Rectangle extends Shape {
	
	private Vec2f _coords;
	private Vec2f _dims;
	private Color _color;

	public Rectangle(Vec2f coords, Vec2f dims, Color color) {
		_coords = coords;
		_dims = dims;
		_color = color;
	}
	
	@Override
	public Float raycast(Vec2f source, Vec2f dir) {
		ArrayList<Vec2f> points = new ArrayList<Vec2f>(4);
		points.add(_coords);
		points.add(_coords.plus(_dims.x, 0));
		points.add(_coords.plus(_dims));
		points.add(_coords.plus(0, _dims.y));
		
		Vec2f prev = points.get(points.size()-1);
		float minDist = Float.POSITIVE_INFINITY;
		for(Vec2f p : points) {
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
		return s.collideRect(this);
	}

	@Override
	public MTVHolder collideRect(Rectangle r) {
		ArrayList<SeparatingAxis> sa = new ArrayList<SeparatingAxis>();
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
		sa.add(new SeparatingAxis(new Vec2f(1,0)));
		sa.add(new SeparatingAxis(new Vec2f(0,1)));
		if(!this.contains(c.getCenter())) {
			float cx = c.getCenter().x;
			float cy = c.getCenter().y;
			float p1;
			float p2;
			//find the x part of the clamp
			if(cx <= _coords.x) {
				p1 = _coords.x;
			}
			else if(c.getCenter().x >= _coords.x + _dims.x) {
				p1 = _coords.x + _dims.x;
			}
			else {
				p1 = cx;
			}
			//find the y part of the clamp
			if(cy <= _coords.y) {
				p2 = _coords.y;
			}
			else if(cy >= _coords.y + _dims.y) {
				p2 = _coords.y + _dims.y;
			}
			else {
				p2 = cy;
			}
			sa.add(new SeparatingAxis(new Vec2f(p1,p2).minus(c.getCenter()).normalized()));
		}
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
		//return new MTVHolder(null);
	}
	
	@Override
	public boolean contains(Vec2f p) {
		if(p.x >= _coords.x && p.x <= _coords.x + _dims.x) {
			if(p.y >= _coords.y && p.y <= _coords.y + _dims.y) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public MTVHolder collidePolygon(ConvexPolygon p) {
		Vec2f mtv = p.collideRect(this).mtv;
		if(mtv == null) {
			return new MTVHolder(mtv);
		}
		return new MTVHolder(mtv.smult(-1));
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(_color);
		g.fill(new Rectangle2D.Float(_coords.x, _coords.y, _dims.x, _dims.y));
	}

	public Vec2f getCoords() {
		return _coords;
	}

	public void setCoords(Vec2f coords) {
		_coords = coords;
	}

	public Vec2f getDims() {
		return _dims;
	}

	public void setDims(Vec2f dims) {
		_dims = dims;
	}

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}

	@Override
	public Vec2f getCenter() {
		return _coords.plus(_dims.sdiv(2));
	}
}
