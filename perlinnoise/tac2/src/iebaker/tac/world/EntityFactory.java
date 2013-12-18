package iebaker.tac.world;

import iebaker.argon.world.Entity;
import java.lang.Class;

public class EntityFactory {
	public static Entity spawn(Class<?> c) {
		if(c.equals(StickSoldier.class)) {
			return new StickSoldier();
		} else if(c.equals(StickBuilder.class)) {
			return new StickBuilder();
		} else if(c.equals(StoneBuilder.class)) {
			return new StoneBuilder();
		} else if(c.equals(StoneSoldier.class)) {
			return new StoneSoldier();
		}
		return new Sticks();
	}
}