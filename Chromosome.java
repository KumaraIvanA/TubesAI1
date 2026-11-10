public abstract class Chromosome<C extends Chromosome<C>> {
	public abstract C crossoverSingle(C other);
	public abstract C mutateRandom();
	public abstract C mutateLocal(Map map);
	public abstract int[] decode();
}
