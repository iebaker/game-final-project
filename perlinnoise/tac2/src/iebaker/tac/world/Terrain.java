package iebaker.tac.world;

import iebaker.argon.world.Place;

public class Terrain {
	public static final String DEEP_WATER = "DEEP_WATER";
	public static final String SHALLOW_WATER = "SHALLOW_WATER";
	public static final String DIRT = "DIRT";
	public static final String GRASS = "GRASS";
	public static final String SAND = "SAND";

	public static String getTerrainType(float terrain_value) {
		if(terrain_value < -0.8) return DEEP_WATER;
		if(terrain_value < -0.1) return SHALLOW_WATER;
		if(terrain_value < 0.33) return SAND;
		if(terrain_value < 0.66) return DIRT;
		return GRASS;
	}

	public static String getTerrainType(Place p) {
		try {
			float terrain_value = Float.parseFloat(p.decoration("TERRAIN_VALUE"));
			return Terrain.getTerrainType(terrain_value);
		} catch (NumberFormatException e) {
			System.err.println("Could not parse terrain type from place");
			return "";
		}
	}
}