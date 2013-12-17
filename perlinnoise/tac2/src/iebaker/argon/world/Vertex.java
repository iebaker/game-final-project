package iebaker.argon.world;

import java.util.HashMap;
import java.util.ArrayList;

/**
 * Vertex is a class which represents a single Vertex in the Graph.  A vertex
 * is aware of its decorations, and of the edges of which it is a part.
 */
public class Vertex {

	/**
	 * A Map from String->String representing decorations added to this vertex
	 */
	private java.util.Map<String, String> decorations = new HashMap<String, String>();

	/**
	 * A List of all the edges incident on this vertex
	 */ 
	private java.util.List<Edge> my_in_edges = new ArrayList<Edge>();

	/** 
	 * A List of all the edges leaving this vertex
	 */
	private java.util.List<Edge> my_out_edges = new ArrayList<Edge>();

	/**
	 * Whether or not the node is active 
	 */
	private boolean active = true;

	/**
	 * Cosntuctor.  Creates a new Vertex.
	 *
	 * @param id 	The id of the newly created Vertex.
	 */
	public Vertex(String id) {
		decorations.put("ID", id);
	}


	/**
	 * Constructor.  Creates a new Vertex with decorations specified by d
	 *
	 * @param id 	The id of the newly created Vertex.
	 * @param d 	The decorations to add to the newly created Vertex
	 */
	public Vertex(String id, Decoration d) {
		decorations.put("ID", id);
		this.decorate(d);
	}


	/**
	 * Adds a decoration to this Vertex specified by a Decoration object.  Keys which are already mapped
	 * to values will be overwritten.
	 *
	 * @param d 	The decoration object which holds decorations for this vertex
	 */
	public void decorate(Decoration d) {
		decorations.putAll(d.entries());
	}


	/**
	 * Adds a single decoration to this Vertex specified by a string which will be the key, and a string
	 * which whill be the value indexed by that key.  A key which is already mapped to a value will be 
	 * overwritten.
	 *
	 * @param key 		The string to use as the key
	 * @param value 	The String to use as the value
	 */
	public void decorate(String key, String value) {
		decorations.put(key, value);
	}


	/**
	 * Removes the decoration indexed by a specific string, and returns the element which was previously 
	 * indexed there.
	 *
	 * @param key 		The key to remove from the decorations
	 * @return 			The value which was stored at that key
	 */
	public String strip(String key) {
		return decorations.remove(key);
	}


	/**
	 * Returns the decoration associated with a specific string
	 *
	 * @param key 		The key which is associated with the desired decoration
	 * @return 			The value associated with the desired key
	 */
	public String decoration(String key) {
		return decorations.get(key);
	}


	/**
	 * Returns a list of the incoming edges to this vertex
	 *
	 * @return 		A list of all vertexs whose head is this vertex
	 */
	public java.util.List<Edge> getInEdges() {
		return my_in_edges;
	}


	/**
	 * Returns a list of all outgoing edges from this vertex
	 *
	 * @return 		a List of all vertexs whose tail is this vertex
	 */
	public java.util.List<Edge> getOutEdges() {
		return my_out_edges;

	}


	/**
	 * Adds an edge leading into this vertex
	 *
	 * @param e 	The edge to be added
	 */
	public void addInEdge(Edge e) {
		my_in_edges.add(e);
	}


	/**
	 * Adds an edge leading away from this vertex
	 *
	 * @param e 	The edge to be added
	 */
	public void addOutEdge(Edge e) {
		my_out_edges.add(e);
	}

	public void setActive(boolean a) {
		active = a;
		for(Edge e : my_in_edges) {
			e.setActive(a);
		}
		for(Edge e : my_out_edges) {
			e.setActive(a);
		}
	}

	public boolean isActive() {
		return active;
	}

	public String toString() {
		return "[VERTEX " + decoration("ID") + "]";
	}

	//Vertices are ONLY equal if they are references to the same object
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vertex) {
			Vertex v = (Vertex) o;
			return (this.decoration("x").equals(v.decoration("x")) && this.decoration("y").equals(v.decoration("y")));
		}
		return false;
	}
} 