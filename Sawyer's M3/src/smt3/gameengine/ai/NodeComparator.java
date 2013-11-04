package smt3.gameengine.ai;

import java.util.Comparator;

/**
 * A comparator used to find the node that is closer to a destination node.
 * @author Sawyer
 *
 */
public class NodeComparator implements Comparator<Node> {

	/**
	 * @return integer representing whether or not the first node is closer
	 */
	public int compare(Node o1, Node o2) {
		int c1 = o1.getDist() + o1.getHeur();
		int c2 = o2.getDist() + o2.getHeur();
		
		if(c1 > c2) {
			return 1;
		}
		
		if(c1 < c2) {
			return -1;
		}
		return 0;
	}

}
