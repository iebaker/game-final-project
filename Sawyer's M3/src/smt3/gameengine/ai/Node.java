package smt3.gameengine.ai;

import java.util.ArrayList;

public class Node {

	private ArrayList<Node> _neighbors;
	private int _dist;
	private Node _prev;
	private int _heur;
	private int _dec1;
	private int _dec2;
	private int _dec3;
	private int _dec4;
	private boolean _active;
	
	public Node() {
		_neighbors = new ArrayList<Node>(10);
		_active = true;
	}
	
	public void addNeighbor(Node neighbor) {
		_neighbors.add(neighbor);
	}
	
	public ArrayList<Node> getNeighbors() {
		return _neighbors;
	}

	public int getDist() {
		return _dist;
	}

	public void setDist(int _dist) {
		this._dist = _dist;
	}

	public Node getPrev() {
		return _prev;
	}

	public void setPrev(Node src) {
		this._prev = src;
	}

	public int getDec1() {
		return _dec1;
	}

	public void setDec1(int dec1) {
		_dec1 = dec1;
	}
	
	public int getDec2() {
		return _dec2;
	}

	public void setDec2(int dec2) {
		_dec2 = dec2;
	}
	
	public int getDec3() {
		return _dec3;
	}

	public void setDec3(int dec3) {
		_dec3 = dec3;
	}
	
	public int getDec4() {
		return _dec4;
	}

	public void setDec4(int dec4) {
		_dec4 = dec4;
	}

	public int getHeur() {
		return _heur;
	}

	public void setHeur(int heur) {
		_heur = heur;
	}
	
	public boolean isActive() {
		return _active;
	}
	
	public void setActive(boolean active) {
		_active = active;
	}
}