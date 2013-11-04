package smt3.gameengine.ai;

public interface Heuristic {

	int getHeuristic(Node curr, Node target);
}
