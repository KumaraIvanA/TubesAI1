package com.firestationga.algorithm;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Kelas chromosome yang menggunakan konsep bitstring.
 * 
 * @author Kumara Ivan Anuraga
 */
public class BitStringChromosome extends Chromosome<BitStringChromosome> {
	private boolean[] bitString;
	private Environment env;

	public void print() {
		for (boolean b : bitString) {
			System.out.print(b ? 1 : 0);
		}
		System.out.println();
	}

	public BitStringChromosome(boolean[] bitString, Environment env) {
		this.bitString = bitString;
		this.env = env;
	}

	public BitStringChromosome(Environment env) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < env.map().getNEmptyCells(); i++) {
			indexes.add(i);
		}

		Collections.shuffle(indexes, env.random());

		this.bitString = new boolean[env.map().getNEmptyCells()];
		this.env = env;
		for (int i = 0; i < env.nFireStations(); i++) {
			bitString[indexes.get(i)] = true;
		}
	}

	public BitStringChromosome[] crossoverSingle(BitStringChromosome other) {
		int cutoff = env.random().nextInt(bitString.length);

		boolean[] childBits1 = new boolean[bitString.length];
		boolean[] childBits2 = new boolean[bitString.length];
		for (int i = 0; i < cutoff; i++) {
			childBits1[i] = bitString[i];
			childBits2[i] = other.getBitString()[i];
		}

		for (int i = cutoff; i < bitString.length; i++) {
			childBits1[i] = other.getBitString()[i];
			childBits2[i] = bitString[i];
		}

		repair(childBits1);
		repair(childBits2);

		BitStringChromosome[] childrens = new BitStringChromosome[2];
		childrens[0] = new BitStringChromosome(childBits1, env);
		childrens[1] = new BitStringChromosome(childBits2, env);
		return childrens;
	}

	public BitStringChromosome[] crossoverUniform(BitStringChromosome other) {
		boolean[] childBits1 = new boolean[bitString.length];
		boolean[] childBits2 = new boolean[bitString.length];

		for (int i = 0; i < bitString.length; i++) {
			if (env.random().nextBoolean()) {
				childBits1[i] = bitString[i];
				childBits2[i] = other.bitString[i];
			} else {
				childBits1[i] = other.bitString[i];
				childBits2[i] = bitString[i];
			}
		}

		repair(childBits1);
		repair(childBits2);

		BitStringChromosome[] childrens = new BitStringChromosome[2];
		childrens[0] = new BitStringChromosome(childBits1, env);
		childrens[1] = new BitStringChromosome(childBits2, env);
		return childrens;
	}

	public BitStringChromosome mutateRandom() {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == env.nFireStations()) {
			return new BitStringChromosome(bitString, env);
		}

		List<Integer> chosenIndexes = new ArrayList<Integer>();
		List<Integer> emptyIndexes = new ArrayList<Integer>();
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				chosenIndexes.add(i);
			} else {
				emptyIndexes.add(i);
			}
		}

		// Pilih petak yang berisi FS
		int chosenIndex = env.random().nextInt(chosenIndexes.size());
		int from = chosenIndexes.get(chosenIndex);

		// Pilih petak yang tidak berisi FS
		int emptyIndex = env.random().nextInt(emptyIndexes.size());
		int to = emptyIndexes.get(emptyIndex);

		// Buat array duplikat dan tukar FS
		boolean[] newBitString = bitString.clone();
		newBitString[from] = false;
		newBitString[to] = true;

		return new BitStringChromosome(newBitString, env);
	}

	public BitStringChromosome mutateLocal() {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == env.nFireStations()) {
			return new BitStringChromosome(bitString, env);
		}

		List<Integer> froms = new ArrayList<Integer>();
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				froms.add(i);
			}
		}

		Collections.shuffle(froms, env.random());

		for (int from : froms) {
			int direction = env.random().nextBoolean() ? 1 : -1;
			int[] tos = { from + direction, from - direction };

			for (int to : tos) {
				if (bitString[to]) {
					continue;
				}

				boolean[] newBitString = bitString.clone();
				newBitString[from] = false;
				newBitString[to] = true;

				return new BitStringChromosome(newBitString, env);
			}
		}

		return new BitStringChromosome(bitString, env);
	}

	public int[] decode() {
		int[] result = new int[env.nFireStations()];

		int resultCounter = 0;
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				result[resultCounter] = i;
				resultCounter++;
			}
		}

		return result;
	}

	private void repair(boolean[] bits) {
		int ones = 0;
		for (boolean bit : bits) {
			if (bit) {
				ones++;
			}
		}

		while (ones > env.nFireStations()) {
			int i = env.random().nextInt(bits.length);
			if (bits[i]) {
				bits[i] = false;
				ones--;
			}
		}

		while (ones < env.nFireStations()) {
			int i = env.random().nextInt(bits.length);
			if (!bits[i]) {
				bits[i] = true;
				ones++;
			}
		}
	}

	public boolean[] getBitString() {
		return bitString;
	}

}
