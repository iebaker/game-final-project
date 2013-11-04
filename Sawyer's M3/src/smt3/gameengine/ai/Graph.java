package smt3.gameengine.ai;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Graph {

	private ArrayList<Node> _nodes;
	private Heuristic _heur;
	
	public Graph(Heuristic heur) {
		// TODO Auto-generated constructor stub
		_nodes = new ArrayList<Node>(10000);
		_heur = heur;
	}
	
	public void addNode(Node n) {
		_nodes.add(n);
	}
	
	public Stack<Node> getAStar(Node src, Node end) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(10, new NodeComparator());
		boolean found = false;
		
		for(Node n : _nodes) {
			n.setDist(99999999);
			n.setPrev(null);
			n.setHeur(_heur.getHeuristic(n, end));
		}
		
		src.setPrev(src);
		src.setDist(0);
		pq.add(src);
		
		while(!pq.isEmpty() && found == false) {
			Node currNode = pq.poll();
			if(currNode == end) {
				break;
			}
			for(Node neighbor : currNode.getNeighbors()) {
				if(neighbor.isActive()) {
					if(neighbor == end) {
						neighbor.setPrev(currNode);
						found = true;
					}
					if(neighbor.getDist() > currNode.getDist() + 1 + currNode.getHeur()) {
						neighbor.setDist(currNode.getDist() + 1 + currNode.getHeur());
						neighbor.setPrev(currNode);
						pq.add(neighbor);
					}
				}
			}
		}
		
		if(found == true) {
			Stack<Node> toReturn = new Stack<Node>();
			Node returnNode = end;
			while(returnNode.getPrev() != returnNode) {
				toReturn.add(returnNode);
				returnNode = returnNode.getPrev();
			}
			
			return toReturn;
		}
		else {
			return new Stack<Node>();
		}
		
	}
}

