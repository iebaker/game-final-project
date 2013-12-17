package iebaker.argon.world;

/**
 * Class Metric represents a way of scoring an Edge object based on its decorations
 * It contains only one method, measure() which will take in an Edge and return a
 * float representing that edge's weight.  This is used by Graph's method aStarPath()
 */
public class Metric {

	/**
	 * Returns the weight of an edge.  By default, returns 0, but should be overridden
	 * by actual implementations in order to return more useful data.
	 *
	 * @param e 	The edge to be measured
	 * @return 		A float which represents the weight assigned to that edge
	 */
	public float measure(Edge e) { 
		return 0f; 
	}
}