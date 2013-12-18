package iebaker.krypton.world.continuous;

public interface Shape {
	public boolean checkCollision(Shape s);
	public boolean checkCircleCollision(Circle c);
	public boolean checkAABCollision(AAB a);
	public boolean checkPolygonCollision(Polygon p);
}
