package engine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;

import cs195n.Vec2f;
import engine.Shape;

/**
 * A class to support the creation and drawing of a button object
 * 
 * @author dgattey
 * 
 */
public class UIButton extends Shape {
	
	private Vec2f		coord;
	private Vec2f		endCoord;
	private UIRoundRect	rect;
	private UIText		uiText;
	
	/**
	 * Creates a button with a message, start and end coordinate, background color, and stroke
	 * 
	 * @param msg
	 *            The message to display
	 * @param coord
	 *            The starting coordinate
	 * @param endCoord
	 *            The ending coordinate
	 * @param c
	 *            The color
	 * @param stk
	 *            The stroke
	 */
	public UIButton(String msg, Vec2f coord, Vec2f endCoord, Color c, Color c2, Stroke stk) {
		this.coord = coord;
		this.endCoord = endCoord;
		this.c = c;
		this.stk = stk;
		this.rect = new UIRoundRect(coord, new Vec2f(0, 0), c, stk);
		this.uiText = new UIText(msg, c2, new Vec2f(0, 0), 1);
	}
	
	/**
	 * Draw the shape onto the graphics object by drawing the rect and the uIText
	 */
	public void drawShape(Graphics2D g) {
		super.drawShape(g);
		rect.drawAndFillShape(g);
		uiText.drawShape(g);
	}
	
	/**
	 * Resizes the whole button to the new size
	 * 
	 * @param coord
	 * @param endCoord
	 */
	public void updatePosition(Vec2f coord, Vec2f endCoord) {
		this.coord = coord;
		this.endCoord = endCoord;
		this.rect.updatePosition(coord, endCoord);
		this.uiText.resizeText(new Vec2f((coord.x + coord.x / 6), (coord.y + 5 * (endCoord.y - coord.y) / 8)),
				(endCoord.y - coord.y) / 2f);
	}
	
	/**
	 * Checks if the mouse click was within the button bounds
	 * 
	 * @param e
	 *            A MouseEvent corresponding to the most recent click
	 * @return A boolean representing if the click was within bounds
	 */
	public boolean hitTarget(MouseEvent e) {
		return (e.getX() > coord.x && e.getX() < endCoord.x && e.getY() > coord.y && e.getY() < endCoord.y);
	}
}
