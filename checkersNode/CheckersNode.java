package checkersNode;

import java.util.ArrayList;

public class CheckersNode implements Comparable<CheckersNode> {

	// This is the state variable
	public int[] board;
	private double f, g;
	double h; 
	// Status
	boolean isOpen;
	// Best suitable parent at this moment
	CheckersNode parent;
	// Children of this node
	ArrayList<CheckersNode> children;
	
	
	public CheckersNode(int[] currentState , CheckersNode parent){
		// Initiate state and parent when created
		this.board = currentState;
		this.parent = parent;
		if (this.parent != null){
			this.g = this.parent.g + arcCost(parent);
		} else
			this.g = 0;
	}
	

	
	public String getKey() {
		String key = "";
		for (int i : board){
			key+= i;
		}
		return key;
	}

	public void generateChildren() {
		if (this.children != null){
			return;
		}
		this.children = new ArrayList<CheckersNode>();
		// Finds the position of free space (0)
		int position = getNullPosition();
		
		// Checks what children is can be generated based on position in list.
		if (position > 1 && position < board.length-2) {
			this.children.add(new CheckersNode(moveLeft(position),this));
			this.children.add(new CheckersNode(jumpLeft(position),this));
			this.children.add(new CheckersNode(moveRight(position),this));
			this.children.add(new CheckersNode(jumpRight(position),this));
			return;
		}
		if (position == 1){
			this.children.add(new CheckersNode(moveLeft(position),this));
			this.children.add(new CheckersNode(jumpLeft(position),this));
			this.children.add(new CheckersNode(moveRight(position),this));
			return;
		}
		if (position == 0){
			this.children.add(new CheckersNode(moveLeft(position),this));
			this.children.add(new CheckersNode(jumpLeft(position),this));
			return;
		}
		if (position == board.length-2){
			this.children.add(new CheckersNode(moveLeft(position),this));
			this.children.add(new CheckersNode(moveRight(position),this));
			this.children.add(new CheckersNode(jumpRight(position),this));
			return;
		}
		if (position == board.length-1){
			this.children.add(new CheckersNode(moveRight(position),this));
			this.children.add(new CheckersNode(jumpRight(position),this));
			return;
		}
	}


	public int[] jumpRight(int position) {
		int[] nb = this.board.clone();
		nb[position] = this.board[position-2];
		nb[position-2] = 0;
		return nb;
	}


	public int[] moveRight(int position) {
		int[] nb = this.board.clone();
		nb[position] = this.board[position-1];
		nb[position-1] = 0;
		return nb;
	}


	public int[] jumpLeft(int position) {
		int[] nb = this.board.clone();
		nb[position] = this.board[position+2];
		nb[position+2] = 0;
		return nb;
	}


	public int[] moveLeft(int position) {
		int[] nb = this.board.clone();
		nb[position] = this.board[position+1];
		nb[position+1] = 0;
		return nb;
		
	}


	public int getNullPosition() {
		// Finds where the open space is
		int empty = 0;
		for (int i: board) {
			if (i == 0){
				return empty;
				}
			empty++;
		}
		return -1;
	}

	public boolean considerUpdatingParent(CheckersNode newParent){
		// Is new parent better suitable then older one?
		if ( this.parent.g > newParent.g){
			// Update parent
			this.parent = newParent;
			this.updateG();
			
			return true;
		}
		return false;
	}
	
	
	public void updateG() {
		// Update g for this node, the node's children and so forth.
		this.g = this.parent.g + arcCost(this.parent); 
		if (this.children == null)
			return;
		for (CheckersNode n : children){
			n.updateG();
		}
	}
	
	
	public double arcCost(CheckersNode otherNode) {
		return 0.1;
	}
	
	public double getf() {
		return this.h + this.g;
	}
	
	// This is where the comparison happens when we run Collections.sort. 
	public int compareTo(CheckersNode arg0) {
		if (this.getf() == arg0.getf())
			return 0;
		return (this.getf() > arg0.getf() ? -1 : 1);
	}


	
}
