package engine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import cs195n.Vec2f;
import engine.Shape;

/**
 * A Shape subclass supporting creating and drawing a text string to screen
 * 
 * @author dgattey
 * 
 */
public class UIText extends Shape {
	
	private static final long	serialVersionUID	= -8030025571448950556L;
	private String				s;
	private Font				f;
	private Vec2f				coord;
	private float				preferredHeight;
	private boolean				visible				= true;
	private float				currentWidth;
	
	/**
	 * Creates a new text shape with the desired text, coordinates, and preferred width
	 * 
	 * @param text
	 * @param coord
	 * @param w
	 */
	public UIText(String text, Color c, Vec2f coord, float w) {
		s = text;
		this.coord = coord;
		preferredHeight = w;
		this.c = c;
		f = new Font("Arial", Font.PLAIN, 1);
	}
	
	/**
	 * Draws the shape to the screen by figuring out the preferred text size that will fit in the height of that space
	 */
	@Override
	public void drawShape(Graphics2D g) {
		if(visible) {
			super.drawShape(g);
			Font tempF = g.getFont();
			
			float height = g.getFontMetrics(tempF).getHeight();
			float newFSize = (tempF.getSize() * (preferredHeight / height));
			if(!s.equals("")) f = tempF.deriveFont(newFSize);
			g.setFont(f);
			tempF = g.getFont();
			currentWidth = g.getFontMetrics(tempF).getStringBounds(s, g).getBounds().width;
			g.drawString(s, coord.x, coord.y);
		}
	}
	
	/**
	 * Updates the current text
	 * 
	 * @param text
	 */
	public void updateText(String text) {
		s = text;
	}
	
	/**
	 * Resizes the text by setting a new coord and preferred width
	 * 
	 * @param dim
	 * @param preferredHeight
	 */
	public void resizeText(Vec2f dim, float preferredHeight) {
		coord = dim;
		this.preferredHeight = preferredHeight;
	}
	
	/**
	 * Updates the position of the text given the standard resizer - preferred height is end.y
	 */
	public void updatePosition(Vec2f start, Vec2f end) {
		resizeText(start, end.y);
	}
	
	/**
	 * Tells whether the UIText is empty or not
	 * 
	 * @return if the string drawn by the UIText is empty
	 */
	public boolean isEmpty() {
		return (s == null) || (s.length() == 0);
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public float getWidth() {
		return currentWidth;
	}
}
