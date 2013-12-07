package game;

import java.awt.Color;
import java.awt.RadialGradientPaint;

import engine.Artist;
import engine.Viewport;
import engine.entity.EnemyEntity;

/**
 * Superclass of all shadow-based enemies. Controls the shadow-drawing parts.
 * @author Sawyer
 *
 */
public abstract class ShadowEnemy extends EnemyEntity {

	private static final long serialVersionUID = 4005660633789539911L;

	private float baseRadius;
	private float[] fractions;	
	
	public ShadowEnemy(float radius, float[] fractions) {
		super();
		baseRadius = radius;
		this.fractions = fractions;
		this.stopsLight = false;
	}
	
	@Override
	public void onTick(float secs) {
		super.onTick(secs);
		if((((GameWorld) this.world).getStartCrystal() != null) && ((GameWorld) this.world).getStartCrystal().shape.getCenter().minus(this.shape.getCenter()).mag2() <= 80000) {
			this.damage(10);
		}
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(this.shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(this.shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(this.shape.getCenter().x - this.shape.getLocation().x + baseRadius * this.hp / this.maxHP);
		Color[] colors = new Color[] { new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(this.shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(this.shape.getLocation()).y;
		a.ellipse(g, topLeftx - radius, topLefty - radius, (centerx - topLeftx + radius)*2, (centery - topLefty + radius)*2);
		super.onDraw(g);
	}
}
