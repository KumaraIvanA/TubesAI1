
import java.util.*;

public class Population {

    public ArrayList<Individual> population;
    private int maxPopulationSize;
    private int populationSize = 0;
    public double elitismPict;
    Map peta;
    Random random;
    DistanceMatrix cost;
    public int numCell;

    public Population(Random random, Map peta, int maxPopulationSize, double elitismPct) {
        this.population = new ArrayList<Individual>(); //simpan populasi dalam arraylist
        this.maxPopulationSize = maxPopulationSize; //ukuran papopulasi maksimum
        this.populationSize = populationSize; //ukuran populasi saat ini
        this.elitismPict = elitismPict; //jumlah populasi yg dipertahankan
        this.random = random; //untuk random
        this.peta = peta; //map sebagai struktur data untuk keberadaan elemen
        this.numCell = this.peta.baris * this.peta.kolom;
    }

    // generate populasi untuk populasi awal
    public void randomPopulation() {
        for (int i = 0; i < this.maxPopulationSize; i++) {
            this.addIndividual(new Individual(this.random, numCell, this.peta.fStation));
        }
    }

    public boolean addIndividual(Individual newIndividu) {
        //kalau jumlah populasi sudah penuh, jangan tambahkan individu ke dalam populasi
        if (this.populationSize >= this.maxPopulationSize) {
            return false;
        }

        //tambahkan individu baru ke dalam populasi
        this.population.add(newIndividu);
        this.populationSize++;
        return true;
    }

    // menghitung fitness untuk seluruh individu dalam 1 populasi
    // public void computeAllFitnesses() {
    //     int[] distMatrix = DistanceMatrix.computeDistanceMatrix(peta.isiMap, peta.baris, peta.kolom, peta.rumah, emptyCells);
    //     for (Individual i : population) {
	// 		double cost = DistanceMatrix.calculateCost(i.chromosome.decode(), distMatrix, peta.rumah);
    //     }		
    //     //sort individu dalam 1 populasi
    //     this.population.sort((individu1, individu2) -> individu1.compareTo(individu2));
    // }

    // membuat populasi baru dengan mempertahankan beberapa individu terbaik
    public Population getNewPopulationWithElit() {
        Population newPopulation = new Population(this.random, this.peta, this.populationSize, this.elitismPict);
        int n = (int) (this.elitismPict * this.maxPopulationSize);
        for (int i = 0; i < n; i++) {
            boolean result = newPopulation.addIndividual(this.population.get(i));
        }
        return newPopulation;
    }

    // cek apakah ppopulasi sudah penuh
    public boolean isFilled() {
        return this.maxPopulationSize == this.populationSize;
    }

    // pilih parent untuk membentuk individu baru
    public Individual[] selectParent() {
        Individual[] parents = new Individual[2];
        int top = this.population.size() + 1;
        long sumfitness = 0;
        this.population.sort((individu1, individu2) -> individu1.compareTo(individu2));

        //hitung keseluruhan fitness populasi
        for (int i = 0; i < this.population.size(); i++) {
            sumfitness = sumfitness + this.population.get(i).fitness;
        }
        // hitung probabilitas masing - masing individu untuk menjadi parent
        for (int i = 0; i < this.population.size(); i++) {
            ((Individual) this.population.get(i)).parentProbability = (1.0 * this.population.get(i).fitness) / sumfitness;
        }
        // periksa probabilitas masing-masing parent
        for (int n = 0; n < 2; n++) {
            int i = -1;
            double prob = this.random.nextDouble();
            double sum = 0.0;
            // jika sudah tidak masuk ke probabilitas, pilih parent tersebut untuk jadi parent
            do {
                i++;
                sum = sum + this.population.get(i).parentProbability;
            } while (sum < prob);
            parents[n] = this.population.get(i);
        }
        return parents;
    }

    // pilih individu terbaik
    public Individual getBestIndividu() {
        return this.population.get(0);
    }

    @Override
    public String toString() {
        String res = new String();
        for (int i = 0; i < this.population.size(); i++) {
            res = res + new String(this.population.get(i) + "\n");
        }
        return res;
    }

}
