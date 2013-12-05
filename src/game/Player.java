package game;

import java.awt.event.KeyEvent;
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
	private boolean jumpUnlocked = true;
	private boolean moveLeft = false;
	private boolean moveRight = false;
	private float lightCountdown = 1;
	private float lightTime = 1;
	
	public Player() {
		super();
		this.stopsLight = false;
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
		if(this.moveLeft && !this.moveRight) {
			goalVelocity = new Vec2f(-800,0);
		}
		else if(this.moveRight && !this.moveLeft) {
			goalVelocity = new Vec2f(800,0);
		}
		else {
			goalVelocity = new Vec2f(0,0);
		}
		
		if (!goalVelocity.equals(new Vec2f(0, 0))) {
			if (!goalVelocity.equals(getVelocity())) {
				applyImpulse((goalVelocity.minus(getVelocity())).smult(0.05f)); //Was 0.05f
			}
		}
		
		if(lightCountdown > 0) {
			lightCountdown -= t;
		}
		
		if(lightCountdown <= 0) {
			lightCountdown = lightTime;
			this.hp -= 1;
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
		if(!jumpUnlocked || this.contactDelay <= 0 || this.lastMTV.y >= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Jumps by applying the appropriate force
	 */
	public void jump() {
		//Clear the current Y-velocity to stop bounce-jumps
		this.resetY();
		this.applyImpulse(this.lastMTV.normalized().smult(world.gravity() * 30));
		this.contactDelay = 0;
	}
	
	/**
	 * Gets the center of the player's position. Useful for sound distance calculations
	 * @return Center of the player's shape's position
	 */
	public Vec2f getCenterPosition() {
		return this.shape.getCenter();
	}
	
	/**
	 * Allows the player to jump
	 */
	public void unlockJump() {
		jumpUnlocked = true;
	}
	
	/**
	 * Allows the player to respond to keyboard input
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A: moveLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D: moveRight = true;
			break;
		case (KeyEvent.VK_W): // W
		case (KeyEvent.VK_SPACE): // Jump
			if (this.canJump()) {
				this.jump();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Allows the player to respond to keyboard input
	 * @param e
	 */
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A : moveLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D: moveRight = false;
			break;
		default:
			break;
		}
	}
}
