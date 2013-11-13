package aStarAlgorithm;

import java.util.ArrayList;

public class Node implements Comparable {
	// An object describing a state of the search process
	public Object state;
	// g - cost of getting to this node
	// h - estimated cost to goal
	// f- estimated total cost of a solution path going through this node.
	double g, h, f;
	// Best parent node
	public Node parent;
	// Children of this node
	public ArrayList<Node> children;
	
	// Node has been opened or not.
	public boolean isOpen;
	
	
	public Node() {
		children = new ArrayList<Node>();
		isOpen = true;
	}
	
	public void setG(double d) {
		g = d;
	}
	
	public double getG() {
		return g;
	}
	
	public void setH(double i) {
		h = i;
	}

	public double getH() {
		return h;
	}
	
	public void setF(double i) {
		f = i;
	}
	
	public double getF() {
		return f;
	}

	public void setChild(Node child) {
		children.add(child);
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public void isVisited() {
		isOpen = false;
	}

	@Override
	public int compareTo(Object arg0) {
		return 0;
	}
}
