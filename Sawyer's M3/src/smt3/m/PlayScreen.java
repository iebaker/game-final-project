package smt3.m;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.FileNotFoundException;

import cs195n.CS195NLevelReader;
import cs195n.CS195NLevelReader.InvalidLevelException;
import cs195n.LevelData;
import cs195n.Vec2f;
import smt3.gameengine.other.Viewport;
import smt3.gameengine.ui.Application;
import smt3.gameengine.ui.Screen;

public class PlayScreen extends Screen {
	
	private MWorld _world;
	private Viewport _vp;

	public PlayScreen(Application app) {
		super(app);
		try {
			LevelData data = CS195NLevelReader.readLevel(new File("BasicLevel.nlf"));
			String[] dims = data.getProperties().get("dimensions").split("[,]");
			_world = new MWorld(app, new Vec2f(Float.parseFloat(dims[0]), Float.parseFloat(dims[1])));
			_vp = new Viewport(new Vec2f(0, 0), new Vec2f(super.getApplication().getScreenSize().x, super.getApplication().getScreenSize().y), _world);
			_world.setVP(_vp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidLevelException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		_world.onTick(nanosSincePreviousTick);
	}

	@Override
	protected void onDraw(Graphics2D g) {
		_vp.draw(g);
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		_world.onKeyPressed(e);
		if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			super.getApplication().removeAndAdd(new WelcomeScreen(super.getApplication()));
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		_world.onKeyReleased(e);
	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		_world.onMousePressed(e);
	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		_world.onMouseReleased(e);
	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		_world.onMouseDragged(e);
	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		_world.onMouseMoved(e);
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize() {
		_vp.setScreenDims(new Vec2f(super.getApplication().getScreenSize().x, super.getApplication().getScreenSize().y));
	}

}
