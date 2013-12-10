package game.entities;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import engine.Artist;
import engine.Viewport;
import engine.entity.Entity;
import engine.sound.Sound;
import engine.sound.SoundHolder;
import game.MuteHolder;
import game.entities.spawners.Spawner;

public class LightCrystal extends Entity implements Consumable {
	
	private static final long	serialVersionUID	= -324958466738745396L;
	protected Spawner source;
	
	public LightCrystal(Spawner source) {
		stopsLight = false;
		this.source = source;
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
	public void destroy() {
		if (!MuteHolder.muted) {
			Sound pickup = null;
			if (SoundHolder.soundTable != null) pickup = SoundHolder.soundTable.get("pickup");
			if (pickup != null) {
				pickup.duplicate().play();
			}
		}
		world.removeEntity(this);
		source.spawnConsumed();
	}
	
}
