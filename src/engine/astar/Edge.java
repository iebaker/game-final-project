package engine.astar;

/**
 * Graph edge class for graph node and graph
 * 
 * @author dgattey
 * 
 * @param <U>
 */
public class Edge<U extends Node<U>> implements Comparable<Edge<U>> {
	public U	target;
	public U	source;
	public int	weight;
	
	/**
	 * Constructor
	 * 
	 * @param source
	 * @param target
	 * @param weight
	 */
	public Edge(U source, U target, int weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
	
	/**
	 * Compares two graph edges by weight
	 */
	public int compareTo(Edge<U> o) {
		return this.weight - o.weight;
	}
}