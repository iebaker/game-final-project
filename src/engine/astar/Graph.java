package engine.astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Graph class, supporting A* pathfinding
 * 
 * @author dgattey
 * 
 * @param <U>
 */
public class Graph<U extends Node<U>> {
	
	protected List<U>	nodes;
	
	/**
	 * Empty constructor
	 */
	public Graph() {
		this.nodes = new ArrayList<U>();
	}
	
	/**
	 * Adds a node to the current list
	 * 
	 * @param node
	 *            A Node to add
	 */
	protected void addNode(U node) {
		nodes.add(node);
	}
	
	/**
	 * Carries out the A* algorithm for all Nodes in the list
	 * 
	 * @param s
	 *            The destination node
	 * @param c
	 *            The comparator to use
	 */
	protected void doAStar(U s, Comparator<U> c) {
		PriorityQueue<U> pq = new PriorityQueue<U>(20, c);
		for (U n : nodes) {
			if (n != s) {
				n.distance = Integer.MAX_VALUE;
			}
		}
		s.distance = 0;
		pq.add(s);
		while (!pq.isEmpty()) {
			U u = pq.remove();
			for (Edge<U> edge : u.edges) {
				U v = edge.target;
				if (v.distance > u.distance + edge.weight) {
					v.distance = u.distance + edge.weight;
					v.parent = u;
					pq.add(v);
				}
			}
		}
	}
	
	/**
	 * Creates a linked list of the shortest path from s to d by carrying out A* for the graph Returns a linked list,
	 * where the first element is the destination node
	 * 
	 * @param s
	 * @param d
	 * @param c
	 * @return
	 */
	public LinkedList<U> findShortestPath(U s, U d, Comparator<U> c) {
		doAStar(s, c);
		U temp = d;
		LinkedList<U> path = new LinkedList<U>();
		while (temp != s) {
			path.addLast(temp);
			if (temp == null) return new LinkedList<U>();
			temp = temp.parent;
		}
		path.addLast(temp);
		return path;
	}
	
}
