package smt3.gameengine.other;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Hashtable;

import cs195n.Vec2i;

/**
 * Sprite class. Used to store a single sprite on a spritesheet. Allows for animation.
 * @author Sawyer
 *
 */
public class Sprite {
	private int _w;
	private int _h;
	private Image _src;
	private String _type;
	//Hashtable that hashes mode -> frame -> position on spritesheet
	private Hashtable<String, Hashtable<Integer, Vec2i>> _posHash = new Hashtable<String, Hashtable<Integer, Vec2i>>();;
	//Hashtable that hashes mode -> number of frames
	private Hashtable<String, Integer> _frameHash = new Hashtable<String, Integer>();
	private int _tempX;
	private int _x;
	private int _y;
	private String _tempMode;
	private int _tempframe;
	private boolean _isStatic;

	/**
	 * 
	 * @param tempMode
	 */
	public void setMode(String tempMode) {
		_tempMode = tempMode;
		_posHash.put(tempMode, new Hashtable<Integer, Vec2i>());
	}
	
	/**
	 * 
	 * @param frames
	 */
	public void setFrameHash(int frames) {
		_frameHash.put(_tempMode, frames);
	}
	
	/**
	 * 
	 * @param tempframe
	 */
	public void setFrame(int tempframe) {
		_tempframe = tempframe;
	}
	
	/**
	 * 
	 * @param isStatic
	 */
	public void setStatic(boolean isStatic) {
		_isStatic = isStatic;
	}
	
	/**
	 * 
	 * @return boolean, true if sprite is not animated
	 */
	public boolean isStatic() {
		return _isStatic;
	}
	
	/**
	 * 
	 * @param newX
	 */
	public void setX(int newX) {
		if(_isStatic) {
			_x = newX;
		}
		
		else {
			_tempX = newX;
		}
	}
	
	/**
	 * 
	 * @param newY
	 */
	public void setY(int newY) {
		if(_isStatic) {
			_y = newY;
		}
		
		else {
			_posHash.get(_tempMode).put(_tempframe, new Vec2i(_tempX, newY));
		}
	}
	
	/**
	 * 
	 * @param mode
	 * @param frame
	 * @return Gets the coorinates on a spritesheet, given the mode and frame of that sprite
	 */
	public Vec2i getModeCoords(String mode, String frame) {
		return _posHash.get(mode).get(frame);
	}

	/**
	 * 
	 * @return int width
	 */
	public int getW() {
		return _w;
	}

	/**
	 * 
	 * @param width
	 */
	public void setW(int w) {
		_w = w;
	}

	/**
	 * 
	 * @return int height
	 */
	public int getH() {
		return _h;
	}

	/**
	 * 
	 * @param height
	 */
	public void setH(int h) {
		_h = h;
	}

	/**
	 * 
	 * @return the source image
	 */
	public Image getSrc() {
		return _src;
	}

	public void setSrc(Image src) {
		_src = src;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}
	
	public void draw(Graphics2D g, int x1, int y1, int x2, int y2) {
		g.drawImage(_src, x1, y1, x2, y2, _x, _y, _x + _w, _y + _h, null);
	}
	
	public void draw(Graphics2D g, int x1, int y1, int x2, int y2, String mode, int time) {
		int frame = time % _frameHash.get(mode);
		int sx1 = _posHash.get(mode).get(frame).x;
		int sy1 = _posHash.get(mode).get(frame).y;
		int sx2 = _posHash.get(mode).get(frame).x + _w;
		int sy2 = _posHash.get(mode).get(frame).y + _h;
		g.drawImage(_src, x1, y1, x2, y2, sx1, sy1, sx2, sy2, null);
	}
	
	public void drawStatic(Graphics2D g, int x1, int y1, int x2, int y2, String mode) {
		int sx1 = _posHash.get(mode).get(0).x;
		int sy1 = _posHash.get(mode).get(0).y;
		int sx2 = _posHash.get(mode).get(0).x + _w;
		int sy2 = _posHash.get(mode).get(0).y + _h;
		g.drawImage(_src, x1, y1, x2, y2, sx1, sy1, sx2, sy2, null);
	}
}
