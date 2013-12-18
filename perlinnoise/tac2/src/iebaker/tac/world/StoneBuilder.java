package iebaker.tac.world;

import iebaker.argon.world.Entity;
import iebaker.argon.world.Place;
import iebaker.argon.world.Sprites;
import iebaker.argon.world.Sprite;
import iebaker.argon.ai.Sequence;
import iebaker.argon.ai.BehaviorTree;
import iebaker.argon.ai.Status;
import iebaker.argon.ai.Behavior;
import java.util.List;
import java.util.ArrayList;

public class StoneBuilder extends StickBuilder {

	private BehaviorTree my_btree = new StoneBuilderBTree(this);
	private StoneBuilderBehavior my_behavior = StoneBuilderBehavior.NULL;
	private boolean newbehavior = true;

	private enum StoneBuilderBehavior {
		NULL, BUILD_RANDOM, BUILD_WALL, BUILD_LONG_WALL
	}

	private class StoneBuilderBTree extends Sequence {
		private StoneBuilder self;
		public StoneBuilderBTree(StoneBuilder s) {
			this.self = s;

			addChild(new StoneBuilderBNode(self, StoneBuilderBehavior.BUILD_WALL));
			addChild(new StoneBuilderBNode(self, StoneBuilderBehavior.BUILD_RANDOM));
			addChild(new StoneBuilderBNode(self, StoneBuilderBehavior.BUILD_LONG_WALL));
		}
	}

	private class StoneBuilderBNode extends Behavior {
		private StoneBuilderBehavior behavior;
		private StoneBuilder self;

		public StoneBuilderBNode(StoneBuilder s, StoneBuilderBehavior sbb) {
			super();
			this.behavior = sbb;
			this.self = s;
		}

		public Status update() {
			if(!started) {
				started = true;
				self.setBehavior(behavior);
				return Status.RUNNING;
			}
			if(target_places.isEmpty()) {
				return Status.SUCCESS;
			} else {
				self.setBehavior(behavior);
				return Status.RUNNING;
			}
		}
	}

	public StoneBuilder() {
		super();
		showpath = false;
	}

	public void setBehavior(StoneBuilderBehavior sbb) {
		if(sbb != my_behavior) {
			my_behavior = sbb;
			newbehavior = true;
		}
	}

	@Override
	public void step() {
		//System.out.println("---");
		//System.out.println(target_places);
		Status s = my_btree.update();
		//System.out.println(s);
		if(s != Status.RUNNING) {
			my_btree.reset();
		}
		//System.out.println(path);
		//System.out.println(my_behavior);

		if(newbehavior) {
			switch(my_behavior) {
				case BUILD_RANDOM:
					addTargetPlace(my_gridgraph.getRandomPlace());
				break;
				case BUILD_WALL:
					Place rnd = my_gridgraph.getRandomPlace();
					Place.Heading h = Place.Heading.random();
					for(int i = 0; i < 5; ++i) {
						addTargetPlace(rnd);
						rnd = rnd.getNextPlace(h);
					}
				break;
				case BUILD_LONG_WALL:
					Place rnd2 = my_gridgraph.getRandomPlace();
					Place.Heading h2 = Place.Heading.random();
					for(int i = 0; i < 20; ++i) {
						addTargetPlace(rnd2);
						rnd2 = rnd2.getNextPlace(h2);
					}
				break;
			}
			newbehavior = false;
		}
		super.step();
	}

	@Override
	public void spawn() {
		if(moved && atloc) {
			my_gridgraph.addEntity(my_prev_place, new StoneWall());
		} 
	}

	@Override
	public void setPath(List<Place> path) {
		this.path = path;
	}

	@Override
	public Sprite getSprite() {
		switch(my_heading) {
			case NORTH:
				return Sprites.group("entities").sprite("stone builder north");
			case SOUTH:
				return Sprites.group("entities").sprite("stone builder south");
			case EAST:
				return Sprites.group("entities").sprite("stone builder east");
			case WEST:
				return Sprites.group("entities").sprite("stone builder west");
		}
		return my_sprite;
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
	public String getTeam() {
		return "stone";
	}
}