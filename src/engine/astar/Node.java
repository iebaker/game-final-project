package engine.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that supports creating a node for the graph
 * 
 * @author dgattey
 * 
 * @param <U>
 */
public class Node<U extends Node<U>> implements Comparable<Node<U>> {
	int				distance;
	U				value;
	U				parent;
	List<Edge<U>>	edges;
	
	/**
	 * Empty constructor
	 */
	public Node() {
		this.edges = new ArrayList<Edge<U>>();
	}
	
	/**
	 * Setter of value
	 * 
	 * @param value
	 */
	public void setValue(U value) {
		this.value = value;
	}
	
	/**
	 * Makes a new Edge given a source, target, and a weight
	 * 
	 * @param source
	 *            A node, source
	 * @param target
	 *            A node, target
	 * @param weight
	 *            A weight
	 */
	public void addEdge(U source, U target, int weight) {
		Edge<U> e = new Edge<U>(source, target, weight);
		this.edges.add(e);
	}
	
	/**
	 * Comparable method for Nodes
	 */
	public int compareTo(Node<U> o) {
		return (this.distance - o.distance);
	}
	
	/**
	 * Public getter for distance
	 * 
	 * @return The distance currently
	 */
	public int getDistance() {
		return distance;
	}
}