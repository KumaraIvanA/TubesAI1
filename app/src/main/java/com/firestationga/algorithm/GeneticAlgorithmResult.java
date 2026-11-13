package com.firestationga.algorithm;

import com.firestationga.utility.Point;

/**
 * Kelas yang berisi hasil dari perjalanan <i>genetic algorithm</i>
 *
 * @author Kumara Ivan Anuraga
 */
public class GeneticAlgorithmResult {
	private Point[] fireStations;
	private double cost;
	
	public GeneticAlgorithmResult(Point[] fireStations, double cost) {
		this.fireStations = fireStations;
		this.cost = cost;
	}

	public double getCost() {
		return cost;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append(fireStations.length)
			.append(' ')
			.append(String.format("%.5f", cost));
		for (Point p : fireStations) {
			str.append('\n')
				.append(p.r)
				.append(' ')
				.append(p.c);
		}

		return str.toString();
	}
}
