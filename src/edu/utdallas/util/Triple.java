package edu.utdallas.util;

public class Triple<F, S, T> implements Tuple {
	protected F fst;
	protected S snd;
	protected T thrd;

	public Triple(F first, S second, T third) {
		this.fst = first;
		this.snd = second;
		this.thrd = third;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * (fst != null ? fst.hashCode() : 0);
		result += prime * (snd != null ? snd.hashCode() : 0);
		result += prime * (thrd != null ? thrd.hashCode() : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Triple))
			return false;
		Triple<?,?,?> other = (Triple<?,?,?>) o;
		return fst.equals(other.fst) && snd.equals(other.snd) && thrd.equals(other.thrd);
	}
	
	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ", " + thrd + ")";
	}
}
