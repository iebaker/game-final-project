package smt3.gameengine.other;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import cs195n.Vec2f;
import smt3.gameengine.physics.Drawable;

public class Viewport {

	private Vec2f _screenCoords;
	private Vec2f _screenDims;
	private World _world;
	
	public Viewport(Vec2f screenCoords, Vec2f screenDims, World world) {
		_screenCoords = screenCoords;
		_screenDims = screenDims;
		_world = world;
	}
	
	public void draw(Graphics2D g) {
		g.clipRect((int) _screenCoords.x, (int) _screenCoords.y, (int) _screenDims.x, (int) _screenDims.y);
		AffineTransform saveAT = g.getTransform();
		g.translate(_world.getGameCoords().x, _world.getGameCoords().y);
		
		_world.onDraw(g);
		
		g.setTransform(saveAT);
	}
	
	public void draw(Graphics2D g, Drawable d) {
		g.clipRect((int) _screenCoords.x, (int) _screenCoords.y, (int) _screenDims.x, (int) _screenDims.y);
		AffineTransform saveAT = g.getTransform();
		g.translate(_world.getGameCoords().x, _world.getGameCoords().y);
		
		d.draw(g);
		
		g.setTransform(saveAT);
	}
	
	public Vec2f convertToScreen(Vec2f gamePoint) {	
		float newX = (gamePoint.x + _world.getGameCoords().x) - _screenCoords.x;
		float newY = (gamePoint.y + _world.getGameCoords().y) - _screenCoords.y;
		return new Vec2f((int) newX, (int) newY);
	}
	
	public Vec2f convertToGame(Vec2f screenPoint) {
		float newX = (screenPoint.x + _screenCoords.x) - _world.getGameCoords().x;
		float newY = (screenPoint.y + _screenCoords.y) - _world.getGameCoords().y;
		return new Vec2f((int) newX, (int) newY);
	}

	public Vec2f getScreenCoords() {
		return _screenCoords;
	}

	public void setScreenCoords(Vec2f _screenCoords) {
		this._screenCoords = _screenCoords;
	}

	public Vec2f getScreenDims() {
		return _screenDims;
	}

	public void setScreenDims(Vec2f _screenDims) {
		this._screenDims = _screenDims;
	}
}

