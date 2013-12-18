package iebaker.tou.world;

import cs195n.Vec2f;
import iebaker.krypton.world.continuous.World;

public class TouWorld extends World {
	private Player my_player;
	private int interval = 10000;
	private int countdown = interval;
	private int total_time = 0;
	private boolean boss = false;
	private int lives = 5;

	public TouWorld(Vec2f size) {
		super(size);
		my_player = new Player(new Vec2f(40f, 40f));
		my_player.bindToWorld(this);
	}

	@Override
	public void onTick(long nanos) {
		total_time += (int)(nanos/1E6);
		if(total_time > 50000) {
			if(!boss) {
				TouBoss tb = new TouBoss(this.getRandomLocation());
				tb.bindToWorld(this);
				total_time = 0;
				boss = true;
			}
		}
		if(!my_player.isBound()) {
			lives--;
			if(lives == 0) {
				//Game over
			} else {
				my_player = new Player(new Vec2f(40f, 40f));
				my_player.bindToWorld(this);
			}
		}
		countdown -= (int)(nanos/1E6);
		if(countdown < 0) {
			spawnDemons();
			spawnExtraBullets();
			spawnExtraHealth();
			countdown = interval;
		}
		super.onTick(nanos);
	}

	private void spawnExtraBullets() {
		if(Math.random() > 0.8) {
			ExtraBullets eb = new ExtraBullets(this.getRandomLocation());
			eb.bindToWorld(this);
		}
	}

	private void spawnExtraHealth() {
		if(Math.random() > 0.5) {
			ExtraHealth eh = new ExtraHealth(this.getRandomLocation());
			eh.bindToWorld(this);
		}
	}

	private void spawnDemons() {
		int num = (int)(Math.random() * 5);
		for(int i = 0; i < num; ++i) {
			Demon d = new Demon(this.getRandomLocation());
			d.bindToWorld(this);
		}
	}

	public Player getPlayer() {
		return my_player;
	}

	public int getLives() {
		return lives;
	}

	public int getTotalTime() {
		return total_time;
	}
}
