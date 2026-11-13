package com.firestationga.algorithm;

import java.util.Random;

import com.firestationga.config.ChromosomeType;
import com.firestationga.config.CrossoverMethod;
import com.firestationga.config.MutationMethod;
import com.firestationga.config.SelectionMethod;
import com.firestationga.utility.Map;

/**
 * Kelas yang menumpan atribut-atribut yang menjelaskan lingkungan masalah fire
 * station.
 *
 * @author Kumara Ivan Anuraga
 */
public record Environment(
		ChromosomeType chromosomeType,
		SelectionMethod selectionMethod,
		CrossoverMethod crossoverMethod,
		MutationMethod mutationMethod,
		int maxPopulationSize,
		int generations,
		double pcrossover,
		double pmutation,
		double pelitism,
		Random random,
		Map map,
		int nFireStations) {
}
