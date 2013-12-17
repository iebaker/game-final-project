package iebaker.argon.world;

import java.util.HashMap;

/**
 * Edge is a class which represents an edge of the Graph object.  An edge is directed (undirected edges in the
 * graph are represented by a pair of Edges, one in each direction.  Edges have a tail (starting end), and a head
 * (final end).  Edges can be decorated in a manner similar to Vertices.
 */
public class Edge {

	/**
	 * A HashMap from String keys to String values representing the decorations of this node
	 */
	private java.util.Map<String, String> decorations = new HashMap<String, String>();

	/**
	 * The starting vertex of this node
	 */
	private Vertex tail;

	/**
	 * The ending vertex of this node
	 */
	private Vertex head;

	/**
	 *
	 */
	private boolean active = true;


	/** 
	 * Constructor.  Takes two vertices and constructs an edge between them.
	 *
	 * @param t 	The tail vertex
	 * @param h 	The head vertex
	 */
	public Edge(Vertex t, Vertex h) {
		tail = t;
		head = h;
	}


	/**
	 * Returns the tail vertex of the Edge
	 * 
	 * @return 	a Vertex which is the tail of the edge
	 */
	public Vertex getTail() {
		return tail;
	}


	/**
	 * Returns the head vertex of the edge
	 *
	 * @return 		a Vertex which is the head of the edge
	 */
	public Vertex getHead() {
		return head;
	}


	/**
	 * Adds a decoration to this edge
	 *
	 * @param d 	A Decoration object which should contain key->value mappings 
	 * 				which will decorate the edge
	 */
	public void decorate(Decoration d) {
		decorations.putAll(d.entries());
	}


	/**
	 * Adds a decoration to this edge specified by a specific key
	 *
	 * @param key 		The key with which to decorate this edge
	 * @param value 	The value with which to decorate this edge
	 */
	public void decorate(String key, String value) {
		decorations.put(key, value);
	}


	/**
	 * Retrieves the decoration specified in this edge by an input key
	 *
	 * @param key 	The key used to index the decoration string
	 * @return 		The String indexed by key
	 */
	public String decoration(String key) {
		return decorations.get(key);
	}


	/**
	 * Sets the edge to either active (true), or inactive (false) depending on an input boolean
	 *
	 * @param a 	The boolean to set the active field to
	 */
	public void setActive(boolean a) {
		active = a;
	}


	/**
	 * Accessor for the field active
	 */
	public boolean isActive() {
		return active;
	}


	/**
	 * Prints a useful String representation of this Edge
	 */
	public String toString() {
		return "[EDGE (" + tail + ", " + head + ")]";
	}
}