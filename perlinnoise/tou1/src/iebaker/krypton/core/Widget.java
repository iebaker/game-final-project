package iebaker.krypton.core;

import iebaker.krypton.slice.*;
import cs195n.*;
import java.awt.event.*;
import java.awt.Graphics2D;

/**
 * Widget is a class representing an on-screen element such as a menu, or a button, or a title.  The Widget class
 * extends SliceElement, and so Widgets can be attached directly to slice.Node objects in order to position them using
 * Slice.  Widgets should respond to input events, and can draw themselves to the screen using their instance of
 * Artist and the Graphics2D object passed in onDraw() calls.  Widgets can access their containing screen and parent 
 * application.
 */
public class Widget extends SliceElement {

	/**
	 * The String identification of this widget, used to index it in the parent Application object
	 */
	public String attrWidgetID;

	/**
	 * The containing screen of this Widget object
	 */
	protected Screen my_screen;

	/**
	 * The parent Application of this Widget object
	 */
	protected Application my_application;

	/**
	 * The artist object used for drawing
	 */
    protected Artist a;	

    /**
     * Constructor.
	 *
	 * @param a 	The parent application
	 * @param s 	The containing screen
	 * @param id 	The ID of this Widget
     */
	public Widget(Application a, Screen s, String id) {
		attrWidgetID = id;
		my_screen = s;
		my_application = a;
		this.a = new Artist();
	}

	//ON functions.  Should be overridden if needed.  I don't feel like documenting these all :(
	public void onTick(long nanos) {}
	public void onDraw(Graphics2D g) {}
	public void onKeyTyped(KeyEvent e) {}
	public void onKeyPressed(KeyEvent e) {}
	public void onKeyReleased(KeyEvent e) {}
	public void onMouseClicked(MouseEvent e) {}
	public void onMousePressed(MouseEvent e) {}
	public void onMouseReleased(MouseEvent e) {}
	public void onMouseDragged(MouseEvent e) {}
	public void onMouseMoved(MouseEvent e) {}
	public void onMouseWheelMoved(MouseWheelEvent e) {}

	/**
	 * Utility method which returns the vertical center of this Widget
	 *
	 * @return 		A float which is halfway along the vertical axis 
	 */
	protected float getVCenter() {
		return (2 * attrLocation.y + attrSize.y)/2;
	}

	/**
	 * Utility method which returns the horizontal center of this widget
	 *
	 * @return 		A float which is halfway along the horizontal axis
	 */
	protected float getHCenter() {
		return (2 * attrLocation.x + attrSize.x)/2;
	}
}


