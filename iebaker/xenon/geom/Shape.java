package iebaker.xenon.geom;

import cs195n.Vec2f;
import iebaker.xenon.util.Collision;

import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;

public interface Shape {
	public Collision checkCollision(Shape s);
	public Collision checkCircleCollision(Circle c);
	public Collision checkAABCollision(AAB a);
	public Collision checkPolygonCollision(Polygon p);
	public Vec2f getCenter();
	public Shape at(Vec2f newCenter);
	public void drawSelf(Artist a, Graphics2D g);
}
