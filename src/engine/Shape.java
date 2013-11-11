package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 * An abstract Shape class designed to be subclassed to draw shapes onscreen
 * 
 * @author dgattey
 * 
 */
public abstract class Shape {
	
	protected Color		c;
	protected Stroke	stk;
	
	/**
	 * Sets the color and sets the stroke
	 * 
	 * @param g
	 */
	protected void drawShape(Graphics2D g) {
		if (c != null) g.setColor(c);
		if (stk != null) g.setStroke(stk);
	}
	
	/**
	 * Public setter for the color
	 * 
	 * @param c
	 */
	public void changeColor(Color c) {
		this.c = c;
	}
}
