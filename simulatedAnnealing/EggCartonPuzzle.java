package simulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public class EggCartonPuzzle extends SimulatedAnnealing {
	
	/** Temperature MAX value */
	private static final double T_MAX = 30.0;
	/** Temperature decreasing value */
	private static final double dT = 0.0002;
	/** Field's saving information about the board */
	private int rows, columns, eggs;
	/** Board represented in double-array **/
	private Egg board[][];
	/** Random generator **/
	private Random rnd;
	
	
	public EggCartonPuzzle(int row, int col, int eggs) {
		this.rows = row;
		this.columns = col;
		this.eggs = eggs;
		rnd = new Random();
		board = new Egg[this.rows][this.columns];
	}
	
	void initiateAndRandomize() {
		// 1. Initiate board with our data structure
		initiateBoard();
		// Generate a few eggs to work with.
		generateRandomSolution();
	}
	
	void run() {
		// 2. Set the temperature, T, to it's starting value.
		this.setTemperature(T_MAX);
		// 3. Evaluate state with objective function. This yields a number between 0.0 and 1.0.
		this.setEvaluation(this.ObjectiveFunction());
		agendaLoop();
	}
	
	
	@Override
	void agendaLoop() {
		// 4. If evaluation equals targeted evaluation. Return current solution.
		while(this.getEvaluation() != 1.0) {
			// 5. Generate neighbors of this state.
			ArrayList<SimulatedAnnealing> neighbors = this.generateNeighbors();
			// 7. Let P_MAX be the neighbor with heighest evaluation.
			double P_MAX = 0.0;
			int indexOfP_MAX = 0;
			for(int i = 0; i < neighbors.size(); i++) {
				if(neighbors.get(i).getEvaluation() > P_MAX) {
					P_MAX = neighbors.get(i).getEvaluation();
					indexOfP_MAX = i;
				}
			}
			// 8. Let q be...
			double q = (P_MAX - this.getEvaluation()) / this.getEvaluation();
			// 9. Let p be...
			double p = Math.min(1.0, Math.pow(Math.E, -q/this.getTemperature()));
			// X is random real number between 0.0 and 1.0
			double x = rnd.nextDouble();
			// This is changed from what I interpreted from the psudocode.
			EggCartonPuzzle newState;
			newState = (EggCartonPuzzle)neighbors.get(indexOfP_MAX);
			// New 11. If there's a better solution than what we have, change.
			if(newState.getEvaluation() > this.getEvaluation()) {
				newState = (EggCartonPuzzle)neighbors.get(indexOfP_MAX);
				this.board = newState.getBoard();
				this.setEvaluation(newState.getEvaluation());
			} else {
				// 12. Random choice among the neighbors. If x < p.
				int randomChoice = rnd.nextInt(neighbors.size());
				newState = (EggCartonPuzzle)neighbors.get(randomChoice);
				if(x < p) {
					this.setEvaluation(newState.getEvaluation());
					this.board = newState.getBoard();
				}
			}
			//13 . Set T = T - dt
			this.setTemperature(this.getTemperature() - EggCartonPuzzle.dT);
		}
		return;
	}

	/**
	 * Shall be able to differencate between horrible, mediocre, 
	 * promising, excellent and optimal solution.
	 */
	@Override
	double ObjectiveFunction() {
		// Gives 3P for correctly placed egg, and 1P for empty spaces.
		double numberOfPoints = 0.0;
		
		// Lists for storing information about where the eggs are located.
		ArrayList<Egg> eggList = new ArrayList<Egg>();
		ArrayList<Egg> emptySpotList = new ArrayList<Egg>();
		
		for(int y = 0; y < this.rows; y++) {
			for(int x = 0; x < this.columns; x++) {
				if(board[y][x].isEgg())
					eggList.add(board[y][x]);
				else
					emptySpotList.add(board[y][x]);
			}
		}
		
		for(Egg e : eggList) {
			if(this.evaluatePosition(e))
				numberOfPoints += 3;
		}
		numberOfPoints = numberOfPoints + (emptySpotList.size()*1.0);
		return numberOfPoints/getMaxPoints();
	}	

	@Override
	ArrayList<SimulatedAnnealing> generateNeighbors() {
		ArrayList<SimulatedAnnealing> neighbors = new ArrayList<SimulatedAnnealing>();
		int i = 0;
		EggCartonPuzzle element;
		while(i != 20) {
			element = this.clone();
			// One position to opposite value.
			int y = rnd.nextInt(this.rows), x = rnd.nextInt(this.columns);
			element.setEgg(y, x, !this.board[y][x].isEgg());
			// 7. Evaluate each neighbor as they are created.
			element.setEvaluation(element.ObjectiveFunction());
			neighbors.add(element);
			++i;
		}
		return neighbors;
	}
	
	/**
	 * Initiate the active board with our data structure.
	 */
	private void initiateBoard() {
		for(int y = 0; y < this.rows; y++) {
			for(int x = 0; x < this.rows; x++) {
				this.board[y][x] = new Egg(false, y, x);
			}
		}
	}
	
	
	/**
	 * Generates a random board to work with.
	 */
	private void generateRandomSolution() {
		int i = 0, x, y;
		while(i != this.eggs*this.rows) {
			y = rnd.nextInt(this.rows);
			x = rnd.nextInt(this.columns);
			if(!board[y][x].isEgg()) {
				board[y][x].setEgg(true);
				i++;
			}
		}
	}
	
	/**
	 * Change a boolean value on a given position.
	 * @param row Int y row where shall be changed.
	 * @param column Int x column where it shall be changed.
	 * @param egg Boolean value to be used.
	 */
	void setEgg(int row, int column, boolean egg) {
		this.board[row][column].setEgg(egg);
	}
	
	/**
	 * Evaluates all conditions.
	 * @param n
	 * @return
	 */
	boolean evaluatePosition(Egg n) {
		return (horizontalEvaluation(n) && verticalEvaluation(n)
				&& diagonalEvaluation(n) && diagonalEvaluation2(n));
	}
	
	/**
	 * Evaluates horizontal condition.
	 * @param n Egg
	 * @return Returns boolean if passed.
	 */
	boolean horizontalEvaluation(Egg n) {
		int y = n.getRow();
		int x = n.getCol();
		int numOfEggs = 0;
		for(int i = 0; i < this.columns; i++) {
			if(i == x)
				continue;
			if(board[y][i].isEgg())
				++numOfEggs;
		}
		return numOfEggs < this.eggs ? true : false;	
	}
	
	/**
	 * Evaluates vertical condition.
	 * @param n Egg
	 * @return Returns boolean if passed.
	 */
	boolean verticalEvaluation(Egg n) {
		int y = n.getRow();
		int x = n.getCol();
		int numOfEggs = 0;
		for(int i = 0; i < this.rows; i++) {
			if(i == y)
				continue;
			if(board[i][x].isEgg())
				++numOfEggs;
		}
		return numOfEggs < this.eggs ? true : false;	
	}
	
	/**
	 * Evaluates \ - direction.
	 * @param n Egg
	 * @return Returns boolean value if passed condition.
	 */
	boolean diagonalEvaluation(Egg n) {
		// Check from left-top to bottom-right.
		int y = n.getRow(), x = n.getCol();
		int d = 0 - x;
		int numOfEggs = 0;
		while(d + x < this.columns) {
			if(((y + d < 0) || (y + d >= this.rows)) ||(y+d == y && x+d == x)) {
				++d;
				continue;
			}
			if(board[y + d][x + d].isEgg())
				++numOfEggs;
			++d;
		}
		return numOfEggs < this.eggs ? true : false;	
	}
	
	/**
	 * Evaluates / - direction.
	 * @param n Egg
	 * @return Returns boolean value if passed condition.
	 */
	boolean diagonalEvaluation2(Egg n) {
		// Check from the bottom-left to right-top.
		int y = n.getRow(), x = n.getCol();
		int d = this.rows - y - 1;
		int numOfEggs = 0;
		while(y + d >= 0) {
			if(x - d < 0 || x - d >= this.columns || (y + d == y && x - d == x)) {
				--d;
				continue;
			}
			if(board[y+d][x-d].isEgg())
				++numOfEggs;
			--d;
			
		}
		return numOfEggs < this.eggs ? true : false;
	}
	
	/**
	 * Used to print out a string representation of the current state / solution.
	 */
	void printBoard() {
		String line;
		for(int y = 0; y < this.rows; y++) {
			line = "|";
			for(int x = 0; x < this.columns; x++) {
				if(this.board[y][x].isEgg()) {
					line += "X";
				} else {
					line += " ";
				}
				line += "|";
			}
			System.out.println(line);
		}
		
	}
	
	/**
	 * Used to calculate the maximum achievable points.
	 * @return
	 */
	int getMaxPoints() {
		int maxPieces = rows < columns ? rows*eggs : columns*eggs;
		int size = rows*columns;
		int empty = size - maxPieces;
		return maxPieces*3 + empty*1;
	}
	
	/**
	 * Hard-copy the current state into a new object.
	 */
	protected EggCartonPuzzle clone() {
		EggCartonPuzzle copy = new EggCartonPuzzle(this.rows, this.columns, this.eggs);
		for(int y = 0; y < this.rows; y++) {
			for(int x = 0; x < this.columns; x++) {
				copy.board[y][x] = new Egg(this.board[y][x].isEgg(), y, x);
			}
		}
		return copy;
	}
	
	/**
	 * Returns the current state as double-array.
	 * @return Egg[][] board.
	 **/
	public Egg[][] getBoard() {
		return this.board;
	}
	
	/**
	 * Main function.
	 * @param args
	 */
	public static void main(String[] args) {
		EggCartonPuzzle puzzle;
		System.out.println("5x5 - 2. Solution: ");
		puzzle = new EggCartonPuzzle(12,12,1);
		puzzle.initiateAndRandomize();
		puzzle.run();
		puzzle.printBoard();
//		System.out.println("6x6 - 2. Solution: ");
//		puzzle = new EggCartonPuzzle(6,6,2);
//		puzzle.initiateAndRandomize();
//		puzzle.run();
//		puzzle.printBoard();
//		System.out.println("8x8 - 1. Solution: ");
//		puzzle = new EggCartonPuzzle(8,8,1);
//		puzzle.initiateAndRandomize();
//		puzzle.run();
//		puzzle.printBoard();
//		System.out.println("10x10 - 3. Solution: ");
//		puzzle = new EggCartonPuzzle(10,10,3);
//		puzzle.initiateAndRandomize();
//		puzzle.run();
//		puzzle.printBoard();
		
	}
}
