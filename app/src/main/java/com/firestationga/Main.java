package com.firestationga;

import java.util.Random;
import java.util.Scanner;

import com.firestationga.algorithm.Chromosome;
import com.firestationga.algorithm.Environment;
import com.firestationga.algorithm.GeneticAlgorithm;
import com.firestationga.algorithm.GeneticAlgorithmResult;
import com.firestationga.cli.CLIParser;
import com.firestationga.cli.InvalidArgumentException;
import com.firestationga.config.Options;
import com.firestationga.utility.Map;

/**
 * Kelas yang menjadi titik awal program.
 *
 * @author Natasya Keisya Hartono
 */
public class Main {
	/**
	* Method sebagai titik awal program.
	* Ditulis oleh: Natasya Keisya Hartono
	* Dimodifikasi oleh: Kumara Ivan Anuraga
	* 
	* @param args argumen CLI yang diberikan pengguna.
	*/
	public static void main(String[] args) {
		// Membaca argumen CLI
		Options options;
		try {
			options = CLIParser.parseArgs(args);
		} catch (InvalidArgumentException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
			return;
		}

		// Mengdeklarasikan variable untuk menyimpan peta
		char[][] mapArray;
		int nFireStations;

		// Membaca file peta 
		try (Scanner sc = new Scanner(options.filepath())) {
			// Membaca jumlah baris peta
			int rows = sc.nextInt();

			// Membaca jumlah kolom peta
			int cols = sc.nextInt();

			// Membaca jumlah fire station yang perlu ditempatkan
			nFireStations = sc.nextInt();

			// Menginisialisasi penyimpanan peta sebesar baris x kolom
			mapArray = new char[rows][cols];

			// Mengisi variable penyimpanan peta dengan karakter sebenarnya
			for (int i = 0; i < mapArray.length; i++) {
				mapArray[i] = sc.next().toCharArray();
			}
		} catch (Exception e) {
			// Print pesan error jika terjadi kesalahan saat membaca file peta
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// Mengubah array karakter 2D menjadi objek peta tersendirinya
		Map map = new Map(mapArray);

		// Gunakan npm ketua sebagai seed untuk menghasilkan seed sebenarnya.
		int npm_ketua = 24045;
		Random randomTmp = new Random(npm_ketua);

		// Batas angka yang akan menjadi seed. Seed tidak dapat melebihi 2^16
		int bound = 1 << 16;

		// Buat sejumlah seed tergatung dengan jumlah percobaan
		int[] seeds = new int[options.runs()];
		for (int i = 0; i < seeds.length; i++) {
			seeds[i] = randomTmp.nextInt(bound);
		}

		// Peroleh jumlah percobaan yang ingin dilakukan
		int runs = options.runs();

		// Inisialisasi variabel untuk menyimpan cost dari setiap percobaan
		// serta hasil jumlahnya.
		double costSum = 0;
		double[] costList = new double[runs];

		// Jalankan percobaan sebanyak jumlah yang diinginkan pengguna.
		for (int i = 0; i < runs; i++) {
			// Masukkan semua parameter pengguna ke dalam lingukan algoritma
			Environment env = new Environment(
					options.chromosomeType(),
					options.selectionMethod(),
					options.crossoverMethod(),
					options.mutationMethod(),
					options.population(),
					options.generations(),
					options.pcrossover(),
					options.pmutation(),
					options.pelitism(),
					new Random(seeds[i]),
					map,
					nFireStations);


			// Keluarkan informasi mengenai urutan mengenai percobaan sekarang
			// jika dilakukan lebih dari satu kali.
			if (runs > 1) {
				System.out.printf("Run %d:\n", i + 1);
			}

			// Jalankan algoritma genetic
			GeneticAlgorithmResult result = runGA(env);

			// Simpan cost dari hasil algoritma.
			costList[i] = result.getCost();

			// Jumlahkan cost dari hasil algoritma.
			costSum += result.getCost();

			// Tampilkan hasil
			System.out.println(result);
			System.out.println();
		}

		// Jika dilakukan lebih dari satu percobaan, tampilkan rata-rata dari
		// cost setiap percobaan
		if (runs > 1) {
			System.out.printf("Average cost out of %d runs: %.5f\n", runs, costSum / runs);
		}
	}

	/**
	 * Method untuk menjalankan algoritma genetic untuk sebuah lingkungan.
	 * Ditulis oleh: Kumara Ivan Anuraga
	 * 
	 * @param Env lingkungan algoritma genetic
	 */
	private static <C extends Chromosome<C>> GeneticAlgorithmResult runGA(Environment env) {
		GeneticAlgorithm<C> ga = new GeneticAlgorithm<C>(env);
		return ga.run();
	}
}
