package iebaker.krypton.world.continuous;

import cs195n.Vec2f;
import java.awt.Graphics2D;

public class Entity {
	protected float damage_dealt = 0;
	protected float health = 100;
	protected float my_top_speed = 1;
	protected Vec2f my_position;
	protected Vec2f my_velocity;
	protected Vec2f my_acceleration;
	protected World my_world;

	public Entity(Vec2f pos) {
		my_position = pos;
		my_velocity = new Vec2f(0,0);
		my_acceleration = new Vec2f(0,0);
	}

	public Entity(Vec2f pos, Vec2f vel) {
		my_position = pos;
		my_velocity = vel;
		my_acceleration = new Vec2f(0,0);
	}

	public void onTick(long nanos) {
		if(health < 0) {
			//System.out.println(health);
			if(this.freeFromWorld() == false) System.out.println("Tried to free a non bound entity");
		}
		float seconds = (float)nanos/(float)1E9;
		my_velocity = my_velocity.plus(my_acceleration.smult(seconds));
		float mag = my_velocity.mag();
		if(mag > my_top_speed) {
			my_velocity = my_velocity.normalized().smult(my_top_speed);
		}
		my_position = my_position.plus(my_velocity);
		my_acceleration = new Vec2f(0,0);
	}

	public void accelerate(Vec2f a) {
		my_acceleration = my_acceleration.plus(a);
	}

	public boolean bindToWorld(World w) {
		if(my_world == null) {
			my_world = w;
			return my_world.bindEntity(this);
		} else {
			return false;
		}
	}

	public boolean freeFromWorld() {
		if(my_world != null) {
			boolean res = my_world.freeEntity(this);
			if(res)
			my_world = null;
			return res;
		} else  {
			return false;
		}
	}

	public void onDraw(Graphics2D g) {
		return;
	}

	public boolean isBound() {
		return my_world != null;
	}

	public boolean onCollisionCheck(Entity e) {
		if(getRepresentativeShape().checkCollision(e.getRepresentativeShape())) {
			e.damage(damage_dealt);
			return true;
		}
		return false;
	}

	public void actOn(Entity e) {
		return;
	}

	public void onEdgeCheck() {
		if(my_world == null) return;
		Vec2f world_size = my_world.getSize();
		if(my_position.x <= 0 || my_position.x >= world_size.x || my_position.y <= 0 || my_position.y >= world_size.y) {
			this.freeFromWorld();
		}
	}

	public Shape getRepresentativeShape() {
		return new Circle(new Vec2f(0,0),0);
	}

	public void damage(float damage_amt) {
		health += damage_amt;
	}

	public Vec2f getPosition() {
		return my_position;
	}

	public float getHealth() {
		return health;
	}
}
