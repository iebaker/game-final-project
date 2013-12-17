package iebaker.argon.world;

public interface Grid {
	public boolean isRealPlace(Place p);
	public Entity addEntity(Place p, Entity e);
	public Entity removeEntity(Place p);
	public Entity getEntity(Place p);
	public java.util.List<Place> getEntityPlaces();
	public java.util.List<Entity> getAllEntities();
	public void addPlannedPlace(Place p);
	public void clearPlannedPlaces();
	public java.util.List<Place> getPlannedPlaces();
	public int getXSize();
	public int getYSize();
	public java.util.List<Entity> getNeighbors(Place p);
	public java.util.List<Place> getAdjacentPlaces(Place p);
	public java.util.List<Place> getEmptyAdjacentPlaces(Place p);
	public java.util.List<Place> getOccupiedAdjacentPlaces(Place p);
}