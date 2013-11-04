package smt3.gameengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

import smt3.gameengine.other.Sprite;
import smt3.gameengine.physics.Drawable;

public abstract class Button implements Drawable {
	
	private int _x;
	private int _y;
	private int _w;
	private int _h;
	private Sprite _sprite;
	private Color _bgColor;
	private Color _shadow;
	private Color _textColor;
	private String _text;
	private int _skew = 10;

	public Button(Color shadow, Color bgc, Color textColor, String text, int x, int y, int w, int h) {
		_x = x;
		_y = y;
		_w = w;
		_h = h;
		_shadow = shadow;
		_bgColor = bgc;
		_textColor = textColor;
		_text = text;
	}
	
	public Button(Sprite sprite, int x, int y, int w, int h) {
		_x = x;
		_y = y;
		_w = w;
		_h = h;
		_sprite = sprite;
	}
	
	public abstract void click();
	public abstract void mouseOver();
	public abstract void unMouseOver();
	
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		if(_sprite == null) {
			g.setColor(_shadow);
			g.fill(new Rectangle2D.Float(_x+_skew, _y+_skew, _w, _h));
			g.setColor(_bgColor);
			g.fill(new Rectangle2D.Float(_x, _y, _w, _h));
			g.setColor(_textColor);
			
			Font f = new Font("Arial", Font.PLAIN, 10);
			g.setFont(f);
			FontMetrics fm = g.getFontMetrics(f);
						
			while(fm.getHeight() < _h*.75 && fm.stringWidth(_text) < _w*.75) {
				f = f.deriveFont((float) fm.getFont().getSize() + 1);
				fm = g.getFontMetrics(f);
			}
			g.setFont(f);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawString(_text, (_w - fm.stringWidth(_text)) / 2 + _x, (_h + fm.getHeight()/2) / 2 + _y);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		else {
			_sprite.draw(g, _x, _y, _x + _w, _y + _h);
		}
	}
	
	public boolean contains(int x, int y) {
		if(x > _x && x < _x+_w && y > _y && y < _y + _h) {
			return true;
		}
		return false;
	}
	
	public void setX(int x) {
		_x = x;
	}
	
	public int getX() {
		return _x;
	}
	
	public void setY(int y) {
		_y = y;
	}
	
	public int getY() {
		return _y;
	}
	
	public void setSkew(int skew) {
		_skew = skew;
	}
	
}
