package smt3.gameengine.physics;

import java.awt.Graphics2D;

import cs195n.Vec2f;
import smt3.gameengine.other.World;

public class Entity {
	
	private Vec2f _velocity;
	private Vec2f _impulse = new Vec2f(0,0);
	private Vec2f _force = new Vec2f(0,0);
	private Shape _shape;
	private Shape _boundingBox;
	private float _mass;
	private boolean _bounded = false;
	private boolean _drawBound = false;
	private World _world;
	private Vec2f _lastMTV = new Vec2f(0,0);
	private float _restitution = 1;
	private boolean _static = false;
	private float _massMult = 1;
	
	public Entity(Shape shape, World world, float density) {
		_world = world;
		_mass = density*shape.getDims().x*shape.getDims().y * _massMult;
		_shape = shape;
		_boundingBox = _shape;
		_velocity = new Vec2f(0,0);
	}
	
	public Entity(Shape shape, World world, float density, float restitution) {
		_world = world;
		_mass = density*shape.getDims().x*shape.getDims().y * _massMult;
		_shape = shape;
		_boundingBox = _shape;
		_velocity = new Vec2f(0,0);
		_restitution = restitution;
	}
	
	public Entity(Shape shape, World world, float density, Vec2f velocity) {
		_world = world;
		_shape = shape;
		_boundingBox = _shape;
		_velocity = velocity;
		_mass = density*shape.getDims().x*shape.getDims().y * _massMult;
	}
	
	public Entity(Shape shape, World world, float density, Vec2f velocity, Vec2f acceleration) {
		_world = world;
		_shape = shape;
		_boundingBox = _shape;
		_mass = density*shape.getDims().x*shape.getDims().y * _massMult;
		_velocity = velocity;
	}

	public void onTick(long nanosSinceLastTick) {
		_lastMTV = new Vec2f(0,0);
		//add force and impulse to velocity
		_velocity = _velocity.plus((_force.pdiv(_mass, _mass)).pmult((float) nanosSinceLastTick/200000000, (float) nanosSinceLastTick/200000000));
		_velocity = _velocity.plus(_impulse.pdiv(_mass, _mass));
		//reset the values of force and impulse
		_force = new Vec2f(0,0);
		_impulse = new Vec2f(0,0);
		//move the object
		float xChange = _velocity.pmult((float) nanosSinceLastTick/200000000, (float) nanosSinceLastTick/200000000).x;
		float yChange = _velocity.pmult((float) nanosSinceLastTick/200000000, (float) nanosSinceLastTick/200000000).y;
		if(_shape.getCoords().x + xChange < 0) {
			if(_bounded) {
				xChange = 0;
			}
			else if(_shape.getCoords().x + xChange +_shape.getDims().x < -10){
				outOfBounds();
			}
		}
		
		if(_shape.getCoords().x + _shape.getDims().x + xChange> _world.getGameDims().x) {
			if(_bounded) {
				xChange = 0;
			}
			else if(_shape.getCoords().x > _world.getGameDims().x + 10) {
				outOfBounds();
			}
		}
		
		if(_shape.getCoords().y + yChange < 0) {
			if(_bounded) {
				yChange = 0;
			}
			else if(_shape.getCoords().y + yChange +_shape.getDims().y < -10){
				outOfBounds();
			}
		}
		
		if(_shape.getCoords().y + _shape.getDims().y + yChange > _world.getGameDims().y) {
			if(_bounded) {
				yChange = 0;
			}
			else if(_shape.getCoords().y + yChange > _world.getGameDims().y + 10) {
				outOfBounds();
			}
		}
		_shape.setCoords(_shape.getCoords().plus(xChange, yChange));
		if(!_boundingBox.equals(_shape)) {
			_boundingBox.setCoords(_boundingBox.getCoords().plus(xChange, yChange));
		}
		_lastMTV = new Vec2f(0,0);
	}
	
	public void onDraw(Graphics2D g) {
		_shape.draw(g);
		if(_drawBound) {
			_boundingBox.draw(g);
		}
	}
	
	public void onCollide(CollisionInfo c) {
		if(!c.mtv.isZero() && !_static) {
			_lastMTV = c.mtv;
			if(c.other.isStatic()) {
				this.setCoords(this.getCoords().plus(c.mtv));
			}
			else {
				this.setCoords(this.getCoords().plus(c.mtv.smult(0.51f)));
			}
			float cor = (float) Math.sqrt((float) _restitution * c.other._restitution);
			float m1 = _mass;
			float m2 = c.other._mass;
			Vec2f u1 = _velocity.projectOnto(c.mtv);
			Vec2f u2 = c.other._velocity.projectOnto(c.mtv);
			Vec2f imp = new Vec2f(0,0);
			if(c.other.isStatic()) {
				imp = (u2.minus(u1)).smult(m1*(1+cor));
			}
			else {
				imp = (u2.minus(u1)).smult(((m1*m2*(1+cor)) / (m1+m2)));
			}
			this.applyImpulse(imp);
		}
	}
	
	public float getMass() {
		return _mass;
	}
	
	public Vec2f getCoords() {
		return _shape.getCoords();
	}
	
	public void setBounded(boolean bounded) {
		_bounded = bounded;
	}

	public void setCoords(Vec2f coords) {
		_shape.setCoords(coords);
	}

	public Vec2f getVelocity() {
		return _velocity;
	}

	public void setVelocity(Vec2f _velocity) {
		this._velocity = _velocity;
	}

	public void applyImpulse(Vec2f i) {
		if(!_static) {
			_impulse = _impulse.plus(i);
		}
	}
	
	public void applyForce(Vec2f f) {
		if(!_static) {
			_force = _force.plus(f);
		}
	}
	
	public Shape getShape() {
		return _shape;
	}
	
	public Shape getBoundingBox() {
		return _boundingBox;
	}
	
	public void setBoundingBox(Shape shape) {
		_boundingBox = shape;
	}
	
	public void drawingBound(boolean drawBound) {
		_drawBound = drawBound;
	}
	
	public World getWorld() {
		return _world;
	}
	
	public Vec2f getLastMTV() {
		return _lastMTV;
	}
	
	public void setStatic(boolean status) {
		_static = status;
	}
	
	public boolean isStatic() {
		return _static;
	}
	
	public void outOfBounds() {}
}
