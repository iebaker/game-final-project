package smt3.m;

import java.awt.Color;

import smt3.gameengine.ui.Button;

public class StartButton extends Button {
	
	private boolean _currMousedOver = false;
	private WelcomeScreen _ws;
	private int _hp;

	public StartButton(WelcomeScreen ws, Color shadow, Color bgc, Color textColor, String text, int x, int y,
			int w, int h, int hp) {
		super(shadow, bgc, textColor, text, x, y, w, h);
		_ws = ws;
		_hp = hp;
	}

	@Override
	public void click() {
		// TODO Auto-generated method stub
		_ws.startGame(_hp);
	}

	@Override
	public void mouseOver() {
		// TODO Auto-generated method stub
		if(!_currMousedOver) {
			super.setSkew(5);
			super.setX(super.getX() + 5);
			super.setY(super.getY() + 5);
			_currMousedOver = true;
		}
	}

	@Override
	public void unMouseOver() {
		// TODO Auto-generated method stub
		if(_currMousedOver) {
			super.setSkew(10);
			super.setX(super.getX() - 5);
			super.setY(super.getY() - 5);
			_currMousedOver = false;
		}
	}

}
