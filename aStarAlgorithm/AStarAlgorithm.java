package aStarAlgorithm;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

public class AStarAlgorithm {
	
	/** 
	 * Unfinished, just a theoretical implementation. 
	 * Check out checkersNode-folder for working implementation
	 * of A*-algorithm.
	 **/
	
	ArrayList<Node> OPEN;
	ArrayList<Node> CLOSED;
	Node n0;
	boolean foundSolution;
	
	public AStarAlgorithm() {
		// Initializing open and close queues for storage of nodes.
		OPEN = new ArrayList<Node>();
		CLOSED = new ArrayList<Node>();
		foundSolution = false;
		// Creating new initial node, n0, for start state.
		n0 = new Node();
		// Set g to be zero.
		n0.setG(0);
		// Performing heuristicCalculation for initial node
		heuristicCalculation(n0);
		// f is equal to g + h.
		n0.setF(n0.getG() + n0.getH());
		// Adding initial node to OPEN-queue.
		OPEN.add(n0);
	}
	
	public Solution bestFirstSearch() throws FailException {
		Node x;
		// Agenda loop
		while(!foundSolution) {
			// Check if OPEN is empty, we have a error if it is.
			if(OPEN.isEmpty()) {
				throw new FailException("OPEN-list is empty and no solution is found.");
			}
			// Taking out next node in the list.
			x = OPEN.remove(0);
			// Tag the node as visited. isOpen = false
			x.isVisited();
			// Add X node to CLOSED queue.
			CLOSED.add(x);
			
			if(isSolution(x)) {
				return new Solution(x, Succeed.SUCCEED);
			}
			ArrayList<Node> succ = generateAllSuccessors(x);
			for(Node s: succ) {
				for(Node y : OPEN) {
					if(y.state == s.state) {
						s = y;
					}
				}
				for(Node w : CLOSED) {
					if(w.state == s.state) {
						s = w;
					}
				}
				x.setChild(s);
				if((!OPEN.contains(s)) && (!CLOSED.contains(s))) {
					attachAndEval(s,x);
					OPEN.add(s);
					Collections.sort(OPEN);
				}
			}
			
		}
		
	}
	
	private ArrayList<Node> generateAllSuccessors(Node n) {
		ArrayList<Node> elements = new ArrayList<Node>();
		elements.add(n);
		return elements;
	}
	
	
	private boolean isSolution(Node x) {
		// TODO Auto-generated method stub
		return false;
	}


	public void attachAndEval(Node c, Node p) {
		c.setParent(p);
		c.setG(p.getG() + arcCost(p,c));
		c.setH(heuristicCalculation(c));
		c.setF(c.getG() + c.getH());
		
	}
	
	public void propagatePathImprovements(Node p) {
		// For each children of p
		for(Node c : p.children) {
			// If condition is true, then update g for all following nodes
			// by recursively call all children.
			if (p.getG() + arcCost(p,c) < c.getG()) {
				c.setParent(p);
				c.setG(p.getG() + arcCost(p,c));
				c.setF(c.getG() + heuristicCalculation(c));
				propagatePathImprovements(c);
			}
		}
		
	}
	
	
	public double heuristicCalculation(Node n) {
		return 1;
	}
	
	public double arcCost(Node p, Node c) {
		return 0.2;
	}
	
	
	
	
	// FailException class
	class FailException extends Exception {
		public FailException() {}
		
		public FailException(String message) {
			super(message);
		}
		
	}
	
	class Solution {
		Node sol;
		Succeed suc;
		
		public Solution(Node sol, Succeed suc) {
			this.sol = sol;
			this.suc = suc;
		}
		
	}

}
