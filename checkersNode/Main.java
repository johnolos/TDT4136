package checkersNode;

public class Main {
	public static void main(String[] args) {
		int[] checkersNumbers = {6,12};
		
		for(int i = 0; i < checkersNumbers.length; i++) {
			System.out.println("================  Best-First-Search  =================");
			Checkers checkers = new Checkers(checkersNumbers[i], SearchType.BESTFIRSTSEARCH); 
			System.out.println("================  Depth-First-Search  =================");
			Checkers checkers2 = new Checkers(checkersNumbers[i], SearchType.DEPTHFIRSTSEARCH);
			System.out.println("================  Bredth-First-Search  =================");
			Checkers checkers3 = new Checkers(checkersNumbers[i], SearchType.BREADTHFIRSTSEARCH);
			System.out.println("=====================  NEW SIZE  ======================");
		}
	}
}
