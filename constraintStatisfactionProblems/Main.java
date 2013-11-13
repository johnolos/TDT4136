package constraintStatisfactionProblems;

public class Main {
	
	final public static int RUNS = 2;
	final public static int QUEENSIZE = 650;
	
	
	public static void main(String args[]) {
		KQueen game;
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < RUNS; i++) {
			game = new KQueen(QUEENSIZE);
			game.initiateBoardWithQueens();
			//game.printBoard();
			game.MIN_CONFLICTS();
			//game.printBoard();
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total time: " + totalTime);
		System.out.println("Average time: " + totalTime / (double)RUNS);
	}

}
