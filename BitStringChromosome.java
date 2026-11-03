import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BitStringChromosome {
	private boolean[] bitString;

	public BitStringChromosome(boolean[] bitString) {
		this.bitString = bitString;
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

		boolean[] bitString = new boolean[length];
		for (int i = 0; i < toggles; i++) {
			bitString[i] = true;
		}

		return new BitStringChromosome(bitString);
	}
}
