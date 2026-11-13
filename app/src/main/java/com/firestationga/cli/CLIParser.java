package com.firestationga.cli;

import java.nio.file.Files;
import java.nio.file.Path;
import com.firestationga.config.Options;
import com.firestationga.config.SelectionMethod;
import com.firestationga.config.ChromosomeType;
import com.firestationga.config.CrossoverMethod;
import com.firestationga.config.MutationMethod;

/**
 * Kelas untuk melakukan parsing terhadap argumen CLI
 *
 * @author Kumara Ivan Anuraga
 */
public class CLIParser {
	private static String helpString = "" +
			"Usage: fsga [options...] <file>\n" +
			"\n" +
			"Options:\n" +
			"  -h, --help             Display this help message\n" +
			"  --chromosome <type>    The chromosome type to use (\"bitstring\" or \"integer\") (default: bitstring)\n" +
			"  --selection <type>     The selection method to use (\"roulette\" or \"rank\") (default: roulette)\n" +
			"  --crossover <method>   The crossover method to use (\"one_point\" or \"uniform\") (default: one_point)\n" +
			"  --mutation <method>    The mutation method to use (\"local\" or \"random\") (default: random)\n" +
			"  --population <number>  The max population size (default: 100)\n" +
			"  --generation <number>  The generation amount (default: 100)\n" +
			"  --pcrossover <number>  The crossover probability (default: 0.8)\n" +
			"  --pmutation <number>   The mutation probability (default: 0.05)\n" +
			"  --pelitism <number>    The elitism probability (default: 0.2)\n" +
			"  --runs <number>        The amount of times the algorithm will run. It'll\n" +
			"                         use a new seed automatically for each consecutive run\n" +
			"                         (default: 1)";

	/**
	 * Menampilkan pesan <i>help</i>.
	 */
	public static void printHelp() {
		System.out.println(helpString);
	}

	/**
	 * Melakukan parsing terhadap argumen CLI yang diberikan.
	 * 
	 * @param args argumen CLI
	 * @return Options opsi untuk diberikan kepada <i>genetic algorithm</i>
	 */
	public static Options parseArgs(String[] args) throws InvalidArgumentException {
		for (String arg : args) {
			if (arg.equals("--help") || arg.equals("-h")) {
				printHelp();
				System.exit(0);
			}
		}

		Path filepath = null;
		ChromosomeType chromosomeType = ChromosomeType.BITSTRING;
		SelectionMethod selectionMethod = SelectionMethod.ROULETTE;
		CrossoverMethod crossoverMethod = CrossoverMethod.ONE_POINT;
		MutationMethod mutationMethod = MutationMethod.LOCAL;
		int population = 100;
		int generations = 100;
		double pcrossover = 0.8;
		double pmutation = 0.05;
		double pelitism = 0.2;
		int runs = 1;

		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
				case "--chromosome":
					try {
						chromosomeType = ChromosomeType.valueOf(args[i + 1].toUpperCase());
					} catch (IllegalArgumentException e) {
						throw new InvalidArgumentException(String.format("Invalid chromosome type: %s", args[i + 1]));
					}
					i++;
					break;
				case "--selection":
					try {
						selectionMethod = SelectionMethod.valueOf(args[i + 1].toUpperCase());
					} catch (IllegalArgumentException e) {
						throw new InvalidArgumentException(String.format("Invalid crossover method: %s", args[i + 1]));
					}
					i++;
					break;
				case "--crossover":
					try {
						crossoverMethod = CrossoverMethod.valueOf(args[i + 1].toUpperCase());
					} catch (IllegalArgumentException e) {
						throw new InvalidArgumentException(String.format("Invalid crossover method: %s", args[i + 1]));
					}
					i++;
					break;
				case "--mutation":
					try {
						mutationMethod = MutationMethod.valueOf(args[i + 1].toUpperCase());
					} catch (IllegalArgumentException e) {
						throw new InvalidArgumentException(String.format("Invalid mutation method: %s", args[i + 1]));
					}
					i++;
					break;
				case "--population":
					try {
						population = Integer.parseInt(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(String.format(
								"Invalid population value, must be an integer greater than 0: %s", args[i + 1]));
					}

					if (population <= 0) {
						throw new InvalidArgumentException(String.format(
								"Invalid population value, must be an integer greater than 0: %s", args[i + 1]));
					}

					i++;
					break;
				case "--generation":
					try {
						generations = Integer.parseInt(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(String.format(
								"Invalid generation value, must be an integer greater than 0: %s", args[i + 1]));
					}

					if (generations <= 0) {
						throw new InvalidArgumentException(String.format(
								"Invalid generation value, must be an integer greater than 0: %s", args[i + 1]));
					}

					i++;
					break;
				case "--pcrossover":
					try {
						pcrossover = Double.parseDouble(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(String
								.format("Invalid pcrossover value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					if (pcrossover < 0 || pcrossover > 1) {
						throw new InvalidArgumentException(String
								.format("Invalid pcrossover value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					i++;
					break;
				case "--pmutation":
					try {
						pmutation = Double.parseDouble(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(String
								.format("Invalid pmutation value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					if (pmutation < 0 || pmutation > 1) {
						throw new InvalidArgumentException(String
								.format("Invalid pmuation value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					i++;
					break;
				case "--pelitism":
					try {
						pelitism = Double.parseDouble(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(String
								.format("Invalid pelitism value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					if (pelitism < 0 || pelitism > 1) {
						throw new InvalidArgumentException(String
								.format("Invalid pelistism value, must be a number between 0 and 1: %s", args[i + 1]));
					}

					i++;
					break;
				case "--runs":
					try {
						runs = Integer.parseInt(args[i + 1]);
					} catch (NumberFormatException e) {
						throw new InvalidArgumentException(
								String.format("Invalid run value, must be an non-zero integer: %s", args[i + 1]));
					}

					if (runs <= 0) {
						throw new InvalidArgumentException(
								String.format("Invalid run value, must be an non-zero integer: %s", args[i + 1]));
					}

					i++;
					break;
				default:
					if (filepath != null) {
						throw new InvalidArgumentException(String.format("Invalid argument: %s", args[i]));
					}

					filepath = Path.of(args[i]);
					if (!Files.exists(filepath)) {
						throw new InvalidArgumentException(
								String.format("File doesn't exist: %s", filepath.toString()));
					}

					if (!Files.isRegularFile(filepath)) {
						throw new InvalidArgumentException(String.format("Path isn't a file: %s", filepath.toString()));
					}

					if (!Files.isReadable(filepath)) {
						throw new InvalidArgumentException(
								String.format("Unable to read file: %s", filepath.toString()));
					}
					break;
			}
		}

		if (filepath == null) {
			printHelp();
			System.exit(0);
		}

		return new Options(
				chromosomeType,
				selectionMethod,
				crossoverMethod,
				mutationMethod,
				population,
				generations,
				pcrossover,
				pmutation,
				pelitism,
				runs,
				filepath);
	}
}
