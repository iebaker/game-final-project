package iebaker.wt.world;

import iebaker.xenon.core.Entity;
import iebaker.xenon.core.Artist;
import java.awt.Graphics2D;
import java.awt.Color;

import cs195n.Vec2f;

public class Player extends Entity implements WTEntity {

	private WTWorld.Color my_color;
	private float right_velocity = 200;
	private float left_velocity = -200;
	
	public Player(Vec2f pos, String id) {
		super(pos, new Vec2f(0,0), 20f, id);
		addOutputs("onTouchBall", "onTouchExit");
	}

	public void onCollideWith(Entity e) {
		if(e instanceof Ball) getOutput("onTouchBall").run();
		if(e instanceof Exit) getOutput("onTouchExit").run();
	}

	public WTWorld.Color getColor() {
		return my_color;
	}

	public void setColor(WTWorld.Color c) {
		if(my_color == null) {
			my_color = c;
		}
	}

	@Override
	public void render(Artist a, Graphics2D g) {
		a.setFillPaint(WTWorld.toAWTColor(my_color));

		a.setStrokePaint(Color.BLACK);
		a.setStroke(true);
		super.render(a, g);
		a.setStroke(false);
	}
	
/*=================================
|      PLAYER ACTION METHODS      |
=================================*/

	public void jumpBall(int dir) {
		Ball b = ((WTWorld)my_world).getBall();
		Vec2f ball_pos = b.getPosition();
		float dist = ball_pos.dist(my_position);

		if(dir == -1) {
			if(ball_pos.x < my_position.x && dist < 50) {
				b.jump();
			}
		} else if(dir == 1) {
			if(ball_pos.x > my_position.x && dist < 50) {
				b.jump();
			}
		}
	}

	public void moveRight() {
		float x_vel = my_velocity.x;
		float diff = right_velocity - x_vel;
		this.addImpulse(new Vec2f(diff, 0));
	}

	public void moveLeft() {
		float x_vel = my_velocity.x;
		float diff = left_velocity - x_vel;
		this.addImpulse(new Vec2f(diff, 0));
	}

	public void jump() {
		if(my_velocity.y > -500 && jumpable) {
			this.addImpulse(new Vec2f(0, -100*my_mass));
		}
	}
}
