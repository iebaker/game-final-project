package game;

import java.util.Map;

import cs195n.LevelData.EntityData;
import cs195n.Vec2f;
import engine.World;
import engine.collision.CollisionInfo;
import engine.connections.Input;
import engine.entity.Entity;
import engine.sound.SoundRecipient;

/**
 * Player entity class
 * 
 * @author dgattey
 * 
 */
public class Player extends Entity implements SoundRecipient {
	
	private static final long	serialVersionUID	= 1654501146675497149L;
	public Vec2f				goalVelocity;
	private long				time;
	private GameWorld gw;
	
	public Player() {
		super();
		this.goalVelocity = new Vec2f(0, 0);
		this.time = 0;
		
		/**
		 * Switches gravity
		 */
		inputs.put("switchGravity", new Input() {
			
			private static final long	serialVersionUID	= -7065159977438010815L;
			
			@Override
			public void run(Map<String, String> args) {
				gw.flipGravity();
			}
		});
	}
	
	@Override
	/**
	 * Scary way to set Player's properties
	 */
	public void setProperties(EntityData ed, World world) {
		super.setProperties(ed, world);
		world.setSoundRecipient(this);
		world.setMainChar(this);
	}
	
	@Override
	/**
	 * Applies the goal velocity force until it reaches actual velocity
	 */
	public void onTick(float t) {
		if (gw.getSoundRecipient() == null) {gw.setSoundRecipient(this);}
		if (!goalVelocity.equals(new Vec2f(0, 0))) {
			if (!goalVelocity.equals(velocity))
				applyImpulse((goalVelocity.minus(velocity)).smult(0.05f));
			else
				goalVelocity = new Vec2f(0, 0);
		}
		if (!gw.checkBounds(shape.getLocation()))
			gw.setLose("You fell (or jumped) out of the world!");
		else if (hp < 0) gw.setLose("Your health dropped below zero...");
		super.onTick(t);
	}
	
	/**
	 * Collides like normal, but uses MTV to determine whether able to jump (on solidish ground - tops of circles too!)
	 */
	public void onCollide(CollisionInfo collisionInfo) {
		super.onCollide(collisionInfo);
		float y = collisionInfo.mtv.normalized().y;
		float up = (gw.gravity() > 1) ? -1.05f : 0.95f;
		float down = (gw.gravity() > 1) ? -0.95f : 1.05f;
		if (y < down && y > up)
			time = System.currentTimeMillis();
		else
			time = 0;
		
	}
	
	/**
	 * Returns if player can jump
	 * 
	 * @return ability to jump currently
	 */
	public boolean canJump() {
		return (time != 0 && System.currentTimeMillis() - time < 500);
	}
	
	/**
	 * Jumps by applying the appropriate force
	 */
	public void jump() {
		time = 0;
		applyImpulse(new Vec2f(0, gw.gravity() * -25));
	}
	
	public Vec2f getLocation() {
		return this.shape.getCenter();
	}
	
	public void setGameWorld(GameWorld gw) {
		this.gw = gw;
	}
	
}
