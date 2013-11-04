//g.draw(new Rectangle2D.Float(0,0,20,20));

package smt3.gameengine.ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public abstract class Screen {

	Application _app;
	
	public Screen(Application app) {
		// TODO Auto-generated constructor stub
		_app = app;
	}
	
	protected abstract void onTick(long nanosSincePreviousTick);
	
	protected abstract void onDraw(Graphics2D g);

	protected abstract void onKeyTyped(KeyEvent e);

	protected abstract void onKeyPressed(KeyEvent e);

	protected abstract void onKeyReleased(KeyEvent e);

	protected abstract void onMouseClicked(MouseEvent e);

	protected abstract void onMousePressed(MouseEvent e);

	protected abstract void onMouseReleased(MouseEvent e);

	protected abstract void onMouseDragged(MouseEvent e);

	protected abstract void onMouseMoved(MouseEvent e);

	protected abstract void onMouseWheelMoved(MouseWheelEvent e);
		
	//protected abstract void setApplication(Application app);
	
	protected abstract void onResize();
	
	public void setApplication(Application app) {
		_app = app;
	}
	
	public Application getApplication() {
		return _app;
	}
}
