package engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.RoundRectangle2D;

import cs195n.Vec2f;
import engine.Shape;

/**
 * A Shape subclass to draw rounded rectangles onscreen
 * 
 * @author dgattey
 * 
 */
public class UIRoundRect extends Shape {
	
	private static final long	serialVersionUID	= -2940009164281465539L;
	private RoundRectangle2D	rect;
	private float				round;
	
	/**
	 * Creates a UIRoundRect object with coord, width, height (optionally with color, stroke, and custom rounded radius
	 * too)
	 * 
	 * @param coord
	 *            The beginning coordinate
	 * @param endCoord
	 *            The end coordinate
	 * @param round
	 *            The float for the rounded radius
	 * @param c
	 *            The color
	 * @param stk
	 *            The stroke
	 */
	public UIRoundRect(Vec2f coord, Vec2f endCoord, float round, Color c, Stroke stk) {
		this(coord, endCoord, round);
		this.c = c;
		this.stk = stk;
	}
	
	public UIRoundRect(Vec2f coord, Vec2f endCoord, Color c, Stroke stk) {
		this(coord, endCoord, 10);
		this.c = c;
		this.stk = stk;
	}
	
	public UIRoundRect(Vec2f coord, Vec2f endCoord, float round) {
		this.round = round;
		rect = new RoundRectangle2D.Float(coord.x, coord.y, endCoord.x - coord.x, endCoord.y - coord.y, round, round);
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
		drawShape(g);
		g.fill(rect);
	}
	
	/**
	 * Resizes the width, height, and coord
	 * 
	 * @param coord
	 * @param endCoord
	 */
	public void updatePosition(Vec2f coord, Vec2f endCoord) {
		rect.setRoundRect(coord.x, coord.y, endCoord.x - coord.x, endCoord.y - coord.y, this.round, this.round);
	}
	
}
