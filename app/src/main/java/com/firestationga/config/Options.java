package com.firestationga.config;

import java.nio.file.Path;

/**
 * Record untuk menyimpan konfigurasi untuk digunakan genetic algorithm
 * 
 * @author Kumara Ivan Anuraga
 */
public record Options(
		ChromosomeType chromosomeType,
		SelectionMethod selectionMethod,
		CrossoverMethod crossoverMethod,
		MutationMethod mutationMethod,
		int population,
		int generations,
		double pcrossover,
		double pmutation,
		double pelitism,
		int runs,
		Path filepath) {
}
