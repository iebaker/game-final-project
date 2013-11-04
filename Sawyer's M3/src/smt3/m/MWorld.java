package smt3.m;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import cs195n.Vec2f;
import smt3.gameengine.other.World;
import smt3.gameengine.physics.CollisionInfo;
import smt3.gameengine.physics.MTVHolder;
import smt3.gameengine.physics.Entity;
import smt3.gameengine.physics.Shape;
import smt3.gameengine.ui.Application;
import smt3.m.Player;

public class MWorld extends World {

	private Player _player;
	private Vec2f _gravity = new Vec2f(0,20);
	private Vec2f _lineSource = new Vec2f(0,0);
	private Vec2f _lineEnd = new Vec2f(0,0);
	private long _laserCountdown = 0;
	private ArrayList<Bomb> _bombs = new ArrayList<Bomb>();
	private Vec2f _lastMousePos = new Vec2f(0,0);
	private ArrayList<Explosion> _explosions = new ArrayList<Explosion>();
	
	public MWorld(Application app, Vec2f gameDims) {
		super(new Vec2f(0,0), gameDims, app);		
	}

	@Override
	public void onTick(long nanosSinceLastTick) {
		super.onTick(nanosSinceLastTick);
		for(int i=_explosions.size()-1; i>=0; i--) {
			_explosions.get(i).onTick(nanosSinceLastTick);
		}
		
		for(Entity e : this.getEntities()) {
			e.applyForce(_gravity.smult(e.getMass()));
		}
		
		//check for bomb collisions
		for(int b=_bombs.size()-1; b>=0; b--) {
			_bombs.get(b).applyForce(_gravity.smult(_bombs.get(b).getMass()));
			_bombs.get(b).onTick(nanosSinceLastTick);
			ArrayList<Entity> ents = new ArrayList<Entity>(this.getEntities());
			ents.remove(_bombs.get(b));
			ents.remove(_player);
			for(Entity e : ents) {
				if(_bombs.get(b).getShape().collide(e.getShape()).mtv != null) {
					_bombs.get(b).explode();
					break;
				}
			}
		}
		
		for(int i=this.getEntities().size()-1; i>=0; i--) {
			Entity e1 = this.getEntities().get(i);
			for(int j=i-1; j>=0; j--) {
				Entity e2 = this.getEntities().get(j);
				MTVHolder coll = e1.getShape().collide(e2.getShape());
				if(coll.mtv != null) {
					e1.onCollide(new CollisionInfo(coll.mtv, e2));
					e2.onCollide(new CollisionInfo(coll.mtv.smult(-1), e1));
				}
			}
		}
		if(_laserCountdown > 0) {
			_laserCountdown -= nanosSinceLastTick;
		}
		Shape p = _player.getShape();
		Vec2f nearPoint = p.getCoords();
		Vec2f farPoint = p.getCoords().plus(p.getDims());
		Vec2f screenSize = new Vec2f(this.getApp().getScreenSize().x, this.getApp().getScreenSize().y);
		Vec2f nearScreenInGame = this.getVP().convertToGame(new Vec2f(0,0)).plus(screenSize.x*2/5,screenSize.y*2/5);
		Vec2f farScreenInGame = this.getVP().convertToGame(screenSize).minus(screenSize.x*2/5,screenSize.y*2/5);
		if(nearPoint.x < nearScreenInGame.x && nearPoint.x > screenSize.x*2/5) {
			this.setGameCoords(this.getGameCoords().plus(nearScreenInGame.x - nearPoint.x, 0));
		}
		if(nearPoint.y < nearScreenInGame.y && nearPoint.y > screenSize.y*2/5) {
			this.setGameCoords(this.getGameCoords().plus(0, nearScreenInGame.y - nearPoint.y));
		}
		
		if(farPoint.x > farScreenInGame.x && farPoint.x < this.getGameDims().x - screenSize.x*2/5) {
			this.setGameCoords(this.getGameCoords().plus(farScreenInGame.x - farPoint.x, 0));
		}
		if(farPoint.y > farScreenInGame.y && farPoint.y < this.getGameDims().y - screenSize.y*2/5) {
			this.setGameCoords(this.getGameCoords().plus(0, farScreenInGame.y - farPoint.y));
		}
	}
	
	@Override
	public void onDraw(Graphics2D g) {
		super.onDraw(g);
		if(_laserCountdown > 0) {
			g.setColor(new Color(255,0,0));
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawLine((int)_lineSource.x, (int)_lineSource.y, (int)_lineEnd.x, (int)_lineEnd.y);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		for(Explosion e : _explosions) {
			e.draw(g);
		}
		for(Bomb b : _bombs) {
			b.getShape().draw(g);
		}
	}
	
	@Override
	public void loseGame() {
		
	}

	@Override
	public void winGame() {
		
	}
	
	public void addBomb(Bomb b) {
		_bombs.add(b);
	}
	
	public void removeBomb(Bomb b) {
		_bombs.remove(b);
	}
	
	public void removeExplosion(Explosion e) {
		_explosions.remove(e);
	}
	
	public void makeExplosion(Vec2f pos, float power, float radius, Bomb sourceBomb) {
		
		Entity target = null;
		Vec2f source = pos;
		Vec2f dir = null;
		for(float i=0; i<Math.PI*2; i+= Math.PI/100) {
			Float minRay = radius;
			dir = new Vec2f(Math.cos(i), Math.sin(i));
			for(Entity ent : this.getEntities()) {
				Float ray = ent.getShape().raycast(source, dir);
				if(ray != null && ray < minRay) {
					minRay = ray;
					target = ent;
				}
			}
			if(target != null) {
				target.applyImpulse(dir.smult(power));
			}
		}
		_explosions.add(new Explosion(pos.minus(radius, radius), radius*2, 50000000, this));
	}
	
	public void setGravity(Vec2f g) {
		_gravity = g;
	}
	
	@Override
	public void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_R) {
			this.flipGravity();
		}
		_player.onKeyPressed(e);
	}
	@Override
	public void onKeyReleased(KeyEvent e) {
		_player.onKeyReleased(e);
	}
	@Override
	public void onMousePressed(MouseEvent e) {
		Float minRay = Float.POSITIVE_INFINITY;
		Entity target = null;
		Vec2f source = _player.getShape().getCenter();
		Vec2f dir = this.getVP().convertToGame(new Vec2f(e.getX(), e.getY())).minus(source).normalized();
		for(Entity ent : super.getEntities()) {
			if(ent != _player) {
				Float ray = ent.getShape().raycast(source, dir);
				if(ray != null && ray < minRay) {
					minRay = ray;
					target = ent;
				}
			}
		}
		if(target != null) {
			_lineSource = source;
			_lineEnd = _lineSource.plus(dir.smult(minRay));
			_laserCountdown = 50000000;
			target.applyImpulse(dir.smult(100000));
		}	
	}
	
	@Override 
	public void onMouseDragged(MouseEvent e) {
	}
	
	@Override
	public void onMouseMoved(MouseEvent e) {
		_lastMousePos = this.getVP().convertToGame(new Vec2f(e.getX(), e.getY()));
	}
	
	@Override
	public void onMouseReleased(MouseEvent e) {
		super.onMouseReleased(e);
	}
	
	public Vec2f getLastMousePos() {
		return _lastMousePos;
	}
	
	public void flipGravity() {
		_gravity = _gravity.smult(-1);
	}

	@Override
	public Player addPlayer(Shape s, float density, float restitution) {
		_player = new Player(s, this, density, restitution);
		return _player;
	}
}
