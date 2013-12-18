package iebaker.tou.world;

import iebaker.krypton.world.continuous.Entity;
import iebaker.krypton.world.continuous.Shape;
import iebaker.krypton.world.continuous.Circle;
import iebaker.krypton.world.continuous.AAB;
import iebaker.krypton.core.Artist;
import cs195n.Vec2f;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Paint;

public class Player extends Entity {
	public float my_angle = (float)Math.PI/2;
	public int bullets = 100;

	public Player(Vec2f pos) {
		super(pos);
		my_top_speed = 1.5f;
	}

	@Override
	public void onDraw(Graphics2D g) {
		Artist a = new Artist();
		a.setFillPaint(Color.BLUE);
		a.rect(g, my_position.x - 15, my_position.y - 15, 30, 30);		
		Vec2f direction = Vec2f.fromPolar(my_angle, 40);
		Vec2f cp = my_position.plus(direction).minus(new Vec2f(5, 5));
		a.ellipse(g, cp.x, cp.y, 10, 10);	
		a.setFillPaint(Color.GREEN);
		a.ellipse(g, my_position.x, my_position.y, 2,2);
	}

	@Override
	public Shape getRepresentativeShape() {
		return new AAB(my_position.minus(new Vec2f(15, 15)), my_position.plus(new Vec2f(15, 15)));
	}

	public Bullet fireBullet() {
		if(bullets > 0) {
			Vec2f direction = Vec2f.fromPolar(my_angle, 40);
			Vec2f cp = my_position.plus(direction).minus(new Vec2f(5, 5));
			Bullet b =  new Bullet(cp, direction, this);
			b.bindToWorld(my_world);
			bullets--;
			return b;
		} else {
			return null;
		}
	}

	public GravityBall fireGravityBall() {
		Vec2f direction = Vec2f.fromPolar(my_angle, 5);
		GravityBall gb = new GravityBall(my_position.plus(new Vec2f(15, 15)), direction, this);
		//gb.bindToWorld(my_world);
		return gb;
	}

	public void rotate(float amt) {
		my_angle += amt; 
	}

	public void reverse() {
		my_angle += Math.PI;
	}

	public void push() {
		Vec2f impulse = Vec2f.fromPolar(my_angle, 20);
		my_acceleration = my_acceleration.plus(impulse);
	}

	public int getBullets() {
		return bullets;
	}

	public void addBullets(int num) {
		bullets += num;
	}

	public void addHealth(float num) {
		health += num;
	}
}
