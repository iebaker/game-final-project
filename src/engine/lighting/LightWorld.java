package engine.lighting;

import cs195n.Vec2f;
import java.util.List;

public interface LightWorld {
	public List<LightSource> getLightSources();
	public List<Vec2fPair> getPointsAndPairs(Vec2f sourcePoint, List<Vec2f> points);
	public Vec2f getWorldSize();
}