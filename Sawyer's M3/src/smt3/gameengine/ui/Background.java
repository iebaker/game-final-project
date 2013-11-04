package smt3.gameengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;

import smt3.gameengine.other.World;
import smt3.gameengine.physics.Rectangle;

public class Background {

	Rectangle _bg;
	
	public Background(World world, Color color) {
		_bg = new Rectangle(world.getGameCoords(), world.getGameCoords().plus(world.getGameDims()), color);
	}

	public void onDraw(Graphics2D g) {
		_bg.draw(g);
	}
}
