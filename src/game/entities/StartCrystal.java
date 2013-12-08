package game.entities;

import game.BackgroundLight;
import game.GameWorld;


public class StartCrystal extends BackgroundLight {

	private static final long serialVersionUID = 5666381145803773762L;
	private boolean set = false;
	
	public StartCrystal() {
		super();
	}
	
	@Override
	public void onTick(float secs) {
		if(set == false && world != null) {
			((GameWorld) this.world).addStartCrystal(this);
		}
		super.onTick(secs);
	}
}
