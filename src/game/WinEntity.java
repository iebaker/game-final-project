package game;

import java.io.Serializable;

import engine.entity.Entity;
import engine.entity.StaticEntity;

public class WinEntity extends StaticEntity implements Serializable {
	
	private static final long	serialVersionUID	= -5591721651849308791L;
	
	public WinEntity() {
		super();
	}
	
	@Override
	public void afterCollision(Entity other) {
		this.world.setWin();
	}
}
