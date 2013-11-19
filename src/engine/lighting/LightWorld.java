package engine.lighting;

import cs195n.Vec2f;
import java.util.List;

public interface LightWorld {
	public List<LightSource> getLightSources();
	public List<Vec2f> getPoints(Vec2f sourcePoint);
	public List<Vec2fPair> getPointPairs(Vec2f sourcePoint);
}