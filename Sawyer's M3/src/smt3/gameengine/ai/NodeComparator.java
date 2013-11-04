package smt3.gameengine.ai;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	public NodeComparator() {
		// TODO Auto-generated constructor stub
	}

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
