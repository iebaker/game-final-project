package game;

import java.util.Map;

import cs195n.Vec2f;
import engine.connections.Input;
import engine.entity.Entity;

/**
 * Player entity class
 * 
 * @author dgattey
 * 
 */
public class Player extends Entity {
	
	private static final long	serialVersionUID	= 1654501146675497149L;
	public Vec2f				goalVelocity;
	private GameWorld w;
	
	public Player() {
		super();
		this.goalVelocity = new Vec2f(0, 0);
		
		/**
		 * Switches gravity
		 */
		inputs.put("switchGravity", new Input() {
			
			private static final long	serialVersionUID	= -7065159977438010815L;
			
			@Override
			public void run(Map<String, String> args) {
				world.flipGravity();
			}
		});
	}
	
	@Override
	/**
	 * Applies the goal velocity force until it reaches actual velocity
	 */
	public void onTick(float t) {
		if (world.getPlayer() == null) world.setPlayer(this);
		if (!goalVelocity.equals(new Vec2f(0, 0))) {
			if (!goalVelocity.equals(velocity))
				applyImpulse((goalVelocity.minus(velocity)).smult(0.05f));
			else
				goalVelocity = new Vec2f(0, 0);
		}
		if (!world.checkBounds(shape.getLocation()))
			world.setLose("You fell (or jumped) out of the world!");
		else if (hp < 0) world.setLose("Your health dropped below zero...");
		super.onTick(t);
	}
	
	/**
	 * Returns if player can jump
	 * 
	 * @return ability to jump currently
	 */
	public boolean canJump() {
		if(!w.getJumpUnlocked() || this.contactDelay <= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Jumps by applying the appropriate force
	 */
	public void jump() {
		applyImpulse(this.lastMTV.normalized().smult(world.gravity() * 40));
		this.contactDelay = 0;
	}
	
	public Vec2f getCenterPosition() {
		return this.shape.getCenter();
	}
	
	public void setGameWorld(GameWorld gw) {
		this.w = gw;
	}
	
}
