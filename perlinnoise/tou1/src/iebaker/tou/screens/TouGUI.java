package iebaker.tou.screens;

import iebaker.krypton.core.Application;
import iebaker.tou.world.TouWorld;
import iebaker.krypton.slice.Node;
import iebaker.krypton.core.Screen;
import iebaker.krypton.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class TouGUI extends Screen {
	private TouWorld my_world;

	public TouGUI(Application a, String id, TouWorld world) {
		super(a, id);
		my_world = world;

		root_node = new Node(parent_application, this, "ineedtosleepmore");
	}

	@Override
	public void onTick(long nanos) {
		super.onTick(nanos);
		if(my_world.getLives() <= 0) {
			my_world.freeAll();
			parent_application.getScreenManager().pushScreen(new TouEndScreen(parent_application, "tou.endscreen", my_world.getTotalTime()));

		}
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.BLACK);
		a.setFontSize(20);
		a.text(g, "Health: " + my_world.getPlayer().getHealth() + "", 30, 30);
		a.text(g, "Bullets: " + my_world.getPlayer().getBullets(), 30, 60);
		a.text(g, "Lives: " + my_world.getLives(), 30, 90);
		a.text(g, "Time survived: " + my_world.getTotalTime(), 30, 120);
	}

	@Override
	public void onKeyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				my_world.getPlayer().rotate((float)(-Math.PI/32));
				break;
			case KeyEvent.VK_RIGHT:
				my_world.getPlayer().rotate((float)(Math.PI/32));
				break;
			case KeyEvent.VK_UP:
				my_world.getPlayer().push();
				break;
			case KeyEvent.VK_SPACE:
				my_world.getPlayer().fireBullet();
				break;
			case KeyEvent.VK_SHIFT:
				my_world.getPlayer().fireGravityBall();
				break;
			case KeyEvent.VK_DOWN:
				my_world.getPlayer().reverse();
				break;
		}
	}

}
