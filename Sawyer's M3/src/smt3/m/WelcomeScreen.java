package smt3.m;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import smt3.collisiontest.CollisionTest;
import smt3.gameengine.ui.Application;
import smt3.gameengine.ui.Button;
import smt3.gameengine.ui.Screen;

public class WelcomeScreen extends Screen {

	private Button _easyButton;
	private Button _hardButton;
	private Button _insaneButton;
	
	public WelcomeScreen(Application app) {
		super(app);
		_easyButton = new StartButton(this, new Color(0,0,0), new Color(100, 100, 100), new Color(0,0,0), "Easy", app.getScreenSize().x / 10, app.getScreenSize().y / 10, app.getScreenSize().x * 4/5, app.getScreenSize().y * 1/5, 500);
		_hardButton = new StartButton(this, new Color(0,0,0), new Color(100, 100, 100), new Color(0,0,0), "Hard", app.getScreenSize().x / 10, app.getScreenSize().y * 4/10, app.getScreenSize().x * 4/5, app.getScreenSize().y * 1/5, 100);
		_insaneButton = new StartButton(this, new Color(0,0,0), new Color(100, 100, 100), new Color(0,0,0), "Insane", app.getScreenSize().x / 10, app.getScreenSize().y * 7/10, app.getScreenSize().x * 4/5, app.getScreenSize().y * 1/5, 50);
	}
	
	public void startGame(int hp) {
		super.getApplication().removeAndAdd(new PlayScreen(super.getApplication()));
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDraw(Graphics2D g) {
		// TODO Auto-generated method stub
		_easyButton.draw(g);
		_hardButton.draw(g);
		_insaneButton.draw(g);
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_C) {
			super.getApplication().removeAndAdd(new CollisionTest(super.getApplication()));
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		if(_easyButton.contains(e.getX(), e.getY())) {
			_easyButton.click();
		}
		
		if(_hardButton.contains(e.getX(), e.getY())) {
			_hardButton.click();
		}
		
		if(_insaneButton.contains(e.getX(), e.getY())) {
			_insaneButton.click();
		}
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		if(_easyButton.contains(e.getX(), e.getY())) {
			_easyButton.mouseOver();
		}
		else {
			_easyButton.unMouseOver();
		}
		
		if(_hardButton.contains(e.getX(), e.getY())) {
			_hardButton.mouseOver();
		}
		else {
			_hardButton.unMouseOver();
		}
		
		if(_insaneButton.contains(e.getX(), e.getY())) {
			_insaneButton.mouseOver();
		}
		else {
			_insaneButton.unMouseOver();
		}
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onResize() {
		// TODO Auto-generated method stub
	}
}
