package iebaker.xenon.core;

import cs195n.Vec2f;
import cs195n.Vec2i;

import iebaker.xenon.slice.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Screen is a class which comprises an activity with which the player can be interaction.  The main application owns a stack of 
 * screens which are rendered in the order they are returned by the iterator of the stack.  All mouse and key input is propagated
 * to each screen (in that order) by the main application as well.  A screen owns a list of widgets, as well as a dictionary indexing
 * each widget by its WidgetID. Screens have a Node object which is used to position all the child widgets, and a reference to the
 * parent Application.  A screen can be active or inactive (inactive screens do not receive input), visible or invisible (invisible
 * screens do not receive onDraw calls).
 */
public class Screen {
	private java.util.List<Widget> my_widgets = new ArrayList<Widget>();
	private java.util.Map<String, Widget> my_widgets_dict = new HashMap<String, Widget>();
	private boolean active;
	private boolean visible;

	protected Node root_node;
	protected Application my_app;

	public String attrScreenID;

	/**
	 * Constructor. Implementations should implicitly or explicitly setup the layout
	 *
	 * @param a 	the parent application
	 * @param id 	the ScreenID of this screen
	 */
	public Screen(Application a, String id) {
		my_app = a;
		attrScreenID = id;
		active = true;
		visible = true;
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

	public Application getApplication() {
		return my_app;
	}

	/**
	 * Adds a Widget to the dictionary of widgets,
	 * indexed by its WidgetID
	 *
	 * @param ws 	the Widget to be added
	 */
	public void registerWidgets(Widget... ws) {
		for(Widget w : ws) {
			my_widgets.add(w);
			my_widgets_dict.put(w.attrWidgetID, w);
		}
	}

	/**
	 * Retrieval method for a Widget.
	 *
	 * @param id 	the WidgetID of the Widget to be retrieved
	 * @return 		the Widget indexed by the id passed
	 */
	public Widget getWidget(String id) {
		return my_widgets_dict.get(id);
	}

	/* --------------------------------- */
	/* PROPAGATION METHODS               */
	/* --------------------------------- */

	/**
	 * Propagates ticks to all Widgets
	 */ 
	public void onTick(long nanos) {
		for(Widget w : my_widgets) {
			w.onTick(nanos);
		}
	}

	/**
	 * Propagates drawing to all Widgets
	 */
	public void onDraw(Artist a, Graphics2D g) {
		for(Widget w : my_widgets) {
			w.onDraw(a, g);
		}
	}

	/**
	 * Propagates keyTyped events to all Widgets
	 */
	public void onKeyTyped(KeyEvent e) {
		for(Widget w : my_widgets) {
			w.onKeyTyped(e);
		}
	}

	/**
	 * Propagates keyPressed events to all Widgets
	 */
	public void onKeyPressed(KeyEvent e) {
		for(Widget w : my_widgets) {
			w.onKeyPressed(e);
		}
	}

	/**
	 * Propagates keyReleased events to all Widgets
	 */
	public void onKeyReleased(KeyEvent e) {
		for(Widget w : my_widgets) {
			w.onKeyReleased(e);
		}
	}

	/**
	 * Propagates mouseClicked events to all Widgets
	 */
	public void onMouseClicked(MouseEvent e) {
		for(Widget w : my_widgets) {
			w.onMouseClicked(e);
		}
	}

	/**
	 * Propagates mousePressed events to all Widgets
	 */
	public void onMousePressed(MouseEvent e) {
		for(Widget w : my_widgets) {
			w.onMousePressed(e);
		}
	}

	/**
	 * Propagates mouseReleased events to all Widgets
	 */
	public void onMouseReleased(MouseEvent e) {
		for(Widget w : my_widgets) {
			w.onMouseReleased(e);
		}
	}

	/**
	 * Propagates mouseDragged events to all Widgets
	 */
	public void onMouseDragged(MouseEvent e) {
		for(Widget w : my_widgets) {
			w.onMouseDragged(e);
		}
	}

	/**
	 * Propagates mouseMoved events to all Widgets
	 */
	public void onMouseMoved(MouseEvent e) {
		for(Widget w : my_widgets) {
			w.onMouseMoved(e);
		}
	}

	/**
	 * Propagates mouseWheelMoved events to all Widgets
	 */
	public void onMouseWheelMoved(MouseWheelEvent e) {
		for(Widget w : my_widgets) {
			w.onMouseWheelMoved(e);
		}
	}

	/**
	 * Rebuilds the Node on resizing
	 */
	public void onResize(Vec2i newSize) {
		root_node.build(newSize);
	}
}