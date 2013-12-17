package iebaker.tac.world;

import iebaker.argon.world.Entity;
import java.lang.Class;

public class EntityFactory {
	public static Entity spawn(Class<?> c) {
		if(c.equals(StickSoldier.class)) {
			return new StickSoldier();
		} else if(c.equals(StickBuilder.class)) {
			return new StickBuilder();
		}
		return new Sticks();
	}
}