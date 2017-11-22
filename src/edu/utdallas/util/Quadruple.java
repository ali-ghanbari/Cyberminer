package edu.utdallas.util;

public class Quadruple<F, S, T, L> implements Tuple {
	protected F fst;
	protected S snd;
	protected T thrd;
	protected L frth;

	public Quadruple(F first, S second, T third, L fourth) {
		this.fst = first;
		this.snd = second;
		this.thrd = third;
		this.frth = fourth;
	}

	public F first() {
		return fst;
	}
	
	public void setFirst(F first) {
		this.fst = first;
	}

	public S second() {
		return snd;
	}
	
	public void setSecond(S second) {
		this.snd = second;
	}
	
	public T third() {
		return thrd;
	}
	
	public void setThird(T third) {
		this.thrd = third;
	}
	
	public L fourth() {
		return frth;
	}
	
	public void setFourth(L fourth) {
		this.frth = fourth;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * (fst != null ? fst.hashCode() : 0);
		result += prime * (snd != null ? snd.hashCode() : 0);
		result += prime * (thrd != null ? thrd.hashCode() : 0);
		result += prime * (frth != null ? frth.hashCode() : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Quadruple))
			return false;
		Quadruple<?,?,?,?> other = (Quadruple<?,?,?,?>) o;
		return fst.equals(other.fst) && snd.equals(other.snd) && thrd.equals(other.thrd) && frth.equals(other.frth);
	}
	
	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ", " + thrd + ", " + frth + ")";
	}
}