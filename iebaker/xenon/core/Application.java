package iebaker.xenon.core;

import cs195n.Vec2f;
import cs195n.Vec2i;
import cs195n.SwingFrontEnd;

import java.util.ArrayList;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.Graphics2D;

/**
 * Application is the main class which any game must extend to use the Helium engine.  An 
 * application owns a list of screens, a ScreenManager object with which to manipulate the screens,
 * and a size parameter which
 * holds the current view size and is used for layout.  Application exists mostly to propagate
 * input and drawing/ticking to its screens.
 */
public class Application extends SwingFrontEnd {

	private java.util.List<Screen> my_screens;
	private ScreenManager my_screen_manager;
	private Vec2i my_size;
	private Artist my_artist = new Artist();

	/* --------------------------------- */
	/* CONSTRUCTORS                      */
	/* --------------------------------- */

	/**
	 * Constructor
	 *
	 * @param title 		The title of the application
	 * @param fullscreen	whether or not the application should be fullscreen
	 */
	public Application(String title, boolean fullscreen) {
		super(title, fullscreen);
		this.setup();
	}

	/**
	 * Constructor
	 *
	 * @param title 		The title of the application
	 * @param fullscreen	whether or not the application should be fullscreen
	 * @param windowSize	The size of the window
	 */
	public Application(String title, boolean fullscreen, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		this.setup();
	}

	/**
	 * Constructor
	 *
	 * @param title 		The title of the application
	 * @param fullscreen	whether or not the application should be fullscreen
	 * @param windowSize	The size of the window
	 * @param closeOp		The operation to perform on closing the window
	 */
	public Application(String title, boolean fullscreen, Vec2i windowSize, int closeOp) {
		super(title, fullscreen, windowSize, closeOp);
		this.setup();
	}

	/**
	 * Sets up instance variables
	 */
	private void setup() {
		my_screens = new ArrayList<Screen>();
		my_screen_manager = new ScreenManager(this, my_screens);
		my_size = this.clientSize;
	}

	/* --------------------------------- */
	/* PROPAGATION METHODS               */
	/* --------------------------------- */

	/**
	 * Propagates ticking to all screens
	 */
	@Override
	public void onTick(long nanos) {
		my_screen_manager.update();
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onTick(nanos);
		}
	}

	/**
	 * Propagates drawing to all screens
	 */
	@Override
	public void onDraw(Graphics2D g) {
		for(Screen s : my_screen_manager.getVisibleScreens()) {
			s.onDraw(my_artist, g);
		}
	}

	/**
	 * Propagates keyTyped events to all screens
	 */
	@Override
	public void onKeyTyped(KeyEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onKeyPressed(e);
		}
	}

	/**
	 * Propagates keyPressed events to all active screens
	 */
	@Override
	public void onKeyPressed(KeyEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onKeyPressed(e);
		}
	}

	/**
	 * Propagates keyReleased events to all active screens
	 */
	@Override
	public void onKeyReleased(KeyEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onKeyReleased(e);
		}
	}

	/**
	 * Propagates mouseClicked events to all active screens
	 */
	@Override
	public void onMouseClicked(MouseEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMouseClicked(e);
		}
	}

	/**
	 * Propagates mousePressed events to all active screens
	 */
	@Override
	public void onMousePressed(MouseEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMousePressed(e);
		}
	}

	/**
	 * Propagates mouseReleased events to all active screens
	 */
	@Override
	public void onMouseReleased(MouseEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMouseReleased(e);
		}
	}

	/**
	 * Propagates mouseDragged events to all active screens
	 */
	@Override
	public void onMouseDragged(MouseEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMouseDragged(e);
		}
	}

	/** 
	 * Propagates mouseMoved events to all active screens
	 */
	@Override
	public void onMouseMoved(MouseEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMouseMoved(e);
		}
	}

	/**
	 * Propagates mouseWheelMoved events to all active screens
	 */
	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		for(Screen s : my_screen_manager.getActiveScreens()) {
			s.onMouseWheelMoved(e);
		}
	}

	/**
	 * Propagates resizing events to all screens, and sets the size field of this appropriately
	 */
	@Override
	public void onResize(Vec2i newSize) {
		my_size = newSize;
		for(Screen s : my_screens) {
			s.onResize(newSize);
		}
	}

	/* --------------------------------- */
	/* ACCESSOR METHODS                  */
	/* --------------------------------- */

	/**
	 * Accessor method for the ScreenManager object associated with this application
	 */
	public ScreenManager getScreenManager() {
		return my_screen_manager;
	}

	/**
	 * Accessor method for the size field of this application
	 */ 
	public Vec2i getSize() {
		return my_size;
	}	
}
