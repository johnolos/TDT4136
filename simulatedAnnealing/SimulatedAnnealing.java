package simulatedAnnealing;

import java.util.ArrayList;
import java.util.Random;

public abstract class SimulatedAnnealing {
	
	/** This state representation on how optimal solution it is. Between 0.0 and 1.0 */
	private double evaluation;
	
	/** Temperature of the vector. Initially it is at T_MAX, but decreases over time */
	private double temperature;
	
	/**
	 * This is where the most important part of the algorithm takes place.
	 */
	abstract void agendaLoop();
	
	/**
	 * This is where the vector is evaluated yielding performance
	 * and quality. This evaluation are then used to help steer the
	 * search simulated annealing algorithm does in a promising
	 * direction.
	 * @param n Vector to be evaluated.
	 */
	abstract double ObjectiveFunction();
	
	/**
	 * This is where neighbors are generated and added to
	 * node n.
	 * @param n Node which we shall generate children.
	 */
	abstract ArrayList<SimulatedAnnealing> generateNeighbors();
	
		
	/**
	 * Set this state's temperature
	 * @param temperature Temperature value.
	 */
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	
	/**
	 * Get this state's temperature.
	 * @return Return this states temperature.
	 */
	public double getTemperature() {
		return this.temperature;
	}
	
	/**
	 * Set the evaluation value.
	 * @param eval Evaluation value.
	 */
	public void setEvaluation(double eval) {
		this.evaluation = eval;
	}
	
	/**
	 * Get the evaluation value.
	 * @return Returns this states evaluation.
	 */
	public double getEvaluation() {
		return this.evaluation;
	}
}
