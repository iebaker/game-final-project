package iebaker.argon.world;

import java.util.HashMap;

/**
 * Decoration is a class which represents a Decoration object which can be added to a Vertex or Edge.  Proper
 * format for adding these should also use double-brace initialization as:
 * <p>
 * <code>
 * myNode.decorate(new Decoration() {{ <br>
 * <blockquote>
 *	entry("key1", "value1"); <br>
 * 	entry("key2", "value2"); <br>
 * </blockquote>
 * }});
 * </code>
 * </p>
 * This will add decorations to the node myNode which map the keys key1 and key2 to values value1 and value2.
 */
public class Decoration {
	
	/**
	 * A Map from String->String holding the decorations stored within this Decorations object
	 */
	private java.util.Map<String, String> stored_decorations = new HashMap<String, String>();


	/**
	 * Returns a Set of Map.Entry objects from String to String which represent the decorations stored in
	 * this current Decoration object.  Used by Vertex and Edge.
	 *
	 * @return 		a java.util.Set of java.util.Map from String -> String which represents all the decorations
	 * 				added to the graph so far.
	 */
	public java.util.Map<String, String> entries() {
		return stored_decorations;
	}


	/**
	 * Adds an entry to the Decoration object
	 *
	 * @param key 		the String by which to index this decoration
	 * @param value 	the String which is referred to by the passed index
	 */
	public void entry(String key, String value) {
		stored_decorations.put(key, value);
	}
}