package iebaker.argon.world;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Graph is a class which represents a general mathematical graph which is composed of vertices and edges.
 * Vertices and edges can both be decorated with string key->value pairs which are used by traversal
 * algorithms.  Graphs should be created with double-brace initialization as:
 * <p>
 * <code>
 * Graph g = new Graph() {{ <br>
 *	<blockquote>
 *	vertices("A", "B", "C"); <br>
 * 	edges("A:B, "B::C", "C:D"); <br>
 * </blockquote>
 * }}
 * </code>
 * </p>
 * <p>
 * The above code creates a graph with three vertices, named "A", "B", and "C".  It then adds edges A to B, B to C, C to B, and
 * C to D.  The idiom "V1:V2" in edges() creates a directed edge from V1 to V2 if V1 and V2 are both the names of nodes.  The
 * idiom "V1::V2" in edges() creates an undirected edge (represented as two edges, one from V1 to V2, and one from V2 to V1). 
 * Syntactically incorrect strings, and string which refer to nonexistent vertices will be disregarded and no edges will
 * be created by that argument.  Directed graph jargon (vertex, edge, head, tail, successor, predecessor) are taken from the
 * article <a href='http://en.wikipedia.org/wiki/Directed_graph'>Directed Graph</a>, and I have attempted to faithfully implement the
 * concepts therein. </p>
 * <p>
 * A Graph is not restricted to be simple, or connected.  A vertex may be connected to itself, and an edge may exist
 * twice. </p>
 */
public class Graph {

	/**
	 * A List storing all of the vertices of the graph
	 */
	protected java.util.List<Vertex> my_vertices = new ArrayList<Vertex>();

	/**
	 * A List storing all the edges of the graph
	 */
	protected java.util.List<Edge> my_edges = new ArrayList<Edge>();

	/**
	 * A Hashmap storing the vertices of the graph by their "ID" decoration
	 */
	protected java.util.Map<String, Vertex> my_vertex_map = new HashMap<String, Vertex>();

	/**
	 * A Hashmap storing the vertices of the graph by the "ID" decorations of their constituent vertices
	 */
	protected java.util.Map<String, Edge> my_edge_map = new HashMap<String, Edge>();

//public Graph() {
//	System.out.println("GRAPH CREATED");
//}
	/**
	 * Adds vertices to the graph.  Mainly for use in double-brace initialization.  Accepts a list of string of a format as
	 * follows:
	 * <p>
	 * <code>
	 * nodename | 'key1'='value1'; 'key2'='value2' </code>
	 * </p>
	 * This will create a vertex whose id is nodename, and who is decorated with the pairs ('key1', 'value1'), ('key2', 'value2').
	 * Parameters are not necessary, and bare strings of node names can be passed also, which will create undecorated nodes.
	 * Improperly formatted arguments are ignored and trigger error messages to be printed on standard error.
	 *
	 * @param vnames  	The list of node name/parameters to be created
	 */
	public void vertices(String... vnames) {
		Pattern p = Pattern.compile("^\\s*'(.*)'\\s*=\\s*'(.*)'\\s*$");
		for(String vname : vnames) {
			if(vname != null && !vname.equals("")) {
				String[] halves = vname.split("\\s*\\|\\s*");
				String id = "";
				String param = "";

				//If there are no parameters, just add the name!
				if(halves.length == 1) {
					id = halves[0];
					this.addVertex(new Vertex(id));
				} 

				//If there are parameters, collect and add them
				else if(halves.length == 2) {
					id = halves[0];				//Add the vertex
					this.addVertex(new Vertex(id));

					param = halves[1];			//Break the parameters apart around " ; "
					String[] pieces = param.split("\\s*;\\s*");

					//Loop through, decorating with parameters
					for(String piece : pieces) {
						Matcher m = p.matcher(piece);
						if(m.matches()) {
							this.getVertex(id).decorate(m.group(1), m.group(2));
						} else {
							System.err.println("Improperly formatted vertex paratmeter");
						}
					}
				} 

				//This will happen if there
				else {
					System.err.println("Improperly formatted vertex statement.. must contain 0 or 1 '|' characters");
				}
			} 

			//Improperly formatted vertex statement
			else {
				System.err.println("Improperly formatted vertex statement (general)");
			}
		}
	}

	/**
	 * Adds edges to the graph.  Mainly for use in double-brace initialization.  Accepts a list of string of format
	 * as follows;
	 * <p>
	 * <code>
	 *	vertex1:vertex2 | 'key1'='value1'; 'key2'='value2' <br>
	 *	vertex3::vertex4 | 'key3'='value3'
	 * </code>
	 * </p>
	 * The first half creates the edge itself.  Directed edges are created by single colons, and point from the first listed
	 * vertex to the second listed vertex.  Undirected edges (represented as one directed edge in both directions) are created
	 * by a double colon.  Parameters are on the right, and function as they do in arguments to vertex().
	 *
	 * @param enames  	The instructions to the edge creation function
	 */
	public void edges(String... enames) {
		Pattern p = Pattern.compile("^\\s*'(.*)'\\s*=\\s*'(.*)'\\s*");

		//Loop through args
		for(String ename : enames) {
			String[] halves = ename.split("\\s*\\|\\s*");

			String edge_half = "";
			String param_half = "";
			Edge edge1 = null;
			Edge edge2 = null;
			boolean success = false;

			//Deal with adding the edges themselves
			if(halves.length >= 1) {
				edge_half = halves[0];	//Get the proper half

				Pattern bothways = Pattern.compile("^(.+)::(.+)$");		//Make a pattern to find out if it's a 2 way edge
				Matcher m = bothways.matcher(edge_half);				//Match against it
				String[] vertices = edge_half.split(":+");				//Split into node names

				//If there aren't two nodes, we're fucked
				if(vertices.length != 2) {
					System.err.println("Failed to add edges!");
				}
				
				//Add the edge in one direction, and if the string matched the two-edge pattern, add the edge
				//The other way
				Vertex first = getVertex(vertices[0]);
				Vertex second = getVertex(vertices[1]);
				if(first != null && second != null) {
					edge1 = new Edge(getVertex(vertices[0]), getVertex(vertices[1]));
					this.addEdge(edge1);
					if(m.matches()) {
						edge2 = new Edge(getVertex(vertices[1]), getVertex(vertices[0]));
						this.addEdge(edge2);
					}
				}
				success = true;			//SOMETHING worked
			}

			//If there are parameters to decorate the edge with
			if(halves.length == 2) {
				String param = halves[1];
				String[] pieces = param.split("\\s*;\\s*");

				//For each kv pair
				for(String piece : pieces) {
					Matcher m = p.matcher(piece);
					if(m.matches()) {
						if(edge1 !=  null) edge1.decorate(m.group(1), m.group(2));
						if(edge2 !=  null) edge2.decorate(m.group(1), m.group(2));
					} else {
						System.err.println("Improperly formatted decorations");
					}
				}
				success = success && true;   //SOMETHING worked again
			}

			if(!success) {
				System.err.println("Failed to add edges!");
			}
		}
	}

	/**
	 * Finds a path from one vertex to a goal vertex using the A* seach algorithm.  If a path is impossible,
	 * will return null.
	 *
	 * @param start 	The vertex at which to begin
	 * @param end 		The goal vertex
	 * @param m 		A metric which maps from edges to edge weights
	 * @param h 		A heuristic which scores the distance from a start to an end node
	 * @return 			A java.util.List which represents a path from the start node to the end node.
	 */
	public java.util.List<Vertex> aStarPath(Vertex start, Vertex end, Metric m, Heuristic h) {
		//System.out.println("Pathfinding");
		java.util.Set<Vertex> visited = new HashSet<Vertex>();

		//Create the fringe with the proper comparator, and add the 
		PriorityQueue<Vertex> fringe = new PriorityQueue<Vertex>(1, new Comparator<Vertex>() {
			@Override public int compare(Vertex v1, Vertex v2) {
				Float p1 = Float.parseFloat(v1.decoration("A_STAR_F"));
				Float p2 = Float.parseFloat(v2.decoration("A_STAR_F"));
				return p1.compareTo(p2);
			}
		}) {
			@Override	//This is to insure that a vertex can't be added to the fringe twice, since Java doesn't have a PriorityHashSet :P
			public boolean add(Vertex v) {
				for(Vertex member : this) {
					if(v == member) {
						return false;
					}
				} 
				return super.add(v);
			}
		};
		start.decorate("A_STAR_F", "0");
		start.decorate("A_STAR_G", h.score(start, end) + "");
		fringe.add(start);

		//int n = 0;
		while(!fringe.isEmpty()) {
			//++n;
			//System.out.println("Solving... iteration #" + n);
			//for(Vertex v : fringe) {
			//	System.out.print(v);
			//}
			//System.out.println();
			//Get the best node from the fringe
			Vertex current = fringe.peek();	

			//If we're at the end, return the backtrace
			if(current == end) {
				java.util.List<Vertex> path = new ArrayList<Vertex>();
				path.add(current);
				while(current != start) {
					Vertex prev = getVertex(current.decoration("A_STAR_PREVIOUS"));
					path.add(0, prev);
					current = prev;
				}
				//for(Vertex v : path) {
				//	System.out.print(v + ", ");
				//}
				//System.out.println("Solved");
				java.util.List<Vertex> touched = new ArrayList<Vertex>();
				touched.addAll(visited);
				touched.addAll(fringe);
				touched.add(current);
				for(Vertex v : touched) {
					v.strip("A_STAR_F");
					v.strip("A_STAR_PREVIOUS");
					v.strip("A_STAR_G");
				}
				return path;
			}

			//Otherwise, note that we've visited current
			fringe.poll();
			visited.add(current);

			//Loop through the outgoing edges
			for(Edge edge : current.getOutEdges()) {
				if(edge.isActive()) {
					//Find which node succeeds us along that edge
					Vertex successor = edge.getHead();

					float g_value = Float.parseFloat(current.decoration("A_STAR_G"));

					float temp_score = g_value + m.measure(edge);
					if(visited.contains(successor) && temp_score >= g_value) {
						continue;
					}

					if(!visited.contains(successor) || temp_score < g_value) {
						successor.decorate("A_STAR_PREVIOUS", current.decoration("ID"));
						successor.decorate("A_STAR_G", g_value + "");
						successor.decorate("A_STAR_F", (g_value + h.score(current, end)) + "");
						fringe.add(successor);
					}
				}
			}
		}
		return new ArrayList<Vertex>();
	}


	/**
	 * Adds a vertex to the graph
	 *
	 * @param v 	The vertex to be added
	 */
	public void addVertex(Vertex vertex) {
		my_vertices.add(vertex);
		my_vertex_map.put(vertex.decoration("ID"), vertex);
	}


	/**
	 * Gets the vertex indexed by a specified ID
	 *
	 * @param id 		The index of the desired vertex
	 * @return 			The vertex indexed by the String id
	 */
	public Vertex getVertex(String id) {
		return my_vertex_map.get(id);
	}


	/**
	 * Adds an edge to the graph
	 *
	 * @param e 	The edge to be added
	 */ 
	public void addEdge(Edge edge) {
		my_edges.add(edge);
		my_edge_map.put(edge.getTail().decoration("ID") + ":" + edge.getHead().decoration("ID"), edge);
		edge.getTail().addOutEdge(edge);
		edge.getHead().addInEdge(edge);
	}


	/**
	 * Gets the edge of a graph indexed by a specified ID.  
	 *
	 * @param id 		The index of the desired edge
	 * @return 			The edge indexed by the String ID
	 */
	public Edge getEdge(String id) {	
		return my_edge_map.get(id);
	}


	/**
	 * Removes a vertex from the graph indexed by a specified ID.  Also removes any edges leading to
	 * or away from this vertex.
	 *
	 * @param id 		The index of the desired vertex to be removed
	 */
	public void removeVertex(String id) {
		Vertex to_be_removed = my_vertex_map.get(id);
		for(Edge e : to_be_removed.getInEdges()) {
			my_edges.remove(e);
			my_edge_map.remove(e.decoration("ID"));
		}
		for(Edge e : to_be_removed.getOutEdges()) {
			my_edges.remove(e);
			my_edge_map.remove(e.decoration("ID"));
		}
		my_vertices.remove(to_be_removed);
		my_vertex_map.remove(id);

	}


	/**
	 * Removes an edge from the graph indexed by a specific ID.  Does not affect the vertices which constitute
	 * the end points of this edge.
	 */
	public void removeEdge(String id) {
		Edge to_be_removed = my_edge_map.get(id);
		my_edges.remove(to_be_removed);
		my_edge_map.remove(id);
	}


	/**
	 * Returns whether the Graph has an edge connecting two specified vertices in the direction passed
	 *
	 * @param v1 	The first vertex
	 * @param v2 	The second vertex
	 * @return 		<code>true</code> if the Graph contains a vertex whose tail is v1 and whose head is v2, 
	 * 				<code>false</code> otherwise
	 */
	public boolean hasEdge(Vertex v1, Vertex v2) {
		return my_edge_map.get(v1.decoration("ID") + ":" + v2.decoration("ID")) != null;
	}


	/**
	 * Returns a string representation of thie Graph object
	 *
	 * @return The graph, represented as a String
	 */
	@Override
	public String toString() {
		String result = "[[iebaker.argon.util.Graph\nVERTICES\n";
		for(Vertex v : my_vertices) {
			result += "    Vertex:" + (v.isActive() ? "[a]" : "[i]") + (v.getOutEdges().size()) + " " + v.decoration("ID") + "\n";
		}
		//result += "EDGES\n";
		//for(Edge e : my_edges) {
		//	result += "    Edge:" + (e.isActive() ? "[a]" : "[i]") + " " + e.getTail().decoration("ID") + "->" + e.getHead().decoration("ID") + "\n";
		//}
		result += "]]";
		return result;
	}
}