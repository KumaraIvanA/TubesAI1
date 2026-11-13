package com.firestationga.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.firestationga.config.CrossoverMethod;
import com.firestationga.config.MutationMethod;
import com.firestationga.utility.DistanceMatrix;

/**
 * Kelas yang merepresentasikan sebuah individu dengan sebuah kromosom
 *
 * @author Clarisa Helena
 */
public class Individual<C extends Chromosome<C>> implements Comparable<Individual<C>> {
	private C chromosome;
	private Environment env;
	private double cost;
	private double fitness;

	public Individual(C chromosome, Environment env) {
		this.chromosome = chromosome;
		this.env = env;
		setFitness();
	}

	public static <C extends Chromosome<C>> Individual<C> createRandom(Environment env) {
		switch (env.chromosomeType()) {
			case BITSTRING:
				return (Individual<C>) new Individual<BitStringChromosome>(new BitStringChromosome(env), env);
			case INTEGER:
				return (Individual<C>) new Individual<BitStringChromosome>(new BitStringChromosome(env), env);
			default:
				return null;
		}
	}

	public List<Individual<C>> crossover(Individual<C> other, CrossoverMethod crossoverMethod) {
		C[] childrens;
		switch (crossoverMethod) {
			case ONE_POINT:
				childrens = chromosome.crossoverSingle(other.chromosome);
				break;
			case UNIFORM:
				childrens = chromosome.crossoverUniform(other.chromosome);
				break;
			default:
				throw new IllegalArgumentException(
						String.format("Unknown crossover method: %s", crossoverMethod.toString()));
		}

		List<Individual<C>> newIndividuals = new ArrayList<Individual<C>>();
		newIndividuals.add(new Individual<>(childrens[0], env));
		newIndividuals.add(new Individual<>(childrens[1], env));
		return newIndividuals;
	}

	public void mutate(MutationMethod mutationMethod) {
		switch (mutationMethod) {
			case LOCAL:
				this.chromosome = chromosome.mutateLocal();
				break;
			case RANDOM:
				this.chromosome = chromosome.mutateRandom();
				break;
			default:
				throw new IllegalArgumentException(
						String.format("Unknown mutation method: %s", mutationMethod.toString()));
		}
	}

	private void setFitness() {
		int[] fireStations = chromosome.decode();
		this.cost = DistanceMatrix.calculateCost(fireStations, env.map().getDistanceMatrix());
		this.fitness = 1.0 / (1.0 + cost);
	}

	@Override
	public int compareTo(Individual<C> other) {
		if (this.fitness > other.fitness) {
			return -1;
		} else if (this.fitness < other.fitness) {
			return 1;
		} else {
			return 0;
		}
	}

	public double getCost() {
		return cost;
	}

	public double getFitness() {
		return fitness;
	}

	public int[] getFireStations() {
		return chromosome.decode();
	}
}
