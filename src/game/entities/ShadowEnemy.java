package game.entities;

import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.util.Map;

import engine.Artist;
import engine.Viewport;
import engine.entity.EnemyEntity;
import game.GameWorld;
import game.entities.spawners.Spawner;

/**
 * Superclass of all shadow-based enemies. Controls the shadow-drawing parts.
 * @author Sawyer
 *
 */
public abstract class ShadowEnemy extends EnemyEntity {

	private static final long serialVersionUID = 4005660633789539911L;

	private float baseRadius;
	private float[] fractions;	
	private Spawner source;
	
	public ShadowEnemy(float radius, float[] fractions, Spawner source) {
		super();
		baseRadius = radius;
		this.fractions = fractions;
		this.stopsLight = false;
		this.source = source;
	}
	
	@Override
	public void onTick(float secs) {
		super.onTick(secs);
		StartCrystal sc = ((GameWorld) this.world).getStartCrystal();
		if((sc != null) && sc.shape.getCenter().minus(this.shape.getCenter()).mag2() <= 80000) {
			//this.applyForce(sc.shape.getCenter().minus(this.shape.getCenter()).normalized().smult(-15000));
		}
	}
	
	@Override
	public void onDraw(java.awt.Graphics2D g) {
		Artist a = new Artist();
		a.setStroke(false);
		
		float centerx = Viewport.gamePtToScreen(this.shape.getCenter()).x;
		float centery = Viewport.gamePtToScreen(this.shape.getCenter()).y;
		
		float radius = Viewport.gameFloatToScreen(this.shape.getCenter().x - this.shape.getLocation().x + baseRadius * (this.hp + 7) / (this.maxHP + 7));
		Color[] colors = new Color[] { new Color(0f, 0f, 0f, 1f), new Color(0f, 0f, 0f, 0f) };
		
		RadialGradientPaint rgp = new RadialGradientPaint(centerx, centery, radius, fractions, colors);
		
		a.setFillPaint(rgp);
		
		float topLeftx = Viewport.gamePtToScreen(this.shape.getLocation()).x;
		float topLefty = Viewport.gamePtToScreen(this.shape.getLocation()).y;
		a.ellipse(g, topLeftx - radius, topLefty - radius, (centerx - topLeftx + radius)*2, (centery - topLefty + radius)*2);
		super.onDraw(g);
	}
	
	@Override
	public void die() {
		source.spawnConsumed();
		super.die();
	}

	public void runInput(String in, Map<String, String> args) {
		this.inputs.get(in).run(args);
	}
}
