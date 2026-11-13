package com.firestationga.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Kelas yang merepresntasikan populasi (kumpulan individu) dari <i>genetic
 * algorithm</i>
 * 
 * @author Natasya Keisya Hartono
 */
public class Population<C extends Chromosome<C>> {
	private List<Individual<C>> population = new ArrayList<Individual<C>>();
	private Environment env;

	public Population(Environment env) {
		this.env = env;
	}

	public void populateRandomly() {
		for (int i = 0; i < env.maxPopulationSize(); i++) {
			Individual<C> newIndividual = Individual.createRandom(env);
			addIndividual(newIndividual);
		}
	}

	public boolean addIndividual(Individual<C> individual) {
		if (population.size() >= env.maxPopulationSize()) {
			return false;
		}

		population.add(individual);
		return true;
	}

	public Population<C> getNewPopulationWithElit() {
		Population<C> newPopulation = new Population<C>(env);
		int n = (int) (env.pelitism() * env.maxPopulationSize());
		for (int i = 0; i < n; i++) {
			newPopulation.addIndividual(population.get(i));
		}

		return newPopulation;
	}

	public Individual<C> getBestIndividual() {
		population.sort((individual1, individual2) -> individual1.compareTo(individual2));
		return population.get(0);
	}

	public boolean isFull() {
		return population.size() == env.maxPopulationSize();
	}

	public List<Individual<C>> rouletteSelection() {
		population.sort((individual1, individual2) -> individual1.compareTo(individual2));

		// hitung keseluruhan fitness populasi
		long sumfitness = 0;
		for (Individual<C> i : population) {
			sumfitness += i.getFitness();
		}

		// hitung probabilitas masing - masing individu untuk menjadi parent
		double[] parentProbability = new double[population.size()];
		for (int i = 0; i < this.population.size(); i++) {
			parentProbability[i] = population.get(i).getFitness() / sumfitness;
		}

		// periksa probabilitas masing-masing parent
		List<Individual<C>> parents = new ArrayList<Individual<C>>();
		for (int n = 0; n < 2; n++) {
			int i = -1;
			double prob = env.random().nextDouble();
			double sum = 0.0;
			// jika sudah tidak masuk ke probabilitas, pilih parent tersebut untuk jadi
			// parent
			do {
				i++;
				sum = sum + parentProbability[i];
			} while (sum < prob);
			parents.add(population.get(i));
		}

		return parents;
	}

	public List<Individual<C>> rankSelection() {
		population.sort((idv1, idv2) -> idv1.compareTo(idv2));

		double rankSum = (double) (1 + population.size()) * population.size() / 2;
		double[] parentProbability = new double[population.size()];
		for (int i = 0; i < population.size(); i++) {
			parentProbability[i] = (1.0 + i) / rankSum;
		}

		List<Individual<C>> parents = new ArrayList<Individual<C>>();
		for (int n = 0; n < 2; n++) {
			int i = -1;
			double prob = env.random().nextDouble();
			double sum = 0.0;
			do {
				i++;
				sum = sum + parentProbability[i];
			} while (sum < prob);
			parents.add(population.get(i));
		}
		return parents;
	}

	public List<Individual<C>> selectParents() {
		switch (env.selectionMethod()) {
			case ROULETTE:
				return rouletteSelection();
			case RANK:
				return rankSelection();
		}

		return null;
	}
}
