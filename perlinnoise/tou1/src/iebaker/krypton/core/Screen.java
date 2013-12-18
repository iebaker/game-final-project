package iebaker.krypton.core;

import cs195n.*;
import iebaker.krypton.slice.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics2D;
import java.awt.event.*;

/**
 * Screen is a class which comprises an activity with which the
 * player can be interacting.  The main application owns a stack of
 * these screens, which are rendered in the order they are returned
 * by the iterator of stack.  All mouse and key input is propagated 
 * to each screen (in that order) by the main application as well.
 * A screen owns a list of Widgets, as well as a dictionary
 * indexing each widget by its WidgetID.  Screens have a Node
 * object which is used to position all the child widgets, and a 
 * reference to the parent Application.  A screen can be active or
 * inactive (inactive screens do not receive input), visible or 
 * invisible (invisible screens do not receive onDraw() calls).
 */
public class Screen {

	//PRIVATE
	private java.util.List<Widget> widgets = new ArrayList<Widget>();
	private java.util.Map<String, Widget> widgets_dict = new HashMap<String, Widget>();
	private boolean active;
	private boolean visible;

	//PROTECTED
	protected Node root_node;
	protected Application parent_application;

	//PUBLIC
	public String attrScreenID;


	/**
	 * Constructor. Implementations should implicitly or explicitly setup the layout
	 *
	 * @param a 	the parent application
	 * @param id 	the ScreenID of this screen
	 */
	public Screen(Application a, String id) {
		parent_application = a;
		attrScreenID = id;
		active = true;
		visible = true;
	}

	/**
	 * Adds a Widget to the dictionary of widgets,
	 * indexed by its WidgetID
	 *
	 * @param se 	the Widget to be added
	 */
	public void registerWidgets(Widget... elems) {
		for(Widget se : elems) {
			widgets.add(se);
			widgets_dict.put(se.attrWidgetID, se);
		}
	}


	/**
	 * Retrieval method for a Widget.
	 *
	 * @param id 	the WidgetID of the Widget to be retrieved
	 * @return 		the Widget indexed by the id passed
	 */
	public Widget getWidgetByID(String id) {
		return widgets_dict.get(id);
	}


	/**
	 * Propagates ticks to all Widgets
	 */ 
	public void onTick(long nanos) {
		for(Widget se : widgets) {
			se.onTick(nanos);
		}
	}


	/**
	 * Propagates drawing to all Widgets
	 */
	public void onDraw(Graphics2D g) {
		for(Widget se : widgets) {
			se.onDraw(g);
		}
	}


	/**
	 * Propagates keyTyped events to all Widgets
	 */
	public void onKeyTyped(KeyEvent e) {
		for(Widget se : widgets) {
			se.onKeyTyped(e);
		}
	}


	/**
	 * Propagates keyPressed events to all Widgets
	 */
	public void onKeyPressed(KeyEvent e) {
		for(Widget se : widgets) {
			se.onKeyPressed(e);
		}
	}


	/**
	 * Propagates keyReleased events to all Widgets
	 */
	public void onKeyReleased(KeyEvent e) {
		for(Widget se : widgets) {
			se.onKeyReleased(e);
		}
	}


	/**
	 * Propagates mouseClicked events to all Widgets
	 */
	public void onMouseClicked(MouseEvent e) {
		for(Widget se : widgets) {
			se.onMouseClicked(e);
		}
	}


	/**
	 * Propagates mousePressed events to all Widgets
	 */
	public void onMousePressed(MouseEvent e) {
		for(Widget se : widgets) {
			se.onMousePressed(e);
		}
	}


	/**
	 * Propagates mouseReleased events to all Widgets
	 */
	public void onMouseReleased(MouseEvent e) {
		for(Widget se : widgets) {
			se.onMouseReleased(e);
		}
	}


	/**
	 * Propagates mouseDragged events to all Widgets
	 */
	public void onMouseDragged(MouseEvent e) {
		for(Widget se : widgets) {
			se.onMouseDragged(e);
		}
	}


	/**
	 * Propagates mouseMoved events to all Widgets
	 */
	public void onMouseMoved(MouseEvent e) {
		for(Widget se : widgets) {
			se.onMouseMoved(e);
		}
	}


	/**
	 * Propagates mouseWheelMoved events to all Widgets
	 */
	public void onMouseWheelMoved(MouseWheelEvent e) {
		for(Widget se : widgets) {
			se.onMouseWheelMoved(e);
		}
	}


	/**
	 * Rebuilds the Node on resizing
	 */
	public void onResize(Vec2i newSize) {
		root_node.build(newSize);
	}


	/**
	 * Accessor method for the visible field
	 *
	 * @return 		true, if the screen is visible.  false, otherwise
	 */
	public boolean isVisible() {
		return visible;
	}


	/**
	 * Setter method for visibility
	 *
	 * @param v 	Desired visibility
	 */
	public void setVisible(boolean v) {
		visible = v;
	}


	/**
	 * Accessor method for the active field
	 *
	 * @return 		true, if the screen is active.  false, otherwise
	 */ 
	public boolean isActive() {
		return active;
	}


	/**
	 * Setter method for activity
	 * 
	 * @param a 	Desired activity
	 */
	public void setActive(boolean a) {
		active = a;
	}


	/**
	 * Accessor method for the layout management object.  Useful
	 * because Widgets may want to manipulate the layout.
	 */
	public Node getLayout() {
		return root_node;
	}


	/**
	 * Sets the Layout to the layout passed as ln.
	 */
	public void setLayout(Node ln) {
		root_node = ln;
	}
}

