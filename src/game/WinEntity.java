package game;

import engine.entity.StaticEntity;

public class WinEntity extends StaticEntity {
	
	public WinEntity() {
		super();
	}
	
	@Override
	public void afterCollision() {
		this.world.setWin();
	}
}
