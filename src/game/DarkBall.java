package game;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.util.ArrayList;

import cs195n.Vec2f;
import engine.Artist;
import engine.Viewport;
import engine.entity.EnemyEntity;

public class DarkBall extends EnemyEntity {

	private static final long serialVersionUID = -8427819650544703193L;

	public DarkBall() {
		super();
		this.stopsLight = false;
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(this.shape.getLocation()).x;
		float centery = Viewport.gamePtToScreen(this.shape.getLocation()).y;
		
		float radius = Viewport.gameFloatToScreen(100f);
		float[] fractions = new float[] { 0.2f, 1f };
		Color[] colors = new Color[] { new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		ArrayList<Vec2f> al = new ArrayList<Vec2f>();
		al.add(new Vec2f(0,0));
		al.add(new Vec2f(0, 10000));
		al.add(new Vec2f(10000, 10000));
		al.add(new Vec2f(10000, 0));
		a.path(g, al);
	}
}
