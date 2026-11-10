import java.util.Random;

public class Individual implements Comparable<Individual> {
    public static final int CROSSOVER_ONE_POINT = 0;
    public static final int CROSSOVER_UNIFORM = 1;
    public static final int MUTATION_RANDOM = 0;
    public static final int MUTATION_LOCAL = 1; // geser satu FS ke sel kosong yang bertetangga

    public BitStringChromosome chromosome; // kromosom: objek BitStringChromosome (true = sel kosong nomor k berisi FS)
    public double cost; // rata-rata jarak rumah ke FS terdekat (makin kecil makin baik)
    public double fitness; // nilai fitness
    public Random MyRand;
    public double parentProbability;// probabilitas terpilih sbg parent
    public int p; // jumlah FS yang harus ada

    // membuat individu acak: tepat p bit bernilai true
    public Individual(Random MyRand, int numCells, int p) {
        this.chromosome = BitStringChromosome.generateRandom(numCells, p, MyRand); // bungkus bit menjadi kromosom
    }

    // membuat individu baru berdasarkan kromosom dari luar
    public Individual(Random MyRand, BitStringChromosome chromosome, int p) {
        this.MyRand = MyRand;
        this.chromosome = chromosome;
        this.p = p;
    }

    // menghitung cost dan fitness
    public double setFitness(char[][] distMatrix, int numHouses) {
		int[] cells = chromosome.decode();
        this.fitness = 1.0 / (1.0 + this.cost); // cost kecil -> fitness besar
        return this.fitness;
    }

    public double getFitness() {
        return this.fitness;
    }
    
    // Tepat satu FS berpindah, bit di kromosom ini diubah langsung
    public void doMutation(int type, Map map) {
        if (type == MUTATION_RANDOM) {
            this.chromosome = chromosome.mutateRandom(); // pindah ke sel kosong mana saja
        } else if (type == MUTATION_LOCAL) {
            this.chromosome = chromosome.mutateLocal(map); // pindah ke sel kosong mana saja
        } else {
            throw new IllegalArgumentException("jenis mutasi tidak dikenal");
        }
    }

    public Individual[] doCrossover(Individual other, int type) {
		BitStringChromosome[] childrens;
		if (type == CROSSOVER_ONE_POINT) {
			childrens = chromosome.crossoverSingle(other.chromosome);
		} else if (type == CROSSOVER_UNIFORM) {
			childrens = chromosome.crossoverUniform(other.chromosome);
		} else {
            throw new IllegalArgumentException("jenis crossover tidak dikenal");
		}

		Individual[] newIndividuals = new Individual[2];
		newIndividuals[0] = new Individual(this.MyRand, childrens[0], p);
		newIndividuals[1] = new Individual(this.MyRand, childrens[1], p);
		return newIndividuals;
    }

    @Override
    public int compareTo(Individual other) { // fitness terbesar di depan
        if (this.fitness > other.fitness) {
            return -1;
        } else if (this.fitness < other.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        boolean[] bits = chromosome.getBitString(); // bit kromosom
        StringBuilder sb = new StringBuilder("cost=" + cost + " cells=");
        for (int i = 0; i < bits.length; i++) { // tulis nomor sel yang berisi FS
            if (bits[i]) {
                sb.append(i).append(' ');
            }
        }
        return sb.toString();
    }
}
