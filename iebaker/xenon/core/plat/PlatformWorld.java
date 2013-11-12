package iebaker.xenon.core.plat;

import iebaker.xenon.util.Deck;
import iebaker.xenon.core.World;
import iebaker.xenon.core.Entity;
import cs195n.Vec2f;

public class PlatformWorld extends World {
	protected PWPlayer _player;
	protected PWGoal _goal;
	private Deck<Entity> entity_deck = new Deck<Entity>();
	private boolean has_player = false;
	private boolean has_goal = false;

	public PlatformWorld(Vec2f v) {
		super(v);
	}

	@Override
	public void addEntity(Entity e) {
		if(PWPlayer.class.isAssignableFrom(e.getClass())) {
			if(has_player) {
				System.err.println("World already contains a player, ignoring addEntity request.");
				return;
			} else {
				has_player = true;
			}
		} else if(PWGoal.class.isAssignableFrom(e.getClass())) {
			if(has_goal) {
				System.err.println("World already contains a goal, ignoring addEntity request.");
				return;
			} else {
				has_goal = true;
			}
		}
		entity_deck.add(e);
		super.addEntity(e);
	}

	@Override
	public void removeEntity(Entity e) {
		entity_deck.remove(e);
		super.removeEntity(e);
	}

	public Entity entity(Class<?> c, int index) {
		if(!entity_deck.hasClass(c)) {
			System.err.println("Entity deck does not contain class " + c.getName() + ", returning null.");
			return null;
		}

		java.util.List<Entity> like = entity_deck.ofClass(c);
		if(like.size() <= index) {
			System.err.println("Entity deck contains " + like.size() + " elements of class " + c.getName() +
				".  Index " + index + " out of bounds, returning null.");
			return null;
		}

		return like.get(index);
	}

	@Override 
	public void update(float dt) {
		if(this.winCondition()) {
			this.onWin();
		}
		super.update(dt);
	}

	public boolean winCondition() {
		return _player.getPosition().dist(_goal.getPosition()) < 10;
	}

	public void onWin() {
		System.out.println("Winner!");
	}
}