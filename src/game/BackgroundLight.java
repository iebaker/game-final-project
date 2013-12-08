package game;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import engine.Artist;
import engine.Viewport;
import engine.entity.PassableEntity;

public class BackgroundLight extends PassableEntity {
	
	private static final long	serialVersionUID	= 4143446809970571164L;
	private boolean				set					= false;
	
	public BackgroundLight() {
		super();
		isStatic = true;
	}
	
	@Override
	public void onTick(float secs) {
		if (set == false && world != null) {
			((GameWorld) world).addBackgroundLight(this);
			set = true;
		}
		super.onTick(secs);
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(300);
		float[] fractions = new float[] { 0.3f, 1f };
		Color[] colors = new Color[] { new Color(0.9f, 0.9f, 1f, 1f), new Color(0.9f, 0.9f, 1f, 0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(shape.getLocation()).y;
		a.ellipse(g, topLeftx - radius, topLefty - radius, (centerx - topLeftx + radius) * 2,
				(centery - topLefty + radius) * 2);
		super.onDraw(g);
	}
}
