package com.firestationga.algorithm;

public abstract class Chromosome<C extends Chromosome<C>> {
	public abstract C[] crossoverSingle(C other);
	public abstract C[] crossoverUniform(C other);
	public abstract C mutateRandom();
	public abstract C mutateLocal();
	public abstract int[] decode();
}
