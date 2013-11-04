package smt3.gameengine.other;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * An interface for objects that take user input.
 * @author Sawyer
 *
 */
public interface Inputable {

	/**
	 * 
	 * @param e
	 */
	abstract void onKeyPressed(KeyEvent e);
	/**
	 * 
	 * @param e
	 */
	abstract void onKeyReleased(KeyEvent e);
	/**
	 * 
	 * @param e
	 */
	abstract void onMousePressed(MouseEvent e);
	/**
	 * 
	 * @param e
	 */
	abstract void onMouseReleased(MouseEvent e);
}
