package iebaker.argon.world;

/**
 * Class heuristic represents a way to score the estimated difficulty of reaching one Vertex from
 * another.  It contains only one method, score, which can be applied to a vertex (current), and a
 * vertex (goal), and which will return a measure of the distance between those vertices based upon
 * their decorations.  Used by Graph's method aStarPath()
 */ 
public class Heuristic {

	/**
	 * Scores the estimated difficulty of getting from vertex v1 to vertex v2. By default, returns
	 * zero, but should be overridden by implementations to return more useful data
	 *
	 * @param v1 	The starting vertex
	 * @param v2 	The goal vertex
	 * @return 		A float representing the difficulty of reaching v2 starting at v1.
	 */
	public float score(Vertex v1, Vertex v2) { 
		return 0f; 
	}
}