package iebaker.argon.slice;

/**
 * The class Slice specifically represents a <em>way</em> to divide a node into sub nodes by placing vertical or horizontal
 * slices through this node, partitioning it into smaller regions of screen real-estate which are linked to the progenitor node
 * in a child-parent relation. A single Slice object can be thought of like a programmable cookie-cutter for Node objects.  Idiomatic
 * use can utilize double-brace initialization when passing a slice object to a divide call, for example
 * <blockquote>
 * <code>
 * my_example_node.divide(new Slice() 
 * <blockquote>
 * 		{{ type(T.BASIC); direction(D.VERT); splits(0.5f, 0.25f); padding(10f); names("top", "middle", "bottom"); }}  
 * </blockquote>
 * );</code>
 * </blockquote>
 * The above code divides <code>my_example_node</code> into three sections: top, which is 50% of the total height, middle, which
 * is 25% of the total height, and bottom which is the leftover (also 25%).  There is a 10 pixel padding between each child node,
 * the axis divided was the vertical one, and the type is basic.  The nodes are given names in order from top down: "top", 
 * "middle", and "bottom"  Other types can split a node evenly into N child nodes, or
 * divide the nodes into blocks of fixed pixel heights which either space themselves evenly through the splitting dimension of 
 * the parent node, or slide to one side according to the Gravity of the parent node.
 */
public class Slice {
	/**
	 * An enumerated type listing the possible ways to divide a node (BASIC: enumerated by percentages of the parent height
	 * at which to place the divisions, EVENLY: enumerated by the number of divisions -- will split the space available
	 * evenly, FLOATING: enumerated by a series of fixed widths/heights for the blocks -- will cause these blocks to evenly
	 * fill the space available, BLOCK: enumerated by a series of fixed widths/heights for the blocks -- will cause these blocks
	 * to slide to one side of the parent node, the direction determined by the parent node's Gravity)
	 */
	public enum T { BASIC, EVENLY, FLOATING, BLOCK }

	/**
	 * An enumerated type representing the dimensions (vertical and horizontal) which can be divided by Slicing nodes
	 */
	public enum D { VERT, HORZ }

	/**
	 * An enumerated type listing the directions in which a node's gravity can pull BLOCK style child nodes
	 */
	public enum G { NONE, NORTH, SOUTH, EAST, WEST }


	/**
	 * The type of slicing which will be performed
	 */
	public T type = null;

	/**
	 * The direction of the axis along which splits will be taken
	 */
	public D direction = null;

	/**
	 * A padding which is placed between child nodes
	 */
	public Float padding = null;

	/**
	 * Places to locate splits, as percentages of the parent node's width/height
	 */ 
	public Float[] splits = null;

	/**
	 * Names assigned to child nodes.
	 */
	public String[] names = null;

	/**
	 * The number of child nodes to create
	 */
	public Integer number = null;

	/**
	 * The sizes of child nodes to create
	 */
	public Float[] sizes = null;

	/**
	 * A prefix to add to all the names of the child nodes
	 */
	public String prefix = null;

	/**
	 * The minimum padding to place between nodes
	 */
	public Float minPadding = null;

	/**
	 * Sets the type of the Slice
	 *
	 * @param t 	The type of the slice
	 */
	public void type(T t) {	type = t; }

	/**
	 * Sets the direction of the slice
	 *
	 * @param d 	The direction of the slice
	 */
	public void direction(D d) { direction = d; }

	/**
	 * Sets the splits to be taken from the parent
	 *
	 * @param s 	The splits to be taken from the parent
	 */
	public void splits(Float... s) { splits = s; }

	/**
	 * Sets the names to call the child nodes
	 *
	 * @param n 	The names to call the child nodes
	 */ 
	public void names(String... n) { names = n; }

	/**
	 * Sets the number of child nodes to create
	 *
	 * @param n 	The numer of child nodes to create
	 */
	public void number(Integer n) { number = n; }

	/**
	 * Sets the sizes of child nodes to create, as raw float values
	 *
	 * @param s 	The sizes of child nodes to create
	 */
	public void sizes(Float... s) { sizes = s; }

	/**
	 * Sets the padding to be placed between child nodes
	 *
	 * @param p the padding to place between child ndoes
	 */
	public void padding(Float p) { padding = p; }

	/**
	 * Sets a prefix to be added to the names of all child nodes
	 *
	 * @param p 	A prefix to add to the names of all nodes
	 */
	public void prefix(String p) { prefix = p; }

	/**
	 * Sets a minimum value on the padding between child nodes
	 *
	 * @param m 	The min padding
	 */
	public void minPadding(Float m) { minPadding = m; }
}