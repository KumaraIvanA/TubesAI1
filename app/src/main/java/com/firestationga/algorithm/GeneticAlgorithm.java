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
	private Environment env;
	private Population<C> population;

	public GeneticAlgorithm(Environment env) {
		this.env = env;
		this.population = new Population<>(env);
		population.populateRandomly();
	}

	public GeneticAlgorithmResult run() {
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
		}

		Individual<C> bestIndividual = population.getBestIndividual();
		Point[] fireStations = Arrays.stream(bestIndividual.getFireStations())
				.mapToObj(cell -> env.map().cellToPoint(cell))
				.toArray(Point[]::new);
		return new GeneticAlgorithmResult(fireStations, bestIndividual.getCost());
	}
}
