package com.firestationga.algorithm;

import java.util.Arrays;
import java.util.List;

import com.firestationga.utility.Point;

/**
 * Kelas yang berfungsi untuk menjalankan <i>genetic algorithm</i>
 * 
 * @author Natasya Keisya Hartono
 */
public class GeneticAlgorithm<C extends Chromosome<C>> {
	private static final double STAGNATION_THRESHOLD = 0.50;
	private static final long TIME_LIMIT_MS = 60_000;
	private Environment env;
	private Population<C> population;


	/**
	 * Konstruktor untuk membuat {@code GeneticAlgorithm} baru dengan lingkungan
	 * yang disediakan.
	 * Ditulis oleh: Natasya Keisya Hartono
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 * 
	 * @param env lingkungan algoritma genetic
	 */
	public GeneticAlgorithm(Environment env) {
		this.env = env;
		this.population = new Population<>(env);

		// Isi populasi secara acak sampai penuh
		population.populateRandomly();
	}

	/**
	 * Method untuk menjalankan algoritma genetic.
	 * Ditulis oleh: Natasya Keisya Hartono
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 * 
	 * @return hasil perjalanan algoritma genetic
	 */
	public GeneticAlgorithmResult run() {
		int generationsWithoutImprovement = 0;
		int startTime = 0;
		double bestFitness = 0;
		for (int generation = 1; generation < env.generations(); generation++) {
			// buat populasi baru
			Population<C> newPopulation = population.getNewPopulationWithElit();

			while (!newPopulation.isFull()) {
				// kalau probabilitas masuk untuk dilakukan crossover
				if (env.random().nextDouble() < env.pcrossover()) {
					// pilih 2 parent untuk membuat individu baru
					List<Individual<C>> parents = population.selectParents();
					Individual<C> parent1 = parents.get(0);
					Individual<C> parent2 = parents.get(1);
					List<Individual<C>> childrens = parent1.crossover(parent2, env.crossoverMethod());

					// kalau probabilitas masuk untuk dilakukan mutasi untuk anak pertama
					if (env.random().nextDouble() < env.pmutation()) {
						childrens.get(0).mutate(env.mutationMethod());
					}

					// kalau probabilitas masuk untuk dilakukan mutasi untuk anak kedua
					if (env.random().nextDouble() < env.pmutation()) {
						childrens.get(1).mutate(env.mutationMethod());
					}

					// tambahkan individu ke populasi baru
					newPopulation.addIndividual(childrens.get(0));
					newPopulation.addIndividual(childrens.get(1));
				}
			}

			population = newPopulation;

			// Periksa jika terjadi stagnasi
			double currentFitness = population.getBestIndividual().getFitness();
			if (currentFitness <= bestFitness) {
				generationsWithoutImprovement++;

				if (generationsWithoutImprovement >= (env.generations() * STAGNATION_THRESHOLD)) {
					break;
				}
			} else {
				generationsWithoutImprovement = 0;
				bestFitness = currentFitness;
			}

			// Periksa jika sudah melewati batas waktu
			if (System.currentTimeMillis() - startTime > TIME_LIMIT_MS) {
				break;
			}
		}

		// Peroleh individu terbaik
		Individual<C> bestIndividual = population.getBestIndividual();

		// Peroleh koordinat setiap fire station
		Point[] fireStations = Arrays.stream(bestIndividual.getFireStations())
				.mapToObj(cell -> env.map().cellToPoint(cell))
				.toArray(Point[]::new);
		return new GeneticAlgorithmResult(fireStations, bestIndividual.getCost());
	}
}
