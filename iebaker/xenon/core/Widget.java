package iebaker.xenon.core;

import cs195n.Vec2f;

import iebaker.xenon.slice.*;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics2D;

public class Widget extends SliceElement {
	public String attrWidgetID;
	protected Vec2f mouse_position = new Vec2f(0,0);
	protected boolean mouse_over = false;

	protected Screen my_screen;
	protected Application my_application;

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
	}	

	public Widget(Screen s, String id) {
		attrWidgetID = id;
		my_screen = s;
		my_application = s.getApplication();
	}

	//ON functions.  Should be overridden if needed.  I don't feel like documenting these all :(
	public void onTick(long nanos) {
		mouse_over = checkMouseOver();
	}
	
	public void onMouseClicked(MouseEvent e) {
		if(mouse_over) {
			this.clickBehavior(e, my_application, my_screen);
		}
	}

	public void clickBehavior(MouseEvent e, Application a, Screen s) {
		return;
	}
	
	public void onMouseDragged(MouseEvent e) {
		mouse_position = new Vec2f(e.getX(), e.getY());
	}
	
	public void onMouseMoved(MouseEvent e) {
		mouse_position = new Vec2f(e.getX(), e.getY());
	}

	public void onMouseWheelMoved(MouseWheelEvent e) {}
	public void onDraw(Artist a, Graphics2D g) {}
	public void onKeyTyped(KeyEvent e) {}
	public void onKeyPressed(KeyEvent e) {}
	public void onKeyReleased(KeyEvent e) {}
	public void onMousePressed(MouseEvent e) {}
	public void onMouseReleased(MouseEvent e) {}

	private boolean checkMouseOver() {
		return mouse_position.x >= attrLocation.x && mouse_position.x <= attrLocation.x + attrSize.x
			&& mouse_position.y >= attrLocation.y && mouse_position.y <= attrLocation.y + attrSize.y;
	}

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