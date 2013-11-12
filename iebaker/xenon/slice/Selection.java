package iebaker.xenon.slice;

import java.util.ArrayList;

/**
 * Selection provides functionality for performing Node operations on groups of
 * nodes.  Nodes can be added to a selection and manipulated with the division and padding
 * methods.
 */
public class Selection {
	/**
	 * All the nodes in this selection
	 */
	private java.util.List<Node> selected_nodes = new ArrayList<Node>();

	/**
	 * Selects nodes, if they are not already in the selection
	 *
	 * @param tbs  	Nodes to be selected
	 * @return 		Any nodes which were newly added to the selection
	 */
	public java.util.List<Node> select(java.util.List<Node> tbs) {
		java.util.List<Node> return_value = new ArrayList<Node>();
		for(Node node : tbs) {
			if(!selected_nodes.contains(node)) {
				selected_nodes.add(node);
				return_value.add(node);
			}
		}
		return return_value;
	}


	/**
	 * Selects nodes, clearing the selection beforehand.
	 *
	 * @param tbs  		Nodes to be selected
	 * @return 			Redundantly, the newly selected nodes (which must be all of them)
	 */
	public java.util.List<Node> reselect(java.util.List<Node> tbs) {
		selected_nodes.clear();
		return select(tbs);
	}


	/**
	 * Removes nodes from the selection.
	 *
	 * @param tbs 		Nodes to be removed from the selection
	 */
	public void unselect(java.util.List<Node> tbs) {
		for(Node node : tbs) {
			if(selected_nodes.contains(node)) {
				selected_nodes.remove(node);
			}
		}
	}


	/**
	 * Returns the list of selected nodes
	 *
	 * @return 		All nodes currently in the selection, as a java.util.List
	 */
	public java.util.List<Node> getSelection() {
		return selected_nodes;
	}


	/**
	 * Containment checker 
	 *
	 * @param node 		The node in question
	 * @return 			Whether the selection currently contains that node
	 */ 
	public boolean contains(Node node) {
		return selected_nodes.contains(node);
	}


	/**
	 * Retrieval method for a child of the nodes in the selection.  Returns the first child it finds 
	 * with the specfied path, or throws a ChildNotFoundException if no child can be found
	 *
	 * @param args 		The path to the child node
	 * @return 			the child node found first with that path
	 * @throws 			ChildNotFoundException
	 */
	public Node getChild(String... args) throws ChildNotFoundException {
		Node child = null;
		for(Node node : selected_nodes) {
			try {
				child = node.getChild(args);
			} catch (ChildNotFoundException e) {}
		}
		if(child == null) {
			throw new ChildNotFoundException("No child found with that path");
		}
		return child;
	}


	/**
	 * Retrieval method for a group of children of the ndoes in the selection.  Returns any child that matches
	 * the path from any of the nodes in the selection, or throws a ChildNotFoundException if no child can be found
	 *
	 * @param args 		The path to the children nodes 
	 * @return 			Any nodes which match this path from a node in the selection
	 * @throws 			ChildNotFoundException
	 */ 
	public java.util.List<Node> getChildren(String... args) throws ChildNotFoundException {
		java.util.List<Node> children = new ArrayList<Node>();
		for(Node node : selected_nodes) {
			try {
				children.addAll(node.getChildren(args));
			} catch (ChildNotFoundException e) {}
		}
		if(children.isEmpty()) {
			throw new ChildNotFoundException("No children found with that path");
		}
		return children;
	}

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
	 * Runs basic division on all nodes in the selection.
	 *
	 * @param d 		The direction in which to perform the split
	 * @param prefix 	An optional prefix to add to the names of all nodes
	 * @param splits  	The locations at which to divide the parent node
	 * @param names  	An optional list of names to give the nodes (if not provided, they are assigned numerically)
	 * @param p 		An optional padding to place between the newly created nodes.
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	private java.util.List<Node> divideBasic(Slice.D d, String prefix, Float[] splits, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();
		for(Node node : selected_nodes) {
			return_value.addAll(node.divideBasic(d, prefix, splits, names, p));
		}
		return return_value;
	}


	/**
	 * Runs even division on all nodes in the selection
	 *
	 * @param d 		The direction in which to perform the split
	 * @param prefix	An optional prefix to add to the names of all nodes
	 * @param names  	An optional list of names to give the nodes (if not provided, they are assigned numerically)
	 * @param n 		An integer number of nodes to create.
	 * @param p 		An optional padding to place between the newly created nodes
	 * @return 			A java.util.List of all child nodes created by this call
	 */
	private java.util.List<Node> divideEvenly(Slice.D d, String prefix, String[] names, int n, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();
		for(Node node : selected_nodes) {
			return_value.addAll(node.divideEvenly(d, prefix, names, n, p));
		}
		return return_value;
	}


	/**
	 * Runs floating division on all the nodes in the selection
	 *
	 * @param d 		The direction in which to perform the division
	 * @param minP		An optional minimum padding between the nodes created
	 * @param sizes 	The sizes of the nodes to be created
	 * @param prefix 	An optional prefix to give to the names of the nodes created
	 * @param names 	A list of names for the nodes (if not provided, they are assigned numerically)
	 * @return 			A java.util.List of all child nodes created by this call
	 */ 
	private java.util.List<Node> divideFloating(Slice.D d, float minP, Float[] sizes, String prefix, String[] names) {
		java.util.List<Node> return_value = new ArrayList<Node>();
		for(Node node : selected_nodes) {
			return_value.addAll(node.divideFloating(d, minP, sizes, prefix, names));
		}
		return return_value;
	}


	/**
	 * Runs blcok division on all the nodes in the selection
	 *
	 * @param d 	 	The direction in which to perform the division
	 * @param sizes 	A list of sizes of nodes to create
	 * @param prefix 	An optional prefix to give to the names of all child nodes
	 * @param names 	A list of names to give the noes (if not provided, they are assigned numerically)
	 * @param p 		An optional padding to put between the nodes
	 * @return 			A java.util.List of all child nodes created by this call 
	 */
	private java.util.List<Node> divideBlocks(Slice.D d, Float[] sizes, String prefix, String[] names, float p) {
		java.util.List<Node> return_value = new ArrayList<Node>();
		for(Node node : selected_nodes) {
			return_value.addAll(node.divideBlocks(d, sizes, prefix, names, p));
		}
		return return_value;
	}


	/** 
	 * Sets the padding on all the nodes in the selection (to different values)
	 *
	 * @param left 		The left padding
	 * @param top	 	The top padding
	 * @param right  	the right padding
	 * @param bottom 	the bottom padding
	 */
	public void setPadding(float left, float top, float right, float bottom) {
		for(Node node : selected_nodes) {
			node.setPadding(left, top, right, bottom);
		}
	}


	/**
	 * Sets the padding on all the nodes in the selection (to the same value)
	 *
	 * @param p 		The value to set all padding to
	 */
	public void setPadding(float p) {
		for(Node node : selected_nodes) {
			node.setPadding(p);
		}
	}
}