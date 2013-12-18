package iebaker.argon.core;

import cs195n.*;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.RenderingHints;


/**
 * Application is the main class which any game must extend to use the Helium engine.  An 
 * application owns a list of screens, a ScreenManager object with which to manipulate the screens,
 * a data manager which can be used to store application-wide data, and a size parameter which
 * holds the current view size and is used for layout.  Application exists mostly to propagate
 * input and drawing/ticking to its screens.
 */
public class Application extends SwingFrontEnd {

	//Instance variables
	private java.util.List<Screen> screens;
	private ScreenManager scr_manager;
	private Vec2i size;


	/**
	 * Constructor.  Sets up screens, screen management.
	 */
	public Application(String title, boolean fullscreen) {
		super(title, fullscreen);
		setup();
	}


	/**
	 * Constructor again.
	 */
	public Application(String title, boolean fullscreen, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		setup();
	}


	/** 
	 * Constructor one more time.
	 */
	public Application(String title, boolean fullscreen, Vec2i windowSize, int closeOp) {
		super(title, fullscreen, windowSize, closeOp);
		setup();
	}



	/**
	 * Sets up instance variables.
	 */
	private void setup() {
		screens = new ArrayList<Screen>();
		scr_manager = new ScreenManager(this, screens);	
		size = this.clientSize;
	}


	/**
	 * Propagates ticking to all screens
	 */
	@Override
	public void onTick(long nanos) {
		scr_manager.update();
		for(Screen s : screens) {
			s.onTick(nanos);
		}
	}


	/**
	 * Propagates drawing to all visible screens
	 */
	@Override
	public void onDraw(Graphics2D g) {
		//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for(Screen s : scr_manager.getVisibleScreens()) {
			s.onDraw(g);
			//System.out.println(scr_manager.getVisibleScreens());
		}
	}


	/**
	 * Propagates keyTyped events to all active screens
	 */
	@Override
	public void onKeyTyped(KeyEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onKeyTyped(e);
		}
	}


	/**
	 * Propagates keyPressed events to all active screens
	 */
	@Override
	public void onKeyPressed(KeyEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onKeyPressed(e);
		}
	}


	/**
	 * Propagates keyReleased events to all active screens
	 */
	@Override
	public void onKeyReleased(KeyEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onKeyReleased(e);
		}
	}


	/**
	 * Propagates mouseClicked events to all active screens
	 */
	@Override
	public void onMouseClicked(MouseEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMouseClicked(e);
		}
	}


	/**
	 * Propagates mousePressed events to all active screens
	 */
	@Override
	public void onMousePressed(MouseEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMousePressed(e);
		}
	}


	/**
	 * Propagates mouseReleased events to all active screens
	 */
	@Override
	public void onMouseReleased(MouseEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMouseReleased(e);
		}
	}


	/**
	 * Propagates mouseDragged events to all active screens
	 */
	@Override
	public void onMouseDragged(MouseEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMouseDragged(e);
		}
	}


	/** 
	 * Propagates mouseMoved events to all active screens
	 */
	@Override
	public void onMouseMoved(MouseEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMouseMoved(e);
		}
	}


	/**
	 * Propagates mouseWheelMoved events to all active screens
	 */
	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		for(Screen s : scr_manager.getActiveScreens()) {
			s.onMouseWheelMoved(e);
		}
	}


	/**
	 * Propagates resizing events to all screens, and sets the size field of this appropriately
	 */
	@Override
	public void onResize(Vec2i newSize) {
		size = newSize;
		for(Screen s : screens) {
			s.onResize(newSize);
		}
	}


	/**
	 * Accessor method for the ScreenManager object associated with this application
	 */
	public ScreenManager getScreenManager() {
		return scr_manager;
	}





	/**
	 * Accessor method for the size field of this application
	 */ 
	public Vec2i getSize() {
		return size;
	}
}
