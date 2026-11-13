package com.firestationga.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Kelas chromosome yang langsung menyimpan lokasi <i>fire station</i>.
 * 
 * @author Kumara Ivan Anuraga
 */
public class IntegerChromosome extends Chromosome<IntegerChromosome> {
	private int[] genes;
	private Environment env;

	public IntegerChromosome(int[] genes, Environment env) {
		this.genes = genes;
		this.env = env;
	}

	public IntegerChromosome(Environment env) {
		List<Integer> emptyCells = new ArrayList<Integer>(env.map().getEmptyCells());
		Collections.shuffle(emptyCells, env.random());

		this.genes = new int[env.nFireStations()];
		this.env = env;

		for (int i = 0; i < genes.length; i++) {
			genes[i] = emptyCells.get(i);
		}
	}

	public IntegerChromosome[] crossoverSingle(IntegerChromosome other) {
		int cutoff = env.random().nextInt(genes.length);

		int[] childGenes1 = new int[genes.length];
		int[] childGenes2 = new int[genes.length];
		for (int i = 0; i < cutoff; i++) {
			childGenes1[i] = genes[i];
			childGenes2[i] = other.genes[i];
		}

		for (int i = cutoff; i < genes.length; i++) {
			childGenes1[i] = other.genes[i];
			childGenes2[i] = genes[i];
		}

		repair(childGenes1);
		repair(childGenes2);

		IntegerChromosome[] childrens = new IntegerChromosome[2];
		childrens[0] = new IntegerChromosome(childGenes1, env);
		childrens[1] = new IntegerChromosome(childGenes2, env);
		return childrens;
	}

	public IntegerChromosome[] crossoverUniform(IntegerChromosome other) {
		int[] childGenes1 = new int[genes.length];
		int[] childGenes2 = new int[genes.length];

		for (int i = 0; i < genes.length; i++) {
			if (env.random().nextBoolean()) {
				childGenes1[i] = genes[i];
				childGenes2[i] = other.genes[i];
			} else {
				childGenes1[i] = other.genes[i];
				childGenes2[i] = genes[i];
			}
		}

		IntegerChromosome[] childrens = new IntegerChromosome[2];
		childrens[0] = new IntegerChromosome(childGenes1, env);
		childrens[1] = new IntegerChromosome(childGenes2, env);
		return childrens;
	}

	public IntegerChromosome mutateRandom() {
		if (genes.length == env.map().getEmptyCells().size()) {
			return new IntegerChromosome(genes, env);
		}

		ArrayList<Integer> usedValues = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(ArrayList::new));

		Set<Integer> optionsSet = new HashSet<Integer>(env.map().getEmptyCells());
		optionsSet.removeAll(usedValues);

		List<Integer> options = new ArrayList<Integer>(optionsSet);

		int mutatedIndex = env.random().nextInt(genes.length);
		int optionsIndex = env.random().nextInt(options.size());

		int[] newGenes = genes.clone();
		newGenes[mutatedIndex] = options.get(optionsIndex);
		return new IntegerChromosome(newGenes, env);
	}

	public IntegerChromosome mutateLocal() {
		List<Integer> values = env.map().getEmptyCells();

		if (genes.length == values.size()) {
			return new IntegerChromosome(genes, env);
		}

		HashSet<Integer> usedValues = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(HashSet::new));

		HashMap<Integer, Integer> valuesIndexMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < values.size(); i++) {
			valuesIndexMap.put(values.get(i), i);
		}

		List<Integer> geneIndexes = new ArrayList<Integer>();
		for (int i = 0; i < genes.length; i++) {
			geneIndexes.add(i);
		}

		Collections.shuffle(geneIndexes, env.random());

		for (int geneIndex : geneIndexes) {
			int gene = genes[geneIndex];
			int geneCellIndex = valuesIndexMap.get(gene);
			int direction = env.random().nextBoolean() ? 1 : -1;
			int[] valuesToTry = { geneCellIndex + direction, geneCellIndex - direction };

			for (int newValue : valuesToTry) {
				if (usedValues.contains(newValue)) {
					continue;
				}

				int[] newGenes = genes.clone();
				newGenes[geneIndex] = newValue;
				return new IntegerChromosome(newGenes, env);
			}
		}

		return new IntegerChromosome(env);
	}

	public int[] decode() {
		return genes;
	}

	private void repair(int[] genes) {
		Set<Integer> set = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(HashSet::new));

		if (set.size() < genes.length) {
			Set<Integer> optionsSet = new HashSet<Integer>(env.map().getEmptyCells());
			optionsSet.removeAll(set);

			List<Integer> options = new ArrayList<Integer>(optionsSet);
			Collections.shuffle(options);

			int optionsCounter = 0;
			for (int i = 0; i < genes.length; i++) {
				if (set.contains(genes[i])) {
					set.remove(genes[i]);
					continue;
				}

				genes[i] = options.get(optionsCounter);
				optionsCounter++;
			}
		}
	}
}
