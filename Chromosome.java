public abstract class Chromosome<C extends Chromosome<C>> {
	public abstract C crossover(C other);
	public abstract C mutate();
	public abstract int[] decode();
}
