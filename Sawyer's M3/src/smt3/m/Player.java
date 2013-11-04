package smt3.m;

import java.awt.event.KeyEvent;

import cs195n.Vec2f;
import smt3.gameengine.physics.*;

public class Player extends Entity {

	private float _goalVelocity = 300;
	private boolean _moveLeft = false;
	private boolean _moveRight = false;
	private MWorld _mw;
	private boolean _extraJump = true;
	
	public Player(Shape s, MWorld world, Float mass, float restitution) {
		super(s, world, mass, restitution);
		_mw = world;
	}
	
	@Override
	public void onTick(long nanosSinceLastTick) {
		if(this.getLastMTV().y < 0) {
			_extraJump = true;
		}
		if(_moveLeft) {
			this.applyForce(new Vec2f(-_goalVelocity + this.getVelocity().x, 0).smult(50));
		}
		if(_moveRight) {
			this.applyForce(new Vec2f(_goalVelocity - this.getVelocity().x, 0).smult(50));
		}
		super.onTick(nanosSinceLastTick);
	}
	
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A : _moveLeft = true;
			break;
		case KeyEvent.VK_D: _moveRight = true;
			break;
		case KeyEvent.VK_SPACE: 
			if(super.getLastMTV()!= null) {
				if(this.getLastMTV().y < 0) {
					this.applyImpulse(this.getLastMTV().normalized().smult(100000));
				}
				//allows double-jumping
				else if(_extraJump) {
					this.applyImpulse(new Vec2f(0, -75000));
					_extraJump = false;
				}
			}
			break;
		case KeyEvent.VK_SHIFT:
			Vec2f dir = _mw.getLastMousePos().minus(this.getShape().getCenter()).normalized();
			Bomb b = new Bomb(this.getShape().getCenter().minus(5,5), 10, _mw, dir.smult(100));
			b.setCoords(b.getCoords().plus(dir.smult(this.getShape().getDims().x / 1.9f)));
			_mw.addBomb(b);
		default:
			break;
		}
	}

	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A : _moveLeft = false;
			break;
		case KeyEvent.VK_D: _moveRight = false;
			break;
		default:
			break;
		}
	}
}
