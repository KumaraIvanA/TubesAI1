import java.util.Random;

public class GenerativeAlgorithm {
    Random random;
    Map isiMap;
    public int maxPopulationSize;
    public double crossoverRate;
    public double mutationRate;
    public double elitismPct;
    public int totalGeneration;

    public GenerativeAlgorithm(Random random, Map isiMap, int maxPopulationSize, double crossoverRate, double mutationRate,
            int totalGeneration) {
        this.random = random;
        this.isiMap = isiMap;
        this.maxPopulationSize = maxPopulationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitismPct = elitismPct;
        this.totalGeneration = totalGeneration;
    }

    public Individual run() {
        // untuk populasi awal (generasi pertama)
        int generation = 1;
        Population currentPopulation = new Population (this.random, this.isiMap, this.maxPopulationSize, this.elitismPct);
        currentPopulation.randomPopulation();
        currentPopulation.computeAllFitnesses();

        // lakukan selama generasi belum mencapai batas
        while (terminate(generation)==false) {
            // buat populasi baru
            Population newPopulation = currentPopulation.getNewPopulationWithElit();

            while (newPopulation.isFilled()==false) {
                // pilih 2 parent untuk membuat individu baru
                Individual[] parents = currentPopulation.selectParent();

                // kalau probabilitas masuk untuk dilakukan crossover
                if (this.random.nextDouble()<this.crossoverRate) {
                    //lakukan crossover antar parent pertama dan kedua
                    Individual child = parents[0].doCrossover(parents[1]);
                    // kalau probabilitas masuk untuk dilakukan mutasi
                    if (this.random.nextDouble()<this.mutationRate) {
                        child.doMutation();
                    }
                    // tambahkan individu ke populasi baru
                    newPopulation.addIndividual(child);
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
        if (generation>=this.totalGeneration) return true;
        else return false;
    }
    
}
