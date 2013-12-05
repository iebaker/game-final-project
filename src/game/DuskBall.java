package game;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import engine.Artist;
import engine.Viewport;
import engine.entity.EnemyEntity;

public class DuskBall extends EnemyEntity {

	private static final long serialVersionUID = -8427819650544703193L;

	public DuskBall() {
		super();
		this.stopsLight = false;
		this.drains = true;
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(this.shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(this.shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(100 * this.hp / this.fullHP());
		float[] fractions = new float[] { 0.2f, 1f };
		Color[] colors = new Color[] { new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(this.shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(this.shape.getLocation()).y;
		a.ellipse(g, topLeftx - 100, topLefty - 100, (centerx - topLeftx + 100)*2, (centery - topLefty + 100)*2);
	}
}
