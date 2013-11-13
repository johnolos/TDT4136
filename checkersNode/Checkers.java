package checkersNode;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Checkers{

	private ArrayList<CheckersNode> OPEN;
	private ArrayList<CheckersNode> CLOSED;
	private CheckersNode root;
	private int[] goal;
	private HashMap<String,CheckersNode> nodes;
	private ArrayList<CheckersNode> solutions;
	private int nodesGenerated;
	private int depthOfSolution;
	private SearchType type;
	
	
	public Checkers(int k, SearchType type){
		this.type = type;
		this.goal = new int[k+1];
		int[] state = new int[k+1]; 
		
		//create startState
		int i =0 ;
		while (i < k/2){
			state[i] = 1; //1 represent black 
			state[i+k/2+1] = 2; //2 represent red
			i++;
		}
		for (int j = 0; j<state.length;j++){
			goal[j] = state[state.length-1-j]; //mirror of startState
		}
		// Initiate empty sets of OPEN and CLOSED
		this.CLOSED = new ArrayList<CheckersNode>();
		this.nodes = new HashMap<String,CheckersNode>();
		this.OPEN = new ArrayList<CheckersNode>();
		// Create root node where to being search.
		this.root =  new CheckersNode(state,null);
		System.out.println(root.getKey());
		this.solutions = new ArrayList<CheckersNode>();
		// Initiate variable to keep track of entered nodes.
		nodesGenerated = 0;
		// Add root node to OPEN set.
		OPEN.add(root);
		agendaLoop();
	}
	
	public void agendaLoop() {
		if (root == null) {
			// Throw FailException here.
		}
		CheckersNode result;
		do {
			result = processBestNode();
		} 
		while (result == null);
		
		this.solutions.add(result);
		//When the AgnendaLoop is finished we have a solution.
		
		this.depthOfSolution = setNodeCounter(result);
		
		System.out.println(result.getKey() + " result");
		System.out.println("Result: (" + this.nodesGenerated + "," + this.depthOfSolution + ")");
		
	}
	
	public CheckersNode processBestNode() {
		CheckersNode node;
		node = OPEN.remove(OPEN.size()-1);
		CLOSED.add(node);
		if (node == null)
			return null;
		if (isNodeSolution(node)){
			return node;
		}
		else {
			expandNode(node);
			
		}
		
		return null;
	}
		
	public boolean isNodeSolution(CheckersNode node) {
		for (int i = 0; i < goal.length; i++)
			if (goal[i] != node.board[i])
				return false;
		return true;
	}
	
	private void expandNode(CheckersNode node) {
		node.generateChildren();
		this.nodesGenerated++;
		
		for (CheckersNode n: node.children){
			heuristicCalculation(n);
			considerAddingNode(n, node);
		
		}
		// Sorting the OPEN-set only if its bestFirstSearch.
		if(this.type == SearchType.BESTFIRSTSEARCH) {
			Collections.sort(OPEN);
		}
		
		
	}



	private boolean considerAddingNode(CheckersNode node, CheckersNode parent) {
		// Check if we have created the same node before
		if (!nodes.containsKey(node.getKey())) {
			nodes.put(node.getKey(), node);
			if(this.type == SearchType.DEPTHFIRSTSEARCH)
				// ARCHIVING DEPTH-FIRST-SEARCH BY SETTING NEW NODES ON TOP OF STACK
				this.OPEN.add(node);
			else
				this.OPEN.add(0,node);
			heuristicCalculation(node);
			
			return true;
		}
		// Node is created before. Perhaps this one is a more suitable parent.
		if (node.considerUpdatingParent(parent)){
			//nodesExpanded--;
		}
		return false;
		
	}

	public void heuristicCalculation(CheckersNode node) {
		int red = 0;
		int black = 0; 
		double d = 0.0;
		int[] temp = node.board.clone();
		
		int l = temp.length;

		for (int i = 0; i < temp.length;i++) {
			if (temp[i]==1) {
				red++;
				d += l/2 -i + red;
				}
			if (temp[i]==2) {
				black++;
				d+= i-black +1;
				}
		}
		node.h = d;
	}
	
	public int setNodeCounter(CheckersNode node){
		int c = 0;
		CheckersNode newNode = node;
		while (newNode.parent != null){
			newNode = newNode.parent;
			
			// Printing out the solution backwards.
			System.out.println(newNode.getKey());
			c++;
		}
		return c;
	}
	

}
