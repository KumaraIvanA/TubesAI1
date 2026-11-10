
import java.util.Random;

public class GeneticAlgorithm {
    public static final double ELITISM_PCT = 0.2;
    Random random;
    Map isiMap;
    public int maxPopulationSize;
    public double crossoverRate;
    public double mutationRate;
    public int totalGeneration;
    public int chromosomeType;
    public int crossoverType;
    public int mutationType;

    public GeneticAlgorithm(Random random, Map isiMap, int maxPopulationSize, double crossoverRate, double mutationRate,
            int totalGeneration, int chromosomeType, int crossoverType, int mutationType) {
        this.random = random;
        this.isiMap = isiMap;
        this.maxPopulationSize = maxPopulationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.totalGeneration = totalGeneration;
        this.chromosomeType = chromosomeType;
        this.crossoverType = crossoverType;
        this.mutationType = mutationType;

    }

    public Individual run() {
        // untuk populasi awal (generasi pertama)
        int generation = 1;
        Population currentPopulation = new Population(this.random, this.isiMap, this.maxPopulationSize, ELITISM_PCT);
        currentPopulation.randomPopulation();
        currentPopulation.computeAllFitnesses();

        // lakukan selama generasi belum mencapai batas
        while (terminate(generation) == false) {
            // buat populasi baru
            Population newPopulation = currentPopulation.getNewPopulationWithElit();

            while (newPopulation.isFilled() == false) {
                // pilih 2 parent untuk membuat individu baru
                Individual[] parents = currentPopulation.selectParent();

                // kalau probabilitas masuk untuk dilakukan crossover
                if (this.random.nextDouble() < this.crossoverRate) {
                    //lakukan crossover antar parent pertama dan kedua
                    Individual child1 = parents[0].doCrossover(parents[1], crossoverType)[0];
                    Individual child2 = parents[0].doCrossover(parents[1], crossoverType)[1];
                    // kalau probabilitas masuk untuk dilakukan mutasi
                    if (this.random.nextDouble() < this.mutationRate) {
                        child1.doMutation(mutationType, isiMap);
                        child2.doMutation(mutationType, isiMap);
                    }
                    // tambahkan individu ke populasi baru
                    newPopulation.addIndividual(child1);
                    newPopulation.addIndividual(child2);
                }
            }
            generation++;
            currentPopulation = newPopulation;
            currentPopulation.computeAllFitnesses();
        }
        return currentPopulation.getBestIndividu();
    }

    //untuk cek apakah generasi saat ini sudah melewati batas
    public boolean terminate(int generation) {
        if (generation >= this.totalGeneration) {
            return true; 
        }else {
            return false;
        }
    }

}
