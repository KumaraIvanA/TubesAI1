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

	/**
	 * Konstruktor untuk membuat {@code Individual} baru dengan lingkungan
	 * dan kromosom yang disediakan.
	 * Ditulis oleh: Clarisa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 * 
	 * @param env lingkungan algoritma genetic
	 */
	public Individual(C chromosome, Environment env) {
		this.chromosome = chromosome;
		this.env = env;
		setFitness();
	}

	/**
	 * Method statis untuk membuat {@code Individual} secara acak berdasarkan
	 * lingkungan yang disediakan.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 * 
	 * @return individu yang terbuat secara acak
	 */
	public static <C extends Chromosome<C>> Individual<C> createRandom(Environment env) {
		switch (env.chromosomeType()) {
			case BITSTRING:
				return (Individual<C>) new Individual<BitStringChromosome>(new BitStringChromosome(env), env);
			case INTEGER:
				return (Individual<C>) new Individual<IntegerChromosome>(new IntegerChromosome(env), env);
			default:
				return null;
		}
	}

	/**
	 * Method untuk melakukan operasi <i>crossover</i>.
	 * Ditulis oleh: Clarisa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 * 
	 * @param other individu yang menjadi induk kedua
	 * @param crossoverMethod metode crossover yang akan digunakan
	 */
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

	/**
	 * Method untuk melakukan operasi <i>crossover</i>.
	 * Ditulis oleh: Clarisa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 * 
	 * @param mutationMethod metode mutasi yang akan digunakan
	 */
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

	/**
	 * Method untuk menghitung dan menyimpan fitness.
	 * Ditulis oleh: Clarisa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 */
	private void setFitness() {
		int[] fireStations = chromosome.decode();
		this.cost = DistanceMatrix.calculateCost(fireStations, env.map().getDistanceMatrix());
		this.fitness = 1.0 / (1.0 + cost);
	}

	/**
	 * Method untuk membandingkan suatu individu dengan individu yang lain
	 * berdasarkan fitnessnya.
	 * Ditulis oleh: Clarisa Helena
	 */
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

	/**
	 * Method untuk memperoleh cost individu.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param cost individu
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Method untuk memperoleh fitness individu.
	 * Ditulis oleh: Clarisa Helena
	 *
	 * @param fitness individu
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * Method untuk memperoleh lokasi fire station
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param array lokasi fire station
	 */
	public int[] getFireStations() {
		return chromosome.decode();
	}
}
