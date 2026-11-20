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

	/**
	 * Konstruktor untuk membuat {@code BitStringChromosome} baru dengan
	 * bitstring yang disediakan.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param bitString bitstring kromosom
	 * @param env lingkungan masalah
	 */
	public BitStringChromosome(boolean[] bitString, Environment env) {
		this.bitString = bitString;
		this.env = env;
	}

	/**
	 * Konstruktor untuk membuat {@code BitStringChromosome} baru dengan
	 * secara acak.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @param env lingkungan masalah
	 */
	public BitStringChromosome(Environment env) {
		// Simpan index sel kosong peta yang berupa angka 0 sampai n ke dalam
		// daftar baru dengan n yang berupa jumlah sel kosong peta 
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < env.map().getNEmptyCells(); i++) {
			indexes.add(i);
		}

		// Acak urutan index sel kosong
		Collections.shuffle(indexes, env.random());

		this.bitString = new boolean[env.map().getNEmptyCells()];
		this.env = env;

		// Pilih p index sel kosong, dengan p berupa jumlah fire station,
		// kemudian ubdah bit yang terpilih menjadi true.
		for (int i = 0; i < env.nFireStations(); i++) {
			bitString[indexes.get(i)] = true;
		}
	}

	/**
	 * Method untuk melakukan crossover dengan metode "single"
	 * Ditulis oleh: Clarissa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @param other Kromosom induk lainnya
	 * @return keturunan yang dihasilkan
	 */
	public BitStringChromosome[] crossoverSingle(BitStringChromosome other) {
		// Tentukan tempat yang menjadi titik potong
		int cutoff = env.random().nextInt(bitString.length);

		// Inisialisasi tempat penyimpanan bitstring keturun baru
		boolean[] childBits1 = new boolean[bitString.length];
		boolean[] childBits2 = new boolean[bitString.length];

		// Bit-bit kromosom yang terletak sebelum titik potong diberikan kepada
		// anak pertama. Sedangkan bit-bit kromosom lainnya yang terletak
		// sebelum titik potong diberikan kepada anak kedua.
		for (int i = 0; i < cutoff; i++) {
			childBits1[i] = bitString[i];
			childBits2[i] = other.bitString[i];
		}

		// Bit-bit kromosom yang terletak setelah titik potong diberikan kepada
		// anak kedua. Sedangkan bit-bit kromosom lainnya yang terletak
		// sebelum titik potong diberikan kepada anak pertama.
		for (int i = cutoff; i < bitString.length; i++) {
			childBits1[i] = other.bitString[i];
			childBits2[i] = bitString[i];
		}

		// Lakukan perbaikan bitstring jika bitstring tidak valid
		repair(childBits1);
		repair(childBits2);

		// Masukkan bitstring setiap anak ke dalam sebuah array
		BitStringChromosome[] childrens = new BitStringChromosome[2];
		childrens[0] = new BitStringChromosome(childBits1, env);
		childrens[1] = new BitStringChromosome(childBits2, env);
		return childrens;
	}

	/**
	 * Method untuk melakukan crossover dengan metode "uniform"
	 * Ditulis oleh: Clarissa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @param other Kromosom induk lainnya
	 * @return keturunan yang dihasilkan
	 */
	public BitStringChromosome[] crossoverUniform(BitStringChromosome other) {
		// Inisialisasi tempat penyimpanan bitstring keturun baru
		boolean[] childBits1 = new boolean[bitString.length];
		boolean[] childBits2 = new boolean[bitString.length];

		// Untuk setiap bit, pilih antara 0 atau 1 secara acak. Jika terpilih
		// angka 1, 
		// Lakukan looping untuk setiap bit
		for (int i = 0; i < bitString.length; i++) {
			// Pilih sebuah boolean secara acak
			if (env.random().nextBoolean()) {
				// Jika terpilih true, maka bit kromosom diberikan kepada anak
				// pertama dan bit kromosom lainnya diberikan kepada anak kedua.
				childBits1[i] = bitString[i];
				childBits2[i] = other.bitString[i];
			} else {
				// Jika terpilih false, maka bit kromosom diberikan kepada anak
				// kedua dan bit kromosom lainnya diberikan kepada anak pertama.
				childBits1[i] = other.bitString[i];
				childBits2[i] = bitString[i];
			}
		}

		// Lakukan perbaikan bitstring jika bitstring tidak valid
		repair(childBits1);
		repair(childBits2);

		// Masukkan bitstring setiap anak ke dalam sebuah array
		BitStringChromosome[] childrens = new BitStringChromosome[2];
		childrens[0] = new BitStringChromosome(childBits1, env);
		childrens[1] = new BitStringChromosome(childBits2, env);
		return childrens;
	}

	/**
	 * Method untuk melakukan mutasi dengan metode "random"
	 * Ditulis oleh: Clarissa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @return kromosom baru setelah proses mutasi terjadi
	 */
	public BitStringChromosome mutateRandom() {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == env.nFireStations()) {
			return new BitStringChromosome(bitString, env);
		}

		// Inisialisasi List yang menyimpan index bit yang sudah terpilih dan
		// index bit yang belum terpilih
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

	/**
	 * Method untuk melakukan mutasi dengan metode "local"
	 * Ditulis oleh: Clarissa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @return kromosom baru setelah proses mutasi terjadi
	 */
	public BitStringChromosome mutateLocal() {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == env.nFireStations()) {
			return new BitStringChromosome(bitString, env);
		}

		// Inisialisasi List yang menyimpan index bit yang sudah terpilih yang
		// menjadi calon index bit asal
		List<Integer> froms = new ArrayList<Integer>();
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				froms.add(i);
			}
		}

		// Acak List tersebut
		Collections.shuffle(froms, env.random());

		// Untuk setiap bit yang terpilih
		for (int from : froms) {
			// Simpan index bit sebelumnya dan sesudahnya yang menjadi calon
			// index bit tujuan 
			int direction = env.random().nextBoolean() ? 1 : -1;
			int[] tos = { from + direction, from - direction };

			for (int to : tos) {
				// Jika index tujuan valid
				if (to < 0 || to >= bitString.length) {
					continue;
				}

				// Dan jika bit tersebut belum terpilih
				if (bitString[to]) {
					continue;
				}

				// Salin bitstring awal dan tukarkan bit asal dengan bit tujuan
				boolean[] newBitString = bitString.clone();
				newBitString[from] = false;
				newBitString[to] = true;

				return new BitStringChromosome(newBitString, env);
			}
		}

		// Kembalikan kromosom induk jika mutasi tidak dapat dilakukan 
		return new BitStringChromosome(bitString, env);
	}

	/**
	 * Method untuk mengembalikan lokasi setiap <i>fire station</i> yang terpilih
	 * Ditulis oleh: Kumara Ivan Anuraga
	 *
	 * @return array lokasi setiap <i>fire station</i> yang terpilih
	 */
	public int[] decode() {
		// Inisialisasi array yang menyimpan lokasi setiap fire station yang
		// terpilih
		int[] result = new int[env.nFireStations()];

		
		int resultCounter = 0;

		// Untuk setiap bit
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				// Jika bit tersebut true, simpan index tersebut ke dalam array
				// result[] 
				result[resultCounter] = i;
				resultCounter++;
			}
		}

		return result;
	}

	/**
	 * Method untuk melakukan perbaikan bitstring setelah terjadinya suatu
	 * operasi.
	 * Ditulis oleh: Clarissa Helena
	 * Dimodifikasi oleh: Kumara Ivan Anuraga
	 *
	 * @param bits bitstring yang ingin diperbaiki
	 */
	private void repair(boolean[] bits) {
		// Hitung jumlah bit yang bernilai true
		int ones = 0;
		for (boolean bit : bits) {
			if (bit) {
				ones++;
			}
		}

		// Jika jumlah bit yang bernilai true berlebihan, pilih sebuah bit yang
		// bernilai true secara acak, lalu ubah bit tersebut menjadi false.
		while (ones > env.nFireStations()) {
			int i = env.random().nextInt(bits.length);
			if (bits[i]) {
				bits[i] = false;
				ones--;
			}
		}

		// Jika jumlah bit yang bernilai true kurang, pilih sebuah bit yang
		// bernilai false secara acak, lalu ubah bit tersebut menjadi true.
		while (ones < env.nFireStations()) {
			int i = env.random().nextInt(bits.length);
			if (!bits[i]) {
				bits[i] = true;
				ones++;
			}
		}
	}
}
