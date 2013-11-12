package iebaker.xenon.slice;

import iebaker.xenon.core.*;
import cs195n.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Node is a class representing a tree-based layout structure.  It
 * exposes functionality to split nodes in various ways to create children.  The Node class is the basis of the 
 * Slice layout system.  To create a new node from an application context, use the constructor which takes an 
 * application, a screen, and a String ID.  The node, however, needs instruction on how to build itself, so you 
 * must also override the build() method of node in place<br>
 * <blockquote>
 *	<code>
 * 	Node foo = new Node(parent_application, containing_screen, "foo") { <br>
 *	<blockquote>
 *		public void build(Vec2i newSize) {<br>
 *			...set up node layout building... <br>
 * 		} <br>
 *	</blockquote>
 *	}
 *  </code>
 * </blockquote>
 * More instructions on exactly how to USE the build() method can be found in the description of the Slice class
 */
public class Node {

	/**
	 * The map from String to Nodes which represents the children of this node
	 */
	private java.util.Map<String, Node> my_children = new HashMap<String, Node>();

	/**
	 * A list of the elements which are owned by this Node
	 */
	private java.util.List<SliceElement> my_elements = new ArrayList<SliceElement>();

	/**
	 * The padding values of this node
	 */
	private float[] my_padding = new float[]{0, 0, 0, 0};

	/**
	 * The Gravity value of this node (which side BLOCKS style children will fall to)
	 */
	private Slice.G my_gravity = Slice.G.NONE;

	/**
	 * The parent node of this node
	 */
	private Node my_node = null;

	/**
	 * The containing screen of this node
	 */
	protected Screen my_screen = null;

	/**
	 * The String ID by which this node is indexed
	 */
	public String attrNodeID;

	/**
	 * The size (width and height) of this node
	 */
	public Vec2f attrSize;

	/**
	 * The location of this node on screen
	 */
	public Vec2f attrLocation;


	/**
	 * Constructor.  This constructor should be used to create the root node in the tree as
	 * it pulls its size information from its parent application and sets its position to be
	 * (0,0) by default.
	 *
	 * @param owner 	The parent application
	 * @param id 		The NodeID of this Node
	 */
	public Node(Application owner, Screen s, String id) {
		attrNodeID = id;
		attrSize = new Vec2f(owner.getSize());
		my_screen = s;
		attrLocation = new Vec2f(0, 0);
	}


	/**
	 * Constructor.  This constructor is used mostly by the division methods to produce nodes which
	 * have a specific location and size.
	 *
	 * @param id 		The NodeID of this Node
	 * @param par  		The parent Node of this node
	 * @param loc  		A vec2f representing the location of this Node
	 * @param siz 		A Vec2f representing the dimensions of this LaoyutNode
	 */
	public Node(String id, Screen s, Node par, Vec2f loc, Vec2f siz) {
		my_node = par;
		my_screen = s;
		attrNodeID = id;
		attrLocation = loc;
		attrSize = siz;
	}


	/**
	 * Function to manually set the parent node
	 *
	 * @param p 	the Node which will be the parent node
	 */
	public void setParentNode(Node p) {
		my_node = p;
	}


	/**
	 * Setter for the node Slice.G
	 *
	 * @param g 	The desired Slice.G
	 */
	public void setSlice(Slice.G g) {
		my_gravity = g;
	}


	/**
	 * Setter for the node Padding, which sets all padding values to one float
	 *
	 * @param p 	The desired padding for all sides
	 */
	public void setPadding(float p) {
		my_padding = new float[]{p, p, p, p};
	}


	/**
	 * Setter for the node padding which provides finer control such that paddings may be set to different values
	 * on different sides of the node
	 *
	 * @param left 		the left padding
	 * @param right  	the right padding
	 * @param top  		the top padding
	 * @param bottom 	the bottom padding
	 */
	public void setPadding(float left, float top, float right, float bottom) {
		my_padding = new float[]{left, top, right, bottom};
	}


	/**
	 * Getter for the parent node of this Node
	 *
	 * @return 		a Node object which is the parent of this Node
	 */
	public Node getParentNode() {
		return my_node;
	}


	/**
	 * Removes all children of this layoutNode by setting its map to a new HashMap instance
	 */
	public void clearChildren() {
		my_children = new HashMap<String, Node>();
	}


	/**
	 * Getter for the map of children
	 *
	 * @return 		A reference to the map from node IDs to child nodes for this Node
	 */
	public java.util.Map<String, Node> getChildrenMap() {
		return my_children;
	}


	/**
	 * Retrieval method for a single child node indexed by a list of Strings which represent the path through the
	 * Node tree to that child.  Will throw a ChildNotFoundException if there is no child found along that path at
	 * any point.
	 *
	 * @param args 		The path to the desired child node
	 * @return 			the Node child which was requested
	 */
	public Node getChild(String... args) throws ChildNotFoundException {
		
		//Start at this node
		Node current = this;
		
		//Loop through arguments
		for(String arg : args) {
			
			//If there is a child indexed by arg, go to it
			if(current.getChildrenMap().containsKey(arg)) {
				current = current.getChildrenMap().get(arg);
			} 

			//Otherwise, throw exception
			else {
				throw new ChildNotFoundException("Could not find child at that path");
			}
		}

		//Return the node found
		return current;
	}


	/**
	 * Retrieval method for a group of children.  Complicated.  Will write a better explanation later.
	 *
	 * @param args 		The tree-path to the desired children nodes.
	 * @return 			The group of children nodes found
	 */
	public java.util.List<Node> getChildren(String... args) throws ChildNotFoundException {
		java.util.List<Node> result = new ArrayList<Node>();
		
		//Remember this twice.
		Node current = this;
		Node caller = this;

		//Loop through args
		for(String arg : args) {
			
			//If the arg is an empty string, collect all the children (this doesn't work right now... not sure why)
			if(arg.equals("")) {
				for(String k : current.getChildrenMap().keySet()) {
					Node temp = current.getChildrenMap().get(k);
					if(!result.contains(temp)) {
						result.add(temp);
					}
				}
			}

			//If there is a chidld node of current with the name arg, then move through to it without adding any nodes 
			//to the result
			else if(current.getChildrenMap().containsKey(arg)) {
				current = current.getChildrenMap().get(arg);
			}


			//If there is no child with that name, move up until a node is found who has a child with that name, adding current.
			else  {
				Node p = current.getParentNode();
				for( ; p != null && p != caller.getParentNode(); p = p.getParentNode()) {
					if(p.getChildrenMap().containsKey(arg)) {
						if(!result.contains(current)) {
							result.add(current);
						}
						current = p.getChildrenMap().get(arg);
						break;
					}
				}
				if(p == null) {
					throw new ChildNotFoundException("Could not find child at that path");
				}
			}
		}

		//Always add the last argument
		if(!result.contains(current)) {
			result.add(current);
		}

		return result;
	}


	/**
	 * Adds Widgets to this node.  They will be given the same location and width as this node.
	 *
	 * @param elems 	The SliceElements to be added
	 */
	public void attach(SliceElement... elems) {
		for(SliceElement se : elems) {
			se.setAttrLocation(new Vec2f(this.attrLocation.x + my_padding[0], this.attrLocation.y + my_padding[1]));
			se.setAttrSize(new Vec2f(this.attrSize.x, this.attrSize.y));
			my_elements.add(se);
		}
	}


	/**
	 * Makes all children of children of this node direct children of this node.  AKA it does what you think 
	 * flatten() should do.  The names of the new nodes created are assigned by appending the IDs of nodes
	 * along the path to that node (separated by an input string).
	 *
	 * @param s 	A string used to separate the appended names of the children 
	 */
	public void flatten(String s) {
		java.util.Map<String, Node> temp = new HashMap<String, Node>();
		flattenHelper(0, this, "", s, temp);
		my_children = temp;
	}


	/**
	 * Helper method which recursively collects children and assigns them names.
	 *
	 * @param hack 		A controller to make sure something only happens the first time.  
	 * @param g 		The node on which flatten was called
	 * @param acc 		An accumulator to hold the appended name of the node
	 * @param fill 		a filler string to be placed between appended names
	 * @param map 		The hashMap to be populated with the new children.  Method is void, but mutates map.
	 */
	private void flattenHelper(int hack, Node g, String acc, String fill, java.util.Map<String, Node> map) {
		if(g.getChildrenMap().isEmpty()) {
			map.put(acc, g);
		} else {
			for(String k : g.getChildrenMap().keySet()) {
				if(hack != 0) {
					flattenHelper(1, g.getChildrenMap().get(k), acc + fill + k, fill, map);
				} else {
					flattenHelper(1, g.getChildrenMap().get(k), acc + k, fill, map);
				}
			}
		}
	}

	/* -------------------------------------- */
	/* DIVISION METHODS                       */
	/* -------------------------------------- */


	/**
	 * Divides nodes
	 *
	 * @param slice 	the slice to be performed
	 */
	public java.util.List<Node> divide(Slice slice) {
		switch(slice.type) {
			case BASIC:
				return divideBasic(slice.direction, slice.prefix, slice.splits, slice.names, slice.padding);
			case EVENLY:
				return divideEvenly(slice.direction, slice.prefix, slice.names, slice.number, slice.padding);
			case FLOATING:
				return divideFloating(slice.direction, slice.minPadding, slice.sizes, slice.prefix, slice.names);
			case BLOCK:
				return divideBlocks(slice.direction, slice.sizes, slice.prefix, slice.names, slice.padding);
		}
		return new ArrayList<Node>();
	}

	/**
	 * Basic division method to create child nodes either horizontally or vertically which split the parent node according to 
	 * specific floating point cutoffs.
	 *
	 * @param d 		The direction in which to perform the split
	 * @param prefix 	An optional prefix to add to the names of all nodes
	 * @param splits  	The locations at which to divide the parent node
	 * @param names  	An optional list of names to give the nodes (if not provided, they are assigned numerically)
	 * @param p 		An optional padding to place between the newly created nodes.
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	public java.util.List<Node> divideBasic(Slice.D d, String prefix, Float[] splits, String[] names, Float p) {
		d = (d == null) ? Slice.D.VERT : d;
		prefix = (prefix == null) ? "" : prefix;
		splits = (splits == null) ? new Float[]{0.5f} : splits;
		names = (names == null) ? makeNames(prefix, splits.length + 1) : makeNames(prefix, names);
		p = (p == null) ? 0 : p;
		return standardDivide(d, splits, names, p);
	}


	/**
	 * Division method which is given either a number of nodes to make, or a list of names (both doesn't really make sense...)
	 * and creates child nodes which split the direction of division evenly.
	 *
	 * @param d 		The direction in which to perform the split
	 * @param prefix	An optional prefix to add to the names of all nodes
	 * @param names  	An optional list of names to give the nodes (if not provided, they are assigned numerically)
	 * @param n 		An integer number of nodes to create.
	 * @param p 		An optional padding to place between the newly created nodes
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	public java.util.List<Node> divideEvenly(Slice.D d, String prefix, String[] names, Integer n, Float p) {
		d = (d == null) ? Slice.D.VERT : d;
		prefix = (prefix == null) ? "" : prefix;
		if(names == null && n == null) {
			return new ArrayList<Node>(); //This is meaningless
		}
		n = (n == null) ? names.length : n;
		names = (names == null) ? first(n, makeNames(prefix, n)) : first(n, makeNames(prefix, names));
		p = (p == null) ? 0 : p;
		return standardDivide(d, makeArray(n-1, 1/(float)n), names, p);	
	}


	/**
	 * Division method which is given a set of sizes of nodes to create and distributes them evenly among the space available
	 * in that direction.
	 *
	 * @param d 		The direction in which to perform the division
	 * @param minP		An optional minimum padding between the nodes created
	 * @param sizes 	The sizes of the nodes to be created
	 * @param prefix 	An optional prefix to give to the names of the nodes created
	 * @param names 	A list of names for the nodes (if not provided, they are assigned numerically)
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	public java.util.List<Node> divideFloating(Slice.D d, Float minP, Float[] sizes, String prefix, String[] names) {
		d = (d == null) ? Slice.D.VERT : d;
		minP = (minP == null) ? 0 : minP;
		if(sizes == null) {
			return new ArrayList<Node>(); //This is meaningless
		}
		prefix = (prefix == null) ? "" : prefix;
		names = (names == null) ? makeNames(prefix, sizes.length) : makeNames(prefix, names);
		return standardFloating(false, d, sizes, names, computePadding(d, sizes, minP));	
	}


	/**
	 * Division method which is given a set of sizes of nodes to create, and slides them to one side of the space available
	 *
	 * @param d 	 	The direction in which to perform the division
	 * @param sizes 	A list of sizes of nodes to create
	 * @param prefix 	An optional prefix to give to the names of all child nodes
	 * @param names 	A list of names to give the noes (if not provided, they are assigned numerically)
	 * @param p 		An optional padding to put between the nodes
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	public java.util.List<Node> divideBlocks(Slice.D d, Float[] sizes, String prefix, String[] names, Float p) {
		d = (d == null) ? Slice.D.VERT : d;
		if(sizes == null) {
			return new ArrayList<Node>(); //This is meaningless
		}
		prefix = (prefix == null) ? "" : prefix;
		names = (names == null) ? makeNames(prefix, sizes.length) : makeNames(prefix, names);
		p = (p == null) ? 0 : p;
		return standardFloating(true, d, sizes, names, p);		
	}

	/* -------------------------------------- */
	/* DIVISION HELPER METHODS (private)      */
	/* -------------------------------------- */


	/**
	 * Computes the amount of padding to place between nodes such that they are evenly spaced.
	 * This is used by dividefloating to make nodes space out evenly.
	 *
	 * @param d 		The direction of the split
	 * @param sizes 	The sizes of the child nodes
	 * @float minP 		The minimum padding allowed by this split
	 * @return 			a float representing the computed padding
	 */
	private float computePadding(Slice.D d, Float[] sizes, float minP) {
		float acc = 0;
		for(float size : sizes) {
			acc += size;
		}

		float useful_height = attrSize.y - my_padding[1] - my_padding[3];
		float useful_width = attrSize.x - my_padding[0] - my_padding[2];
		float leftover = 0;

		switch(d) {
			case VERT:
				leftover = useful_height - acc;
				break;

			case HORZ:
				leftover = useful_width - acc;
				break;
		}

		float ans = leftover/(float)(sizes.length + 1);

		return ans > minP ? ans : minP;
	}


	/**
	 * Utility method to make an array of a desired length composed only of one value
	 *
	 * @param length 	The length of the desired array
	 * @param value 	The value which will be every element (copied)
	 * @return 			An array whose length is length and whose elements are all value.
	 */ 
	private Float[] makeArray(int length, Float value) {
		Float[] result = new Float[length];
		for(int i = 0; i < length; ++i) {
			result[i] = new Float(value); //copy! lolreferences.
		}
		return result;
	}


	/**
	 * Utility method which returns an array of length n composed of the first n elements of an input array,
	 * or the entire input array if n is greater than that array's length
	 *
	 * @param n 		The length of the desired output array
	 * @param array 	The array to take from
	 * @return 			The new array computed
	 */
	private String[] first(Integer n, String[] array) {
		String[] result = n > array.length ? new String[array.length] : new String[n];
		for(int i = 0; i < result.length; ++i) {
			result[i] = array[i];
		}
		return result;
	}


	/**
	 * Utility method which will construct a list of names by appending a prefix to the numbers 0 ... number
	 *
	 * @param prefix 		The prefix to append
	 * @param number 		The number of names desired
	 * @return 				A list of string names computed
	 */
	private String[] makeNames(String prefix, int number) {
		String[] result = new String[number];
		for(int i = 0; i < number; ++i) {
			result[i] = prefix + i;
		}
		return result;
	}


	/**
	 * Utility method similar to the one above which constructs a list of names by appending a prefix to the
	 * submitted list of names
	 *
	 * @param prefix  		The string prefix to add to every name
	 * @param names 		The existing list of names
	 * @return 	 			A list of string names computed
	 */
	private String[] makeNames(String prefix, String[] names) {
		String[] result = new String[names.length];
		for(int i = 0; i < names.length; ++i) {
			result[i] = prefix + names[i];
		}
		return result;
	}


	/**
	 * Method which propagates divide calls to the appropriate specific division methods
	 *
	 * @param d 		The direction in which to perform the division
	 * @param splits 	The places at which splits must be placed
	 * @param names 	The list of names to give the nodes
	 * @param p 		A padding to place between the nodes
	 * @return 			A list of all child nodes created
	 */
	private java.util.List<Node> standardDivide(Slice.D d, Float[] splits, String[] names, float p) {
		switch(d) {
			case VERT:
				return verticalDivide(splits, names, p);
			case HORZ:
				return horizontalDivide(splits, names, p);
		}
		return new ArrayList<Node>();
	}


	/**
	 * Method which propagates floating/block division calls to the appropriate specific floating/block methods
	 *
	 * @param justify 		Whether to slide all blocks to one side
	 * @param d 			The direction in which to perform the division
	 * @param sizes 		The sizes of the child nodes to be created
	 * @param names  	 	The names to give the children nodes
	 * @param p 			An optional padding to place between the nodes created
	 * @return 				A list of all child nodes created
	 */
	private java.util.List<Node> standardFloating(boolean justify, Slice.D d, Float[] sizes, String[] names, float p) {
		switch(d) {
			case VERT:
				return verticalFloating(justify, sizes, names, p);
			case HORZ:
				return horizontalFloating(justify, sizes, names, p);
		}
		return new ArrayList<Node>();
	}


	/**
	 * Method which actually performs vertical division!
	 *
	 * @param splits 		The places to put divisions
	 * @param names 		Names to give the child nodes
	 * @param p 			Padding to place between the nodes
	 * @return 				a list of all child nodes created by this call.
	 */
	private java.util.List<Node> verticalDivide(Float[] splits, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();

		//Accumulator (sort of)
		float leftover = 1;

		//These will be incremented as we move down the division
		float current_y = attrLocation.y + my_padding[1];
		float current_x = attrLocation.x + my_padding[0];

		//The actual space available to use
		float useful_height = attrSize.y - my_padding[1] - my_padding[3] - (splits.length * p);
		float useful_width = attrSize.x - my_padding[0] - my_padding[2]; 

		//Loop through, creating and adding child nodes
		for(int index = 0; index < names.length - 1; ++index) {
			Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width, useful_height * splits[index]));
			return_value.add(temp);
			my_children.put(names[index], temp);
			current_y += (useful_height * splits[index] + p);
			leftover -= splits[index];
		}

		//Create and add the last node
		Node temp = new Node(names[names.length - 1], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width, useful_height * leftover));
		return_value.add(temp);
		my_children.put(names[names.length - 1], temp);

		//Return the ndoes created
		return return_value;
	}


	/**
	 * Method which actually performs horizontal division!
	 *
	 * @param splits 		The places to put the divisions
	 * @param names 		The names to give the child nodes
	 * @param p 			Padding to place between the nodes
	 * @return 	 			A list of all child nodes created by this call.
	 */
	private java.util.List<Node> horizontalDivide(Float[] splits, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();

		float leftover = 1;
		float current_y = attrLocation.y + my_padding[1];
		float current_x = attrLocation.x + my_padding[0];
		float useful_height = attrSize.y - my_padding[1] - my_padding[3];
		float useful_width = attrSize.x - my_padding[0] - my_padding[2] - (splits.length * p);

		for(int index = 0; index < names.length - 1; ++index) {
			Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width * splits[index], useful_height));
			return_value.add(temp);
			my_children.put(names[index], temp);

			current_x += (useful_width * splits[index] + p);
			leftover -= splits[index];
		}

		Node temp = new Node(names[names.length - 1], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width * leftover, useful_height));
		return_value.add(temp);
		my_children.put(names[names.length - 1], temp);

		return return_value;
	}


	/**
	 * Method which actually performs vertical floating division!
	 *
	 * @param justify 		Whether to slide all child nodes to one side
	 * @param sizes 		The sizes of the children nodes to be created
	 * @param names 		The names to give the nodes
	 * @param p 			The padding to place between the ndoes
	 * @return 				A list of all child nodes created by this call
	 */
	private java.util.List<Node> verticalFloating(boolean justify, Float[] sizes, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();

		float current_x = attrLocation.x + my_padding[1];
		float current_y;
		float useful_height = attrSize.y - my_padding[1] - my_padding[3];
		float useful_width = attrSize.x - my_padding[0] - my_padding[2];

		if(justify) {
			if(my_gravity == Slice.G.SOUTH) {
				current_y = attrLocation.y + attrSize.y - my_padding[3];
				for(int index = sizes.length - 1; index >= 0; --index) {
					Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y - sizes[index]), new Vec2f(useful_width, sizes[index]));
					return_value.add(temp);
					my_children.put(names[index], temp);
					current_y -= (sizes[index] + p);
				}
			} else {
				current_y = attrLocation.y + my_padding[1];
				for(int index = 0; index < sizes.length; ++index) {
					Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width, sizes[index]));
					return_value.add(temp);
					my_children.put(names[index], temp);
					current_y += (sizes[index] + p);
				}
			}
		} else {
			current_y = attrLocation.y + my_padding[1] + p;
			for(int index = 0; index < sizes.length; ++index) {
				Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(useful_width, sizes[index]));
				return_value.add(temp);
				my_children.put(names[index], temp);
				current_y += (sizes[index] + p);
			}
		}

		return return_value;
	}


	/**
	 * Method which actually performs horizontal floating division!
	 *
	 * @param justify 		Whether to slide all child nodes to one side
	 * @param sizes 		The sizes of the children nodes to be created
	 * @param names 		The names to give the nodes
	 * @param p 			The padding to place between the modes
	 * @return 				A list of all child nodes created by this call
	 */
	private java.util.List<Node> horizontalFloating(boolean justify, Float[] sizes, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();

		float current_x;
		float current_y = attrLocation.y + my_padding[0];
		float useful_height = attrSize.y - my_padding[1] - my_padding[3];
		float useful_width = attrSize.x - my_padding[0] - my_padding[2];

		if(justify) {
			if(my_gravity == Slice.G.EAST) {
				current_x = attrLocation.x + attrSize.x - my_padding[2];
				for(int index = sizes.length - 1; index >= 0; --index) {
					Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x - sizes[index], current_y), new Vec2f(sizes[index], useful_height));
					return_value.add(temp);
					my_children.put(names[index], temp);
					current_x -= (sizes[index] + p);
				}
			} else {
				current_x = attrLocation.x + my_padding[0];
				for(int index = 0; index < sizes.length; ++index) {
					Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(sizes[index], useful_height));
					return_value.add(temp);
					my_children.put(names[index], temp);
					current_x -= (sizes[index] + p);
				}
			}
		} else {
			current_x = attrLocation.x + my_padding[0] + p;
			for(int index = 0; index < sizes.length; ++index) {
				Node temp = new Node(names[index], my_screen, this, new Vec2f(current_x, current_y), new Vec2f(sizes[index], useful_height));
				return_value.add(temp);
				my_children.put(names[index], temp);
				current_x += (sizes[index] + p);
			}
		}

		return return_value;
	}

	/**
	 * Should be overridden by implementations
	 */
	public void build(Vec2i newSize) {
		attrSize = new Vec2f(newSize);
	}
}


