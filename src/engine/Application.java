package engine;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import cs195n.SwingFrontEnd;
import cs195n.Vec2i;

/**
 * High level class to control game engine flow and usage
 * 
 * @author dgattey
 * 
 */
public class Application extends SwingFrontEnd {
	
	private List<Screen>	screenStack	= new ArrayList<Screen>();
	private Vec2i			currentScreenSize;
	
	/**
	 * Supports creation of an Application with a title for the top of the window and a "starts in fullscreen" variable
	 * 
	 * @param title
	 * @param fullscreen
	 */
	public Application(String title, boolean fullscreen) {
		super(title, fullscreen);
		System.setProperty("sun.java2d.opengl", "true");
		System.out.println("App initialized! Starting game...");
	}
	
	/**
	 * Pushes the given Screen s onto the screenStack, bringing it to the foreground
	 * 
	 * @param s
	 *            The Screen to add
	 */
	public void pushScreen(Screen s) {
		if (getCurrentScreenSize() != null) s.onResize(getCurrentScreenSize());
		screenStack.add(s);
	}
	
	/**
	 * Pops the top Screen off the stack, returning control to the previous Screen
	 */
	public void popScreen() {
		screenStack.remove(screenStack.size() - 1);
		if (getCurrentScreenSize() != null) screenStack.get(screenStack.size() - 1).onResize(getCurrentScreenSize());
	}
	
	/**
	 * The three main methods to control the game, onDraw, onTick, and onResize pass through their calls to the current
	 * screen for it to respond appropriately, though onResize also saves the new size and calls onResize for all
	 * screens in the stack
	 */
	protected void onTick(long nanosSincePreviousTick) {
		screenStack.get(screenStack.size() - 1).onTick(nanosSincePreviousTick);
	}
	
	protected void onDraw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		screenStack.get(screenStack.size() - 1).onDraw(g);
	}
	
	protected void onResize(Vec2i newSize) {
		currentScreenSize = newSize;
		screenStack.get(screenStack.size() - 1).onResize(getCurrentScreenSize());
	}
	
	/**
	 * All onXXX methods simply pass through their call to the current top stack screen
	 */
	protected void onKeyTyped(KeyEvent e) {
		screenStack.get(screenStack.size() - 1).onKeyTyped(e);
	}
	
	protected void onKeyRepeated(KeyEvent e) {
		screenStack.get(screenStack.size() - 1).onKeyRepeated(e);
	}
	
	protected void onKeyPressed(KeyEvent e) {
		screenStack.get(screenStack.size() - 1).onKeyPressed(e);
	}
	
	protected void onKeyReleased(KeyEvent e) {
		screenStack.get(screenStack.size() - 1).onKeyReleased(e);
	}
	
	protected void onMouseClicked(MouseEvent e) {
		screenStack.get(screenStack.size() - 1).onMouseClicked(e);
	}
	
	protected void onMousePressed(MouseEvent e) {
		screenStack.get(screenStack.size() - 1).onMousePressed(e);
	}
	
	protected void onMouseReleased(MouseEvent e) {
		screenStack.get(screenStack.size() - 1).onMouseReleased(e);
	}
	
	protected void onMouseDragged(MouseEvent e) {
		screenStack.get(screenStack.size() - 1).onMouseDragged(e);
	}
	
	protected void onMouseMoved(MouseEvent e) {
		screenStack.get(screenStack.size() - 1).onMouseMoved(e);
	}
	
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		screenStack.get(screenStack.size() - 1).onMouseWheelMoved(e);
	}
	
	public Vec2i getCurrentScreenSize() {
		return currentScreenSize;
	}
	
}
