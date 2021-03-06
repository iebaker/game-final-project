package game.entities;

import java.awt.event.KeyEvent;
import java.util.Map;

import cs195n.Vec2f;
import engine.connections.Input;
import engine.entity.Entity;
import engine.sound.Sound;
import engine.sound.SoundHolder;
import game.GameWorld;
import game.MuteHolder;

/**
 * Player entity class
 * 
 * @author dgattey
 * 
 */
public class Player extends Entity {
	
	private static final long	serialVersionUID	= 1654501146675497149L;
	public Vec2f				goalVelocity;
	private boolean				jumpUnlocked		= false;
	private boolean				laserUnlocked		= false;
	private transient boolean	moveLeft			= false;
	private transient boolean	moveRight			= false;
	private float				lightCountdown		= 1;
	private float			lightTime			= 0.5f;
	private int					crystals			= 100;
	private boolean				highJumpUnlocked	= false;
	private GameWorld			gw;
	private boolean				inLight				= false;
	private int					totalCrystals		= 0;
	
	public Player() {
		super();
		stopsLight = false;
		goalVelocity = new Vec2f(0, 0);
		
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
		
		inputs.put("addCrystal", new Input() {
			
			private static final long	serialVersionUID	= -563821758280686045L;
			
			@Override
			public void run(Map<String, String> args) {
				Player.this.addCrystal();
			}
		});
	}
	
	@Override
	/**
	 * Applies the goal velocity force until it reaches actual velocity
	 */
	public void onTick(float t) {
		if (gw == null) {
			gw = ((GameWorld) world);
		}
		if (world.getPlayer() == null) world.setPlayer(this);
		if (moveLeft && !moveRight) {
			goalVelocity = new Vec2f(-800, 0);
		} else if (moveRight && !moveLeft) {
			goalVelocity = new Vec2f(800, 0);
		} else {
			goalVelocity = Vec2f.ZERO;
		}
		
		if (!goalVelocity.equals(Vec2f.ZERO)) {
			if (!goalVelocity.equals(getVelocity())) {
				applyImpulse((goalVelocity.minus(getVelocity())).smult(0.05f));
			}
		}
		
		if (lightCountdown > 0) {
			lightCountdown -= t;
		}
		
		if (lightCountdown <= 0) {
			lightCountdown = lightTime;
			hp -= 1;
			if (hp <= 0) {
				gw.die();
			}
		}
		if ((gw.getStartCrystal() != null)
				&& gw.getStartCrystal().shape.getCenter().minus(shape.getCenter()).mag2() <= 80000) {
			if (heal(10) == 10 && !MuteHolder.muted) {
				Sound heal = null;
				if (SoundHolder.soundTable != null) heal = SoundHolder.soundTable.get("heal");
				if (heal != null) heal.play();
			}
			world.save();
			gw.reloadEnemies();
			if (!gw.explainedLight()) {
				gw.explainLight();
			}
			if (!gw.hasShopped() && crystals >= 5) {
				gw.explainShopping();
			}
			if (!inLight && gw.hasShopped()) gw.enterShop();
			inLight = true;
			// if(totalCrystals == 1) gw.win();
		} else
			inLight = false;
		
		if (!world.checkGameBounds(shape.getLocation()))
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
		if (!jumpUnlocked || contactDelay <= 0 || lastMTV == null || lastMTV.y >= 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Jumps by applying the appropriate force
	 */
	public void jump() {
		// Clear the current Y-velocity to stop bounce-jumps
		resetY();
		applyImpulse(lastMTV.normalized().smult(world.gravity() * ((highJumpUnlocked) ? 30 : 20)));
		contactDelay = 0;
	}
	
	/*public boolean canSwim() {
		if ()
	}*/
	
	/**
	 * Gets the center of the player's position. Useful for sound distance calculations
	 * 
	 * @return Center of the player's shape's position
	 */
	public Vec2f getCenterPosition() {
		return shape.getCenter();
	}
	
	/**
	 * Allows the player to jump
	 */
	public void unlockJump() {
		jumpUnlocked = true;
	}
	
	/**
	 * Allows player to jump higher
	 */
	public void unlockHighJump() {
		highJumpUnlocked = true;
	}
	
	/**
	 * Allows the player to fire a laser
	 */
	public void unlockLaser() {
		laserUnlocked = true;
	}
	
	public void decreaseLightLoss() {
		this.lightTime = 0.8f;
	}
	
	/**
	 * 
	 * @return true if laser is unlocked
	 */
	public boolean laserUnlocked() {
		return laserUnlocked;
	}
	
	/**
	 * Allows the player to respond to keyboard input
	 * 
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			moveLeft = true;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			moveRight = true;
			break;
		case (KeyEvent.VK_W):
		case (KeyEvent.VK_SPACE): // Jump
			if (canJump()) {
				jump();
			} /*else if (canSwim()) {
				swim(true);
				}*/
			break;
		/*case (KeyEvent.VK_DOWN):
		case (KeyEvent.VK_S): // Swim if possible
			if (canSwim()) {
				swim(false);
			}
			break;*/
		default:
			break;
		}
	}
	
	/**
	 * Allows the player to respond to keyboard input
	 * 
	 * @param e
	 */
	public void onKeyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			moveLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			moveRight = false;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Adds a crystal to the player's count and gives the player some light
	 */
	public void addCrystal() {
		crystals++;
		totalCrystals++;
		hp += 5;
		if (hp > maxHP) {
			hp = maxHP;
		}
	}
	
	/**
	 * 
	 * @return number of crystals the player has
	 */
	public int getCrystals() {
		return crystals;
	}
	
	public int getTotalCrystals() {
		return totalCrystals;
	}
	
	/**
	 * Spends a certain number of crystals
	 * 
	 * @param spent
	 */
	public boolean spendCrystals(int spent) {
		if (crystals >= spent) {
			crystals -= spent;
			return true;
		}
		return false;
	}
	
	@Override
	public void die() {
		gw.die();
	}
	
	@Override
	public void reloadTransientData() {
		moveLeft = false;
		moveRight = false;
		super.reloadTransientData();
	}
}
