
import java.util.*;

public class Population {

    public ArrayList<Individual> population;
    private int maxPopulationSize;
    private int populationSize = 0;
    public double elitismPict;
    Map isiMap;
    int sumRank = 0;
    Random random;

    public Population(Random random, Map isiMap, int maxPopulationSize, double elitismPct) {
        this.population = new ArrayList<Individual>();
        this.maxPopulationSize = maxPopulationSize;
        this.populationSize = populationSize;
        this.elitismPict = elitismPict;
        this.sumRank = sumRank;
        this.random = random;
        this.isiMap = isiMap;
        for (int i = 1; i <= this.maxPopulationSize; i++) {
            this.sumRank = this.sumRank + i;
        }
    }

    public void randomPopulation() {
        for (int i = 0; i < this.maxPopulationSize; i++) {
            this.addIndividual(new Individual(this.random));
        }
    }

    public boolean addIndividual(Individual newIndividu) {
        if (this.populationSize >= this.maxPopulationSize) {
            return false;
        }

        this.population.add(newIndividu);
        this.populationSize++;
        return true;
    }

    public void computeAllFitnesses() {
        for (int i = 0; i < this.populationSize; i++) {
            ((Individual) this.population.get(i)).setFitness(this.isiMap, this.maxCapacity);
        }
        this.population.sort((individu1, individu2) -> individu1.compareTo(individu2));
    }

    public Population getNewPopulationWithElit() {
        Population newPopulation = new Population(this.random, this.isiMap, this.populationSize, this.elitismPict);
        int n = (int) (this.elitismPict * this.maxPopulationSize);
        for (int i = 0; i < n; i++) {
            boolean result = newPopulation.addIndividual(this.population.get(i));
        }
        return newPopulation;
    }

    public boolean isFilled() {
        return this.maxPopulationSize == this.populationSize;
    }

    public Individual[] selectParent() {
        Individual[] parents = new Individual[2];
        int top = this.population.size() + 1;
        long sumfitness = 0;

        for (int i = 0; i < this.population.size(); i++) {
            sumfitness = sumfitness + this.population.get(i).fitness;
        }

        for (int i = 0; i < this.population.size(); i++) {
            ((Individual) this.population.get(i)).parentProbability = (1.0 * this.population.get(i).fitness) / sumfitness;
        }

        for (int n = 0; n < 2; n++) {
            int i = -1;
            double prob = this.random.nextDouble();
            double sum = 0.0;
            do {
                i++;
                sum = sum + this.population.get(i).parentProbability;
            } while (sum < prob);
            parents[n] = this.population.get(i);
        }
        return parents;
    }

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
