package engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import cs195n.Vec2i;

/**
 * Abstract class representing a screen to be overriden in subclasses Has a reference to the parent Application and
 * requires onTick, onDraw, and onResize to be implemented in subclasses. Passes through onXXX events
 * 
 * @author dgattey
 * 
 */
public abstract class Screen {
	
	public Application	a;
	
	/**
	 * Initializes a Screen with a reference to its parent Application
	 * 
	 * @param a
	 */
	public Screen(Application a) {
		this.a = a;
	}
	
	/**
	 * Methods to implement in child classes
	 */
	protected abstract void onTick(long nanosSincePreviousTick);
	
	protected abstract void onDraw(Graphics2D g);
	
	protected abstract void onResize(Vec2i newSize);
	
	/**
	 * Methods to use if needed in child classes
	 */
	protected void onKeyTyped(KeyEvent e) {}
	
	protected void onKeyPressed(KeyEvent e) {}
	
	protected void onKeyReleased(KeyEvent e) {}
	
	protected void onMouseClicked(MouseEvent e) {}
	
	protected void onMousePressed(MouseEvent e) {}
	
	protected void onMouseReleased(MouseEvent e) {}
	
	protected void onMouseDragged(MouseEvent e) {}
	
	protected void onMouseMoved(MouseEvent e) {}
	
	protected void onMouseWheelMoved(MouseWheelEvent e) {}
	
	protected void onKeyRepeated(KeyEvent e) {}
}
