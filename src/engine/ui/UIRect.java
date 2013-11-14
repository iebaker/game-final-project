package engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;
import engine.Shape;

/**
 * A Shape subclass to draw rectangles onscreen
 * 
 * @author dgattey
 * 
 */
public class UIRect extends Shape {
	
	private static final long	serialVersionUID	= 4337470195115901749L;
	private Rectangle2D			rect;
	
	/**
	 * Creates a UIRect object with dim, width, height, color, and stroke
	 * 
	 * @param coord
	 * @param endCoord
	 * @param c
	 * @param stk
	 */
	public UIRect(Vec2f coord, Vec2f endCoord, Color c, Stroke stk) {
		this(coord, endCoord);
		this.c = c;
		this.stk = stk;
	}
	
	/**
	 * Creates a UIRect object with dim, width, and height
	 * 
	 * @param sdim
	 * @param dim
	 */
	public UIRect(Vec2f sdim, Vec2f dim) {
		rect = new Rectangle2D.Float(sdim.x, sdim.y, dim.x - sdim.x, dim.y - sdim.y);
	}
	
	/**
	 * Public getter for width
	 * 
	 * @return the width
	 */
	public double getW() {
		return rect.getWidth();
	}
	
	/**
	 * Public getter for height
	 * 
	 * @return the height
	 */
	public double getH() {
		return rect.getHeight();
	}
	
	/**
	 * Draws a rectangle using Rectangle2D into the screen
	 */
	public void drawShape(Graphics2D g) {
		super.drawShape(g);
		g.draw(rect);
	}
	
	/**
	 * Draws a rectangle using Rectangle2D into the screen and fills it
	 */
	public void drawAndFillShape(Graphics2D g) {
		super.drawShape(g);
		g.fill(rect);
	}
	
	/**
	 * Resizes the width, height, and coord
	 * 
	 * @param sdim
	 * @param dim
	 */
	public void updatePosition(Vec2f sdim, Vec2f dim) {
		rect.setRect(sdim.x, sdim.y, dim.x - sdim.x, dim.y - sdim.y);
	}
	
}
