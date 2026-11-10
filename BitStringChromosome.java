import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class BitStringChromosome extends Chromosome<BitStringChromosome> {
	private boolean[] bitString;
	private int toggles;
	private Random rand;

	public void print() {
		for (boolean b: bitString) {
			System.out.print(b ? 1 : 0);
		}
		System.out.println();
	}

	public BitStringChromosome(int toggles, boolean[] bitString) {
		this.bitString = bitString;
		this.toggles = toggles;
	}

	public BitStringChromosome crossoverSingle(BitStringChromosome other) {
		int cutoff = rand.nextInt();

		boolean[] childBits1 = new boolean[bitString.length];
		boolean[] child2 = new boolean[bitString.length];
		for (int i = 0; i < cutoff; i++) {
			childBits1[i] = bitString[i];
			child2[i] = other.getBitString()[i];
		}

		for (int i = cutoff; i < bitString.length; i++) {
			childBits1[i] = other.getBitString()[i];
			child2[i] = bitString[i];
		} 

		repair(childBits1);

		return new BitStringChromosome(toggles, childBits1);
	}

	public BitStringChromosome crossoverUniform(BitStringChromosome other) {
		boolean[] childBits = new boolean[bitString.length];
		for (int i = 0; i < bitString.length; i++) {
			if (bitString[i] == other.getBitString()[i]) {
				childBits[i] = bitString[i];
			} else {
				childBits[i] = rand.nextBoolean();
			}
		}
		
		repair(childBits);

		return new BitStringChromosome(toggles, childBits);
	}

	public BitStringChromosome mutateRandom() {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == toggles) {
			return new BitStringChromosome(toggles, bitString);
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
		int chosenIndex = rand.nextInt(chosenIndexes.size());
		int from = chosenIndexes.get(chosenIndex);

		// Pilih petak yang tidak berisi FS
		int emptyIndex = rand.nextInt(emptyIndexes.size());
		int to = emptyIndexes.get(emptyIndex);

		// Buat array duplikat dan tukar FS
		boolean[] newBitString = bitString.clone();
		newBitString[from] = false;
		newBitString[to] = true;

		return new BitStringChromosome(toggles, newBitString);
	}

	public BitStringChromosome mutateLocal(Map map) {
		// Return awal jika semua petak berisi FS.
		if (bitString.length == toggles) {
			return new BitStringChromosome(toggles, bitString);
		}

		int rows = map.baris;
		int cols = map.kolom;

		int from = rand.nextInt(bitString.length);
		while (!bitString[from]) {
			from = rand.nextInt(bitString.length);
		}

		int row = from / rows;
		int col = from % rows;

		List<Integer> choices = new ArrayList<Integer>();
		int candidate = from - rows;
		if (row > 0 && map.get(candidate) == '.') {
			choices.add(candidate);
		}

		candidate = from - 1;
		if (col > 0 && map.get(candidate) == '.') {
			choices.add(candidate);
		}

		candidate = from + 1;
		if (row < rows - 1 && map.get(candidate) == '.') {
			choices.add(candidate);
		}

		candidate = from + rows;
		if (col < cols - 1 && map.get(candidate) == '.') {
			choices.add(from + rows);
		}

		int to = choices.get(rand.nextInt(choices.size()));

		// Buat array duplikat dan tukar FS
		boolean[] newBitString = bitString.clone();
		newBitString[from] = false;
		newBitString[to] = true;

		return new BitStringChromosome(toggles, newBitString);
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

	private void repair(boolean[] bits) {
		int ones = 0;
		for (boolean bit : bits) {
			if (bit) {
				ones++;
			}
		}

		while (ones > toggles) {
			int i = rand.nextInt(bits.length);
			if (bits[i]) {
				bits[i] = false;
				ones--;
			}
		}

		while (ones < toggles) {
			int i = rand.nextInt(bits.length);
			if (!bits[i]) {
				bits[i] = true;
				ones++;
			}
		}
	}

	public boolean[] getBitString() {
		return bitString;
	}

	public static BitStringChromosome generateRandom(int length, int toggles, Random r) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < length; i++) {
			indexes.add(i);
		}

		Collections.shuffle(indexes, r);

		boolean[] bitString = new boolean[length];
		for (int i = 0; i < toggles; i++) {
			bitString[indexes.get(i)] = true;
		}

		return new BitStringChromosome(toggles, bitString);
	}
}
