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

public class Main {
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

		// Membaca peta
		char[][] mapArray;
		int nFireStations;
		try (Scanner sc = new Scanner(options.filepath())) { // membaca file
			int rows = sc.nextInt(); // membaca banyaknya baris
			int cols = sc.nextInt(); // membaca banyaknya kolom
			nFireStations = sc.nextInt(); // membaca banyaknya fStation yang dibutuhkan

			mapArray = new char[rows][cols]; // peta sebesar baris x kolom

			for (int i = 0; i < mapArray.length; i++) {
				mapArray[i] = sc.next().toCharArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}

		// Panggil Map untuk mendapatkan isi map
		Map map = new Map(mapArray);

		// Buat sejumlah seed tergatung dengan jumlah percobaan
		int npm_ketua = 24045;
		Random randomTmp = new Random(npm_ketua);
		int bound = 1 << 16;

		int[] seeds = new int[options.runs()];
		for (int i = 0; i < seeds.length; i++) {
			seeds[i] = randomTmp.nextInt(bound);
		}

		int runs = options.runs();

		double costSum = 0;
		for (int i = 0; i < runs; i++) {
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

			if (runs > 1) {
				System.out.printf("Run %d:\n", i + 1);
			}

			GeneticAlgorithmResult result = runGA(env);
			costSum += result.getCost();
			System.out.println(result);
			System.out.println();
		}

		if (runs > 1) {
			System.out.printf("Average cost out of %d runs: %.5f\n", runs, costSum / runs);
		}
	}

	private static <C extends Chromosome<C>> GeneticAlgorithmResult runGA(Environment env) {
		GeneticAlgorithm<C> ga = new GeneticAlgorithm<C>(env);
		return ga.run();
	}
}
