package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import iebaker.argon.world.Sprite;
import iebaker.argon.ai.Selector;
import iebaker.argon.ai.BehaviorTree;
import iebaker.argon.ai.Status;
import iebaker.argon.ai.Behavior;
import iebaker.argon.ai.Condition;
import java.util.List;
import java.util.ArrayList;

public class StoneSoldier extends StickSoldier {

	private BehaviorTree my_btree = new StoneSoldierBTree(this);
	private StoneSoldierBehavior my_behavior = StoneSoldierBehavior.NULL;
	private boolean newbehavior = true;
	private String t = "";

	private enum StoneSoldierBehavior {
		NULL, PATROL_NS, PATROL_EW, PATROL_SQ
	}

	private class StoneSoldierBTree extends Selector {
		private StoneSoldier self;

		public StoneSoldierBTree(StoneSoldier s) {
			this.self = s;

			addChild(new StoneSoldierCNode(self, Terrain.SAND));
			addChild(new StoneSoldierBNode(self, StoneSoldierBehavior.PATROL_NS));
			addChild(new StoneSoldierCNode(self, Terrain.DIRT));
			addChild(new StoneSoldierBNode(self, StoneSoldierBehavior.PATROL_EW));
			addChild(new StoneSoldierCNode(self, Terrain.GRASS));
			addChild(new StoneSoldierBNode(self, StoneSoldierBehavior.PATROL_SQ));
		}
	}

	private class StoneSoldierCNode extends Condition {
		private String terrain;
		private StoneSoldier self;
		public StoneSoldierCNode(StoneSoldier s, String t) {
			super();
			this.self = s;
			this.terrain = t;
		}

		public Status update() {
			zyxwv(terrain);
			String type = Terrain.getTerrainType(self.getPlace());
			if(type.equals(terrain)) {
				return Status.SUCCESS;
			} else {
				return Status.FAILURE;
			}
		}
	}

	private class StoneSoldierBNode extends Behavior {
		private StoneSoldierBehavior behavior;
		private StoneSoldier self;

		public StoneSoldierBNode(StoneSoldier s, StoneSoldierBehavior ssb) {
			super();
			this.behavior = ssb;
			this.self = s;
		}

		public Status update() {
			String terrain = Terrain.getTerrainType(my_place);
			if(terrain != t) return Status.FAILURE;
			if(!started) {
				started = true;
				self.setBehavior(behavior);
				return Status.RUNNING;
			}
			if(target_places.isEmpty()) {
				return Status.SUCCESS;
			}
			self.setBehavior(behavior);
			return Status.RUNNING;
		}
	}

	public StoneSoldier() {
		super();
		showpath = false;
	}

	public void setBehavior(StoneSoldierBehavior ssb) {
		if(ssb != my_behavior) {
			my_behavior = ssb;
			newbehavior = true;
		}
	}

	@Override
	public void step() {
		if(targetnum > 4 || targetnum == -1) {
			target_places.clear();
			Status s = my_btree.update();
			if(s != Status.RUNNING) {
				my_btree.reset();
			}
			targetnum = 0;
			newbehavior = true;
		}
		if(newbehavior) {
			Place n = my_place;
			for(int i = 0; i < 5; ++i) {
				n = n.getNextPlace(Place.Heading.NORTH);
			}
			Place s = my_place;
			for(int i = 0; i < 5; ++i) {
				s = s.getNextPlace(Place.Heading.SOUTH);
			}
			Place e = my_place;
			for(int i = 0; i < 5; ++i) {
				e = e.getNextPlace(Place.Heading.EAST);
			}
			Place w = my_place;
			for(int i = 0; i < 5; ++i) {
				w = w.getNextPlace(Place.Heading.WEST);
			}
			switch(my_behavior) {
				case PATROL_NS:
					addTargetPlace(n);
					addTargetPlace(s);
				break;
				case PATROL_EW:
					addTargetPlace(e);
					addTargetPlace(w);
				break;
				case PATROL_SQ:
					addTargetPlace(n);
					addTargetPlace(e);
					addTargetPlace(s);
					addTargetPlace(w);
				break;
			}
			newbehavior = false;
		}
		super.step();
	}

	@Override
	public void setPath(List<Place> path) {
		this.path = path;
	}

	@Override
	public void spawn() {
		if(new java.util.Random().nextFloat() > 0.5) return;
		if(corner || target_places.isEmpty()) return;

		Place.Heading left = my_heading.plus(Place.Turn.LEFT);
		Place.Heading right = my_heading.plus(Place.Turn.RIGHT);

		Place n_left = my_place.getNextPlace(left);
		Place n_right = my_place.getNextPlace(right);

		if(my_gridgraph.isRealPlace(n_left)) my_gridgraph.addEntity(n_left, new StoneBullet(left));
		if(my_gridgraph.isRealPlace(n_right)) my_gridgraph.addEntity(n_right, new StoneBullet(right));
	}

	@Override
	public void dealWith(Entity e) {
		if(e instanceof Sticks) {
			System.out.println("Sticks!");
		} else if(e instanceof Stones) {
			System.out.println("Stones!");
		}
	}

	@Override
	public Sprite getSprite() {
		return Sprites.group("entities").sprite("stone soldier");
	}

	@Override
	public String getTeam() {
		return "stone";
	}

	public void zyxwv(String t) {
		this.t = t;
	}
}