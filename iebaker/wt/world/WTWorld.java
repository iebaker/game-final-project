package iebaker.wt.world;

import cs195n.Vec2f;
import cs195n.CS195NLevelReader;
import cs195n.LevelData;
import iebaker.xenon.core.Entity;
import iebaker.xenon.geom.AAB;
import iebaker.xenon.geom.Circle;
import iebaker.xenon.geom.Polygon;
import iebaker.xenon.geom.Shape;
import iebaker.xenon.core.World;
import iebaker.xenon.core.Connection;
import iebaker.xenon.util.Collisions;
import iebaker.wt.screens.WTResultScreen;
import java.util.ArrayList;
import java.io.File;

public class WTWorld extends World {

	private final String LEVEL1 = "lib/levels/1ThePremise.nlf";
	private final String LEVEL2 = "lib/levels/2TheresNoIinTeam.nlf";
	private final String LEVEL3 = "lib/levels/3LockAndKey.nlf";
	private final String LEVEL4 = "lib/levels/4RoomOfRequirements.nlf";

	private java.util.List<String> my_levels = new ArrayList<String>();
	private int current_level = -1;
	private boolean blue_finished = true;
	private boolean red_finished = true;
	private boolean game_over = false;

	//Expected entities

	private Player red_player;
	private Player blue_player;
	private Ball ball;
	private BallTarget ball_target;
	private Exit exit;

	public enum Color {
		RED, BLUE, NEUTRAL
	}

	public enum Result {
		WIN, DEATH
	}

	public WTWorld(Vec2f size) {
		super(size);
	}

	public static java.awt.Color toAWTColor(WTWorld.Color c) {
		if(c == WTWorld.Color.BLUE) {
			return java.awt.Color.BLUE;
		} else if(c == WTWorld.Color.RED) {
			return java.awt.Color.RED;
		}
		return java.awt.Color.GRAY;
	}

	public static java.awt.Color toTransAWTColor(WTWorld.Color c) {
		if(c == WTWorld.Color.BLUE) {
			return new java.awt.Color(0f, 0f, 1f, 0.5f);
		} else if(c == WTWorld.Color.RED) {
			return new java.awt.Color(1f, 0f, 0f, 0.5f);
		}
		return new java.awt.Color(0f, 0f, 0f, 0.5f);
	}

	@Override
	public void init() {
		addLevels(LEVEL1, LEVEL2, LEVEL3);
	}

	public void loadLevel(String level_loc) {
		CS195NLevelReader lr = new CS195NLevelReader();

		try {
			LevelData ld = lr.readLevel(new File(level_loc));

			for(LevelData.EntityData entity : ld.getEntities()) {
				spawnEntity(entity);
			}

			this.birth();

			for(LevelData.ConnectionData connection : ld.getConnections()) {
				buildConnection(connection);
			}
		} catch (Exception e) {
			System.err.println("Error reading level data file");
			e.printStackTrace();
		}
	}

	private void spawnEntity(LevelData.EntityData e_data) {
		Entity new_entity = null;
		Shape entity_shape = null;

		Vec2f pos = null;

		LevelData.ShapeData s_data = e_data.getShapes().get(0);
		pos = s_data.getCenter();

		switch(s_data.getType()) {
			case CIRCLE:
				entity_shape = new Circle(pos, s_data.getRadius());
				break;

			case POLY:
				entity_shape = new Polygon(s_data.getVerts());
				break;

			case BOX:
				entity_shape = new AAB(s_data.getMin(), s_data.getMax());
				break;
		}

		String color = e_data.getProperties().get("color");
		if(color == null) {
			System.err.println("Entity listed with no color, setting color to neutral");
			color = "neutral";
		}
		WTWorld.Color wtw_color = this.toWTWColor(color);

		switch(e_data.getEntityClass()) {
			case "Ball":
				new_entity = new Ball(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				ball = (Ball) new_entity;
				break;

			case "BallTarget":
				new_entity = new BallTarget(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				ball_target = (BallTarget) new_entity;
				break;

			case "Barrier":
				new_entity = new Barrier(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				break;

			case "Exit":
				new_entity = new Exit(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				exit = (Exit) new_entity;
				break;

			case "Player":
				new_entity = new Player(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				if(color.equals("red")) {
					red_player = (Player) new_entity;
				} else if(color.equals("blue")) {
					blue_player = (Player) new_entity;
				}
				break;

			case "LockWall":
				new_entity = new LockWall(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				break;

			case "Key":
				new_entity = new Key(pos, e_data.getName());
				((WTEntity)new_entity).setColor(wtw_color);
				break;
		}

		if(new_entity != null && entity_shape != null && pos != null) {
			new_entity.setShape(entity_shape);
			new_entity.bindToWorld(this);
		}
	}

	@Override
	public void affect(Entity e) {
		e.addForce(new Vec2f(0,150*e.getMass()));
	}

	private WTWorld.Color toWTWColor(String s) {
		switch(s) {
			case "red":
				return WTWorld.Color.RED;
			case "blue":
				return WTWorld.Color.BLUE;
		}
		return WTWorld.Color.NEUTRAL;
	}

	private void buildConnection(LevelData.ConnectionData c_data) {
		Connection c = new Connection(this.getEntity(c_data.getTarget()).getInput(c_data.getTargetInput()));
		this.getEntity(c_data.getSource()).getOutput(c_data.getSourceOutput()).connect(c);
	}

	@Override
	public void collideEntities() {
		for(int i = 0; i < my_entities.size(); ++i) {
			for(int j = i; j < my_entities.size(); ++j) {
				Entity e1 = my_entities.get(i);
				Entity e2 = my_entities.get(j);

				WTWorld.Color c1 = ((WTEntity)(e1)).getColor();
				WTWorld.Color c2 = ((WTEntity)(e2)).getColor();

				if(this.interacting(c1, c2) 
					|| (e1 instanceof Player && e2 instanceof Ball) 
					|| (e1 instanceof Ball && e2 instanceof Player)) Collisions.resolve(e1, e2);
			}
		}
	}

	public boolean interacting(WTWorld.Color c1, WTWorld.Color c2) {
		return (c1 == WTWorld.Color.RED && c2 == WTWorld.Color.RED) ||
			   (c1 == WTWorld.Color.BLUE && c2 == WTWorld.Color.BLUE) ||
			   c1 == WTWorld.Color.NEUTRAL || c2 == WTWorld.Color.NEUTRAL;
	}

	@Override
	public void updateEnvironment(float dt) {
		if(blue_finished && red_finished) {
			this.purge();
			++current_level;

			if(current_level < my_levels.size()) {
				this.loadLevel(my_levels.get(current_level));
				blue_finished = false;
				red_finished = false;
			} else {
				game_over = true;
			}
		}
	}

	public void restartLevel() {
		this.purge();
		this.loadLevel(my_levels.get(current_level));
	}

	public boolean gameOver() {
		return game_over;
	}

	private boolean hasRequiredEntities() {
		return red_player != null && blue_player != null && ball != null && ball_target != null && exit != null;
	}

	public void addLevels(String... levels) {
		for(String level : levels) {
			my_levels.add(level);
		}
	}

	public void purge() {
		my_entities = new ArrayList<Entity>();
		red_player = null;
		blue_player = null;
		ball = null;
		ball_target = null;
		exit = null;
	}

	public int getCurrentLevel() {
		return current_level;
	}

	public void recordBlueFinish() {
		blue_finished = true;
	}

	public void recordRedFinish() {
		red_finished = true;
	}

	public Player getRedPlayer() {
		return red_player;
	}

	public Player getBluePlayer() {
		return blue_player;
	}

	public Ball getBall() {
		return ball;
	}

	@Override
	public Vec2f getInitialCenter() {
		return new Vec2f(150,150);
	}

	@Override
	public float getInitialScale() {
		return 0.3f;
	}
}	
