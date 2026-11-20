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
	
	/**
	 * Konstruktor untuk membuat {@code GeneticAlgorithmResult} baru dengan
	 * array lokasi fire station dan cost dari solusi yang disediakan.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 * 
	 * @param fireStations array lokasi setiap <i>fire station</i>
	 * @param cost cost dari solusi
	 */
	public GeneticAlgorithmResult(Point[] fireStations, double cost) {
		this.fireStations = fireStations;
		this.cost = cost;
	}

	/**
	 * Method untuk mengembalikan cost dari hasil algoritma genetic.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return cost dari solusi
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Method untuk mengembalikan hasil algoritma genetic dalam bentuk String.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return Representasi string dari hasil algoritma genetic
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		// Simpan jumlah fire station dan cost dari solusi
		str.append(fireStations.length)
			.append(' ')
			.append(String.format("%.5f", cost));

		// Simpan koordinat setiap lokasi fire station
		for (Point p : fireStations) {
			str.append('\n')
				.append(p.r)
				.append(' ')
				.append(p.c);
		}

		return str.toString();
	}
}
