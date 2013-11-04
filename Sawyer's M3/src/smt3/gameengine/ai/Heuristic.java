package smt3.gameengine.ai;

/**
 * A heuristic interface.
 * @author Sawyer
 *
 */
public interface Heuristic {

	int getHeuristic(Node curr, Node target);
}
