package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.Serializable;

/**
 * An abstract Shape class designed to be subclassed to draw shapes onscreen
 * 
 * @author dgattey
 * 
 */
public abstract class Shape implements Serializable {
	
	private static final long	serialVersionUID	= 2603943546880126544L;
	protected Color				c;
	protected transient Stroke	stk;
	
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
	public void setColor(Color c) {
		this.c = c;
	}
}
