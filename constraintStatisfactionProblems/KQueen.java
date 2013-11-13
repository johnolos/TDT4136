package constraintStatisfactionProblems;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import simulatedAnnealing.Egg;

public class KQueen {
	
	private int board[][];
	private Random rnd;
	public final static int MAXIMUM_STEPS = 10000000;
	private long startTime;
	
	public KQueen(int n) {
		this.board = new int[n][n];
		rnd = new Random();
	}
	
	
	void initiateBoardWithQueens() {
		for(int i = 0; i < this.board.length; i++) {
			this.board[i][rnd.nextInt(this.board.length)] = 1;
		}
	}
	
	
	void MIN_CONFLICTS() {
		// Storing the time when the solution began calculating
		this.startTime = System.currentTimeMillis();
		// Storing the number of steps it took.
		int steps = 0;
		// Variables used.
		int y, queen, currentConflicts, otherConflicts;
		// The loop
		while(steps != MAXIMUM_STEPS) {
			// Get a random row
			y = rnd.nextInt(this.board.length);
			// Find the queen on the given row.
			queen = getQueenIndex(y);
			// Get number of conflicts for that position.
			currentConflicts = this.getConflicts(y, queen);
			// Find the position with lowest conflicts on that row.
			ArrayList<Integer> min = new ArrayList<Integer>();
			min.add(queen);
			for(int x = 0; x < this.board.length; x++) {
				// Don't evaluate the same spot as we did earlier.
				if(x == queen) {
					++steps;
					continue;
				}
				// Get number of conflicts in the index x.
				otherConflicts = this.getConflicts(y, x);
				// If otherConflicts are same as currentConflicts. Add to our list.
				if(otherConflicts > currentConflicts) {
					++steps;
					continue;
				} else if(currentConflicts == otherConflicts) {
					min.add(x);
				} else if(currentConflicts > otherConflicts) {
					min.clear();
					currentConflicts = otherConflicts;
					min.add(x);
				}
			}			
			int nextQueen = min.get(rnd.nextInt(min.size()));
			this.put(y, queen, 0);
			this.put(y, nextQueen, 1);
			// Prevent resources being drained.
			if(steps % this.board.length == 1) {
				if(isValid()) {
					break;
				}
			}
			++steps;
		}
//		this.printBoard();
		if(isValid()) {
			System.out.println("Solution is valid.");
		} else {
			System.out.println("Solution is not valid.");
		}
//		System.out.println("Used " + (System.currentTimeMillis() - this.startTime) + " milliseconds.");
	}
	
	
	/**
	 * Used when the the currently solution is not solveable.
	 * The function takes a random queen in a random row and places
	 * the queen from that row in another random column.
	 */
	private void randomSwitch() {
		int y = rnd.nextInt(this.board.length);
		int x = rnd.nextInt(this.board.length);
		int z = this.getQueenIndex(y);
		if(x == z) {
			randomSwitch();
			return;
		}
		put(y,z,0);
		put(y,x,1);	
	}

	/**
	 * Checks the current state of the solution if it is a solution
	 * without conflicts. Where a conflict is a voilation of the rules.
	 * @return Boolean value
	 */
	public boolean isValid() {
		int x, numOfConflicts = 0;
		for(int y = 0; y < this.board.length; y++) {
			x = this.getQueenIndex(y);
			numOfConflicts += this.getConflicts(y, x);
			if(numOfConflicts > 0) {
				return false;
			}
		}
		return numOfConflicts == 0;
	}


	/**
	 * Returns the column index of where the queen is in the row given.
	 * @param y Index y in the row.
	 * @return Returns column index or -1 if there's no queen in the row.
	 */
	private int getQueenIndex(int y) {
		for(int i = 0; i < this.board.length; i++) {
			if(board[y][i] == 1) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * The function get all conflicts in a position. It does not check
	 * if there's a queen in the position given.
	 * @param y Row index.
	 * @param x Column index.
	 * @return Returns amount of conflicts.
	 */
	int getConflicts(int y, int x) {
		return (this.diagonalEvaluation(y, x) + 
				this.diagonalEvaluation2(y, x) + 
				this.verticalEvaluation(y, x));
	}

	/**
	 * Evaluate number of conflicts in \ - direction.
	 * @param y Row index.
	 * @param x Column index.
	 * @return Returns number of conflicts.
	 */
	int diagonalEvaluation(int y, int x) {
            // Check from left-top to bottom-right.
            int d = 0 - x;
            int numOfConflicts = 0;
            while(d + x < this.board.length) {
                    if(((y + d < 0) || (y + d >= this.board.length)) ||( y + d == y && x+d == x)) {
                            ++d;
                            continue;
                    }
                    if(board[y + d][x + d] == 1)
                            ++numOfConflicts;
                    ++d;
            }
            return numOfConflicts;   
    }
	
	
	/**
	 * Evaluate number of conflicts in / - direction.
	 * @param y Row index.
	 * @param x Column index.
	 * @return Returns number of conflicts.
	 */
    int diagonalEvaluation2(int y, int x) {
            // Check from the bottom-left to right-top.
            int d = this.board.length - y - 1;
            int numOfConflicts = 0;
            while(y + d >= 0) {
                    if(x - d < 0 || x - d >= this.board.length || (y + d == y && x - d == x)) {
                            --d;
                            continue;
                    }
                    if(board[y+d][x-d] == 1)
                            ++numOfConflicts;
                    --d;
                   
            }
            return numOfConflicts;
    }
    
    
	/**
	 * Evaluate number of conflicts in  vertical direction.
	 * @param y Row index.
	 * @param x Column index.
	 * @return Returns number of conflicts.
	 */
	int verticalEvaluation(int y, int x) {
		int numOfConflicts = 0;
		for(int i = 0; i < this.board.length; i++) {
			if(i == y)
				continue;
			if(board[i][x] == 1)
				++numOfConflicts;
		}
		return numOfConflicts;
	}
    
	/**
	 * Assigns a value to a given coordinate within the board.
	 * @param y Row index.
	 * @param x Column index.
	 * @param value Assigned value
	 */
    void put(int y, int x, int value) {
    	this.board[y][x] = value;
    }
      
    /**
     * Print function that prints out the current state of a solution.
     */
	void printBoard() {
		String line;
		for(int y = 0; y < this.board.length; y++) {
			line = "|";
			for(int x = 0; x < this.board.length; x++) {
				if(this.board[y][x] == 1) {
					line += "X";
				} else {
					line += " ";
				}
				line += "|";
			}
			System.out.println(line);
		}
	}

}
