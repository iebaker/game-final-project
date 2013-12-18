package iebaker.krypton.core;

import iebaker.krypton.core.Artist;
import java.awt.Graphics2D;
import cs195n.Vec2f;

public interface Viewable {
	public void render(Artist a, Graphics2D g);
	public Vec2f getInitialCenter();
	public float getInitialScale();
}