package game;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import engine.Artist;
import engine.Viewport;
import engine.entity.Entity;
import engine.sound.SoundHolder;

public class LightCrystal extends Entity {

	private static final long serialVersionUID = -324958466738745396L;
	
	public LightCrystal() {
		this.stopsLight = false;
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(this.shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(this.shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(40);
		float[] fractions = new float[] { 0f, 1f };
		Color[] colors = new Color[] { new Color(0.7f, 0.7f, 1f, 0.5f), new Color(0.7f, 0.7f, 1f, 0.0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(this.shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(this.shape.getLocation()).y;
		a.ellipse(g, topLeftx - 40, topLefty - 40, (centerx - topLeftx + 40)*2, (centery - topLefty + 40)*2);
		super.onDraw(g);
	}
	
	public void destroy() {
		SoundHolder.soundTable.get("pickup").duplicate().play();
		this.world.removeEntity(this);
	}

}
