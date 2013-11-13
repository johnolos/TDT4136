package simulatedAnnealing;

public class Egg {
	
	/** Boolean used to determine if it's an egg **/
	private boolean egg;
	/** Y- and X-codinates. Helpful when debuging **/
	private int y, x;
	

	/**
	 * 
	 * @param temperature Temperature T used by the simulated annealing algorithm.
	 * @param egg Boolean to determine if it's an egg or an empty slot.
	 * @param row Indicate which row it is.
	 * @param col Indicate which column it is.
	 */
	public Egg(boolean egg, int row, int col) {
		super();
		this.egg = egg;
		this.y = row;
		this.x = col;
	}
	
	boolean isEgg() {
		return this.egg;
	}
	
	void setEgg(boolean bol) {
		this.egg = bol;
	}
	
	void setEmpty() {
		this.egg = false;
	}
	
	int getRow() {
		return this.y;
	}
	
	int getCol() {
		return this.x;
	}
	

}
