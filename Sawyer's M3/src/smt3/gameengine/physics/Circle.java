package smt3.gameengine.physics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import cs195n.Vec2f;

public class Circle extends Shape {
	
	private Vec2f _coords;
	private float _diameter;
	private Color _color;

	public Circle(Vec2f coords, float diameter, Color color) {
		_coords = coords;
		_diameter = diameter;
		_color = color;
	}
	
	@Override
	public Float raycast(Vec2f source, Vec2f dir) {
		if(this.contains(source)) {
			//find the distance needed to translate the ray from (0,0) to where it is
			Vec2f trans = source.minus(source.projectOnto(dir));
			//get the point that forms a right angle between the source and the center of the circle along the ray
			Vec2f projection = this.getCenter().projectOnto(dir).plus(trans);
			//get the distance between the center of the circle and that point
			float dist1 = -(this.getCenter().minus(projection)).mag();
			//use the pythagorean theorem to find the length of the part of the ray inside the circle
			float innerDist = (float) Math.sqrt(Math.pow(this.getRadius(), 2) - Math.pow(dist1, 2));
			//find the actual length of the ray
			float finLen = projection.minus(source).mag() + innerDist;
			//return that length, multiplied by the correct direction
			return finLen;
		}
		else {
			//find the distance needed to translate the ray from (0,0) to where it is
			Vec2f trans = source.minus(source.projectOnto(dir));
			//get the point that forms a right angle between the source and the center of the circle along the ray
			Vec2f projection = this.getCenter().projectOnto(dir).plus(trans);
			Vec2f difToTest = projection.minus(source).normalized().minus(dir);
			if(difToTest.x <= 0.01 && difToTest.y <= 0.01 && difToTest.x >= -.01 && difToTest.y >= -.01) {
				//get the distance between the center of the circle and that point
				float dist1 = (this.getCenter().minus(projection)).mag();
				float innerDist = (float) Math.sqrt(Math.pow(this.getRadius(), 2) - Math.pow(dist1, 2));
				//find the actual length of the ray
				float finLen = projection.minus(source).mag() - innerDist;
				//return that length, multiplied by the correct direction
				return finLen;
			}
			else return null;
		}
	}
	
	@Override
	public MTVHolder collide(Shape s) {
		return s.collideCircle(this);
	}

	@Override
	public MTVHolder collideRect(Rectangle r) {
		Vec2f mtv = r.collideCircle(this).mtv;
		if(mtv == null) {
			return new MTVHolder(mtv);
		}
		return new MTVHolder(mtv.smult(-1));
	}

	@Override
	public MTVHolder collideCircle(Circle c) {
		Vec2f dist = c.getCenter().minus(this.getCenter());
		float mtvLen = (float) (c.getRadius()+this.getRadius()-(Math.sqrt(Math.pow(dist.x, 2) + Math.pow(dist.y, 2))));
		if(mtvLen < 0) {
			return new MTVHolder(null);
		}
		return new MTVHolder(dist.normalized().smult(mtvLen));
		/*
		if(Math.pow(c.getCenter().x - this.getCenter().x, 2) + Math.pow(c.getCenter().y - this.getCenter().y, 2) <= Math.pow(this.getRadius() + c.getRadius(), 2)) {
			return true;
		}
		return false;*/
	}

	@Override
	public MTVHolder collidePolygon(ConvexPolygon p) {
		Vec2f mtv = p.collideCircle(this).mtv;
		if(mtv == null) {
			return new MTVHolder(mtv);
		}
		return new MTVHolder(mtv.smult(-1));
	}
	
	@Override
	public boolean contains(Vec2f p) {
		if(Math.pow(p.x - this.getCenter().x, 2) + Math.pow(p.y - this.getCenter().y, 2) <= Math.pow(_diameter/2, 2)) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(_color);
		g.fill(new Ellipse2D.Float(_coords.x, _coords.y, _diameter, _diameter));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	public Vec2f getCoords() {
		return _coords;
	}

	public void setCoords(Vec2f coords) {
		_coords = coords;
	}

	public float getDiameter() {
		return _diameter;
	}

	public void setDiameter(float diameter) {
		_diameter = diameter;
	}

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}
	
	public Vec2f getCenter() {
		return new Vec2f((float) (_coords.x + .5*_diameter), (float) (_coords.y + .5*_diameter));
	}
	
	public void setCenter(Vec2f pos) {
		this.setCoords(pos.minus(this.getRadius(), this.getRadius()));
	}
	
	public float getRadius() {
		return _diameter/2;
	}

	@Override
	public Vec2f getDims() {
		// TODO Auto-generated method stub
		return new Vec2f(_diameter, _diameter);
	}
}