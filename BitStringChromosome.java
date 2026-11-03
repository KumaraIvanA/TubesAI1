import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BitStringChromosome extends Chromosome<BitStringChromosome> {
	private Boolean[] bitString;
	private int toggles;

	public BitStringChromosome(int toggles, Boolean[] bitString) {
		this.bitString = bitString;
		this.toggles = toggles;
	}

	public BitStringChromosome crossover(BitStringChromosome other) {
		// ...
		return new BitStringChromosome(toggles, bitString);
	}

	public BitStringChromosome mutate() {
		// ...
		return new BitStringChromosome(toggles, bitString);
	}

	public int[] decode() {
		int[] result = new int[toggles];

		int resultCounter = 0;
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i]) {
				result[resultCounter] = i;
				resultCounter++;
			}
		}

		return result;
	}

	public boolean[] getBitString() {
		return bitString;
	}

	// ini ga random (?)
	public static BitStringChromosome generateRandom(int length, int toggles, int seed) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			indexes.add(i);
		}

		Random r = new Random(seed);
		Collections.shuffle(indexes, r);

		Boolean[] bitString = new Boolean[length];
		for (int i = 0; i < toggles; i++) {
			bitString[indexes.get(i)] = true;
		}

		return new BitStringChromosome(toggles, bitString);
	}

}
