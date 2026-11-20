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

	/**
	 * Konstruktor untuk membuat {@code IntegerChromosome} baru dengan
	 * gen yang disediakan.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param genes gen kromosom
	 * @param env lingkungan masalah
	 */
	public IntegerChromosome(int[] genes, Environment env) {
		this.genes = genes;
		this.env = env;
	}

	/**
	 * Konstruktor untuk membuat {@code IntegerChromosome} baru
	 * secara acak.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param env lingkungan masalah
	 */
	public IntegerChromosome(Environment env) {
		// Simpan index dari sel kosong
		List<Integer> emptyCells = new ArrayList<Integer>();
		for (int i = 0; i < env.map().getNEmptyCells(); i++) {
			emptyCells.add(i);
		}

		// Acak index sel kosong
		Collections.shuffle(emptyCells, env.random());

		// Inisialisasi array gen dan lingkungan masalah
		this.genes = new int[env.nFireStations()];
		this.env = env;

		// Pilih p index sel kosong, dimana p merupakan jumlah fire station
		for (int i = 0; i < genes.length; i++) {
			genes[i] = emptyCells.get(i);
		}
	}

	/**
	 * Method untuk melakukan crossover dengan metode "single"
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param other Kromosom induk lainnya
	 * @return keturunan yang dihasilkan
	 */
	public IntegerChromosome[] crossoverSingle(IntegerChromosome other) {
		// Tentukan tempat yang menjadi titik potong
		int cutoff = env.random().nextInt(genes.length);

		// Inisialisasi tempat penyimpanan bitstring keturun baru
		int[] childGenes1 = new int[genes.length];
		int[] childGenes2 = new int[genes.length];

		// Gen-gen kromosom yang terletak sebelum titik potong diberikan kepada
		// anak pertama. Sedangkan gen-gen kromosom lainnya yang terletak
		// sebelum titik potong diberikan kepada anak kedua.
		for (int i = 0; i < cutoff; i++) {
			childGenes1[i] = genes[i];
			childGenes2[i] = other.genes[i];
		}

		// Gen-gen kromosom yang terletak setelah titik potong diberikan kepada
		// anak kedua. Sedangkan gen-gen kromosom lainnya yang terletak
		// sebelum titik potong diberikan kepada anak pertama.
		for (int i = cutoff; i < genes.length; i++) {
			childGenes1[i] = other.genes[i];
			childGenes2[i] = genes[i];
		}

		// Lakukan perbaikan bitstring jika bitstring tidak valid
		repair(childGenes1);
		repair(childGenes2);

		// Masukkan kromosom setiap anak ke dalam sebuah array
		IntegerChromosome[] childrens = new IntegerChromosome[2];
		childrens[0] = new IntegerChromosome(childGenes1, env);
		childrens[1] = new IntegerChromosome(childGenes2, env);
		return childrens;
	}

	/**
	 * Method untuk melakukan crossover dengan metode "uniform"
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param other Kromosom induk lainnya
	 * @return keturunan yang dihasilkan
	 */
	public IntegerChromosome[] crossoverUniform(IntegerChromosome other) {
		// Inisialisasi tempat penyimpanan bitstring keturun baru
		int[] childGenes1 = new int[genes.length];
		int[] childGenes2 = new int[genes.length];

		// Untuk setiap gen, pilih antara 0 atau 1 secara acak. Jika terpilih
		// angka 1, 
		// Lakukan looping untuk setiap gen
		for (int i = 0; i < genes.length; i++) {
			// Pilih sebuah boolean secara acak
			if (env.random().nextBoolean()) {
				// Jika terpilih true, maka gen kromosom diberikan kepada anak
				// pertama dan gen kromosom lainnya diberikan kepada anak kedua.
				childGenes1[i] = genes[i];
				childGenes2[i] = other.genes[i];
			} else {
				// Jika terpilih false, maka gen kromosom diberikan kepada anak
				// kedua dan gen kromosom lainnya diberikan kepada anak pertama.
				childGenes1[i] = other.genes[i];
				childGenes2[i] = genes[i];
			}
		}

		// Masukkan bitstring setiap anak ke dalam sebuah array
		IntegerChromosome[] childrens = new IntegerChromosome[2];
		childrens[0] = new IntegerChromosome(childGenes1, env);
		childrens[1] = new IntegerChromosome(childGenes2, env);
		return childrens;
	}

	/**
	 * Method untuk melakukan mutasi dengan metode "random"
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return kromosom baru setelah proses mutasi terjadi
	 */
	public IntegerChromosome mutateRandom() {
		// Return awal jika semua petak berisi FS.
		if (genes.length == env.map().getNEmptyCells()) {
			return new IntegerChromosome(genes, env);
		}

		// Simpan index sel kosong yang sudah terpakai
		ArrayList<Integer> usedValues = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(ArrayList::new));

		// Simpan keseluruhan index sel kosong
		Set<Integer> optionsSet = new HashSet<Integer>();
		for (int i = 0; i < env.map().getNEmptyCells(); i++) {
			optionsSet.add(i);
		}

		// Hapus sel kosong yang sudah terpakai
		optionsSet.removeAll(usedValues);

		// Ubah Set menjadi List
		List<Integer> options = new ArrayList<Integer>(optionsSet);

		// Peroleh index gen yang akan dimutasi secara acak
		int mutatedIndex = env.random().nextInt(genes.length);

		// Peroleh index sel kosong yang belum terpakai secara acak
		int optionsIndex = env.random().nextInt(options.size());

		// Buat salinan dari gen
		int[] newGenes = genes.clone();

		// Ubah alel dari gen yang akan dimutasi menjadi index sel kosong terpilih
		newGenes[mutatedIndex] = options.get(optionsIndex);
		return new IntegerChromosome(newGenes, env);
	}

	/**
	 * Method untuk melakukan mutasi dengan metode "local"
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return kromosom baru setelah proses mutasi terjadi
	 */
	public IntegerChromosome mutateLocal() {
		// Simpan keseluruhan index sel kosong
		List<Integer> values = new ArrayList<Integer>();
		for (int i = 0; i < env.map().getNEmptyCells(); i++) {
			values.add(i);
		}

		// Return awal jika semua petak berisi FS.
		if (genes.length == values.size()) {
			return new IntegerChromosome(genes, env);
		}

		// Simpan index sel kosong yang sudah terpakai
		HashSet<Integer> usedValues = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(HashSet::new));

		// Simpan index dari index sel kosong
		HashMap<Integer, Integer> valuesIndexMap = new HashMap<Integer, Integer>();
		for (int i = 0; i < values.size(); i++) {
			valuesIndexMap.put(values.get(i), i);
		}

		// Simpan index dari index gen
		List<Integer> geneIndexes = new ArrayList<Integer>();
		for (int i = 0; i < genes.length; i++) {
			geneIndexes.add(i);
		}

		// Acak index gen
		Collections.shuffle(geneIndexes, env.random());

		// Untuk setiap index sel kosong yang terpilih
		for (int geneIndex : geneIndexes) {
			int gene = genes[geneIndex];
			int geneCellIndex = valuesIndexMap.get(gene);
			int direction = env.random().nextBoolean() ? 1 : -1;
			int[] valuesToTry = { geneCellIndex + direction, geneCellIndex - direction };

			// Coba memindahkan fire station ke sel kosong disebelahnya
			for (int newValue : valuesToTry) {
				// Lewat jika posisi baru sudah ditempatkan sebuah fire station
				if (usedValues.contains(newValue)) {
					continue;
				}

				// Lewat jika posisi baru tidak valid
				if (newValue < 0 || newValue >= env.map().getNEmptyCells()) {
					continue;
				}

				// Salin gen awal dan tukarkan lokasi fire station asal dengan lokasi baru
				int[] newGenes = genes.clone();
				newGenes[geneIndex] = newValue;
				return new IntegerChromosome(newGenes, env);
			}
		}

		return new IntegerChromosome(env);
	}

	/**
	 * Method untuk mengembalikan lokasi setiap <i>fire station</i> yang terpilih
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return array lokasi setiap <i>fire station</i> yang terpilih
	 */
	public int[] decode() {
		return genes;
	}

	/**
	 * Method untuk melakukan perbaikan array gen setelah terjadinya suatu
	 * operasi.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param genes array gene yang ingin diperbaiki
	 */
	private void repair(int[] genes) {
		// Simpan setiap lokasi fire station unik yang sudah terpilih
		Set<Integer> set = Arrays.stream(genes)
				.boxed()
				.collect(Collectors.toCollection(HashSet::new));

		// Jika jumlah lokasi fire station unik lebih sedikit dari panjang
		// kromosom, maka terdapat nilai yang duplikat
		if (set.size() < genes.length) {
			// Simpan index sel kosong yang belum terpilih
			Set<Integer> optionsSet = new HashSet<Integer>();
			for (int i = 0; i < env.map().getNEmptyCells(); i++) {
				optionsSet.add(i);
			}
			optionsSet.removeAll(set);

			// Ubah menjadi tipe List
			List<Integer> options = new ArrayList<Integer>(optionsSet);

			// Acak List index sel kosong yang belum terpilih
			Collections.shuffle(options);

			// Untuk setiap gen
			int optionsCounter = 0;
			for (int i = 0; i < genes.length; i++) {
				// Cari gen dengan alel yang duplikat
				if (set.contains(genes[i])) {
					set.remove(genes[i]);
					continue;
				}

				// Ubah alel tersebut dengan alel yang belum terpilih
				genes[i] = options.get(optionsCounter);
				optionsCounter++;
			}
		}
	}
}
