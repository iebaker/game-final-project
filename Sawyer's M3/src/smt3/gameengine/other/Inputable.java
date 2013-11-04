package smt3.gameengine.other;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Inputable {

	abstract void onKeyPressed(KeyEvent e);
	abstract void onKeyReleased(KeyEvent e);
	abstract void onMousePressed(MouseEvent e);
	abstract void onMouseReleased(MouseEvent e);
}
