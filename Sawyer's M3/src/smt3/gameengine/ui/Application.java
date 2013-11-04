package smt3.gameengine.ui;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Hashtable;
import java.util.Stack;

import cs195n.SwingFrontEnd;
import cs195n.Vec2i;
import smt3.gameengine.other.Sprite;

public class Application extends SwingFrontEnd {

	Stack<Screen> _screenStack;
	Vec2i _screenSize;
	Hashtable<String, Sprite> _ht;
	
	public Application(String title, boolean fullscreen) {
		this(title, fullscreen, DEFAULT_WINDOW_SIZE);
	}
	
	public Application(String title, boolean fullscreen, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		_screenStack = new Stack<Screen>();
		_screenSize = windowSize;
	}
	
	public Application(String title, boolean fullscreen, Hashtable<String, Sprite> ht) {
		super(title, fullscreen);
		// TODO Auto-generated constructor stub
		_ht = ht;
		_screenStack = new Stack<Screen>();
		_screenSize = DEFAULT_WINDOW_SIZE;
	}

	public Application(String title, boolean fullscreen, Hashtable<String, Sprite> ht, Vec2i windowSize) {
		super(title, fullscreen, windowSize);
		// TODO Auto-generated constructor stub
		_ht = ht;
		_screenSize = windowSize;
		_screenStack = new Stack<Screen>();
	}

	public Application(String title, boolean fullscreen, Hashtable<String, Sprite> ht, Vec2i windowSize,
			int closeOp) {
		super(title, fullscreen, windowSize, closeOp);
		// TODO Auto-generated constructor stub
		_ht = ht;
		_screenSize = windowSize;
		_screenStack = new Stack<Screen>();
	}
	
	public void addScreen(Screen newScreen) {
		_screenStack.add(newScreen);
		_screenStack.peek().setApplication(this);
		_screenStack.peek().onResize();
	}
	
	public void removeAndAdd(Screen newScreen) {
		this.removeScreen();
		_screenStack.add(newScreen);
		_screenStack.peek().setApplication(this);
		_screenStack.peek().onResize();
	}
	
	public Hashtable<String, Sprite> getHash() {
		return _ht;
	}
	
	public Screen removeScreen() {
		Screen toReturn = _screenStack.pop();
		if(!_screenStack.isEmpty()) {
		//Make sure the next screen knows the correct screen size
			_screenStack.peek().onResize();
		}
		return toReturn;
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		//sets the minimum framerate that ticks will follow
		if(nanosSincePreviousTick >= 5000000) {
			onTick(nanosSincePreviousTick-5000000);
			nanosSincePreviousTick = 5000000;
		}
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onTick(nanosSincePreviousTick);
		}
	}

	@Override
	protected void onDraw(Graphics2D g) {
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onDraw(g);
		}
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onKeyTyped(e);
		}
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onKeyPressed(e);
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onKeyReleased(e);
		}
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMouseClicked(e);
		}
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMousePressed(e);
		}
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMouseReleased(e);
		}
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMouseDragged(e);
		}
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMouseMoved(e);
		}
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onMouseWheelMoved(e);
		}
	}

	@Override
	protected void onResize(Vec2i newSize) {
		// TODO Auto-generated method stub
		_screenSize = newSize;
		if(!_screenStack.isEmpty()) {
			_screenStack.peek().onResize();
		}
	}
	
	public Vec2i getScreenSize() {
		return _screenSize;
	}
}
