package game.entities;

import engine.Artist;
import engine.Viewport;
import engine.World;
import game.entities.spawners.Spawner;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import cs195n.LevelData.EntityData;

public class DarkenedCrystal extends LightCrystal {

	private static final long serialVersionUID = -7516116297460743602L;

	public DarkenedCrystal(Spawner source) {
		super(source);
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(40);
		float[] fractions = new float[] { 0f, 1f };
		Color[] colors = new Color[] { new Color(0.7f, 0.7f, 1f, 0.5f), new Color(0.7f, 0.7f, 1f, 0.0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(shape.getLocation()).y;
		a.ellipse(g, topLeftx - 40, topLefty - 40, (centerx - topLeftx + 40) * 2, (centery - topLefty + 40) * 2);
		super.onDraw(g);
	}
	
	@Override
	public void setProperties(EntityData ed, World w) {
		super.setProperties(ed, w);
		this.c = c.darker().darker();
	}
	
	@Override
	public void destroy() {
		world.removeEntity(this);
		this.source.spawnConsumed();
	}

}
