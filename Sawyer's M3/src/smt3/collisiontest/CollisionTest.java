package smt3.collisiontest;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import cs195n.Vec2f;
import smt3.gameengine.physics.*;
import smt3.gameengine.ui.Application;
import smt3.gameengine.ui.Screen;
import smt3.m.WelcomeScreen;

public class CollisionTest extends Screen {
	
	private ArrayList<Shape> _shapes;
	private Shape _pickedUp;
	private Vec2f _lastPoint;
	private Vec2f _mtv;

	public CollisionTest(Application app) {
		super(app);
		_shapes = new ArrayList<Shape>();
		//_shapes.add(new ConvexPolygon(new Color(0,0,0), new Vec2f(40, 10), new Vec2f(10, 50), new Vec2f(150, 75)));
		//_shapes.add(new ConvexPolygon(new Color(0,0,0), new Vec2f(40, 10), new Vec2f(10, 50), new Vec2f(150, 75)));
		//_shapes.add(new ConvexPolygon(new Color(0,0,0), new Vec2f(40, 10), new Vec2f(10, 50), new Vec2f(100, 150), new Vec2f(150, 75)));
		_shapes.add(new Circle(new Vec2f(200,200), 50, new Color(0,0,0)));
		//_shapes.add(new Circle(new Vec2f(200,200), 50, new Color(0,0,0)));
		_shapes.add(new Rectangle(new Vec2f(0,0), new Vec2f(50,50), new Color(0,0,0)));
	}

	@Override
	protected void onTick(long nanosSinceLastTick) {
		for(Shape s : _shapes) {
			s.setColor(new Color(0,0,0));
		}
		for(int i=0; i < _shapes.size(); i++) {
			for(int j=i+1; j<_shapes.size(); j++) {
				_mtv = _shapes.get(i).collide(_shapes.get(j)).mtv;
				if(_mtv != null) {
					_shapes.get(i).setColor(new Color(255,0,0));
					_shapes.get(j).setColor(new Color(255,0,0));
				}
			}
		}
	}

	@Override
	protected void onDraw(Graphics2D g) {
		// TODO Auto-generated method stub
		for(Shape shape : _shapes) {
			shape.draw(g);
		}
		if(_mtv != null) {
			g.setColor(new Color(0,0,252));
			g.drawLine(10,10,(int)(10+_mtv.x),(int)(10+_mtv.y));
		}
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			super.getApplication().removeAndAdd(new WelcomeScreen(super.getApplication()));
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
		for(Shape s : _shapes) {
			if(s.contains(new Vec2f(e.getX(), e.getY()))) {
				_lastPoint = new Vec2f(e.getX(), e.getY());
				_pickedUp = s;
			}
		}
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		_pickedUp = null;
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(_pickedUp != null) {
			Vec2f newPoint = new Vec2f(e.getX(), e.getY());
			Vec2f dif = newPoint.minus(_lastPoint);
			_pickedUp.setCoords(_pickedUp.getCoords().plus(dif));
			_lastPoint = newPoint;
		}
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

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
