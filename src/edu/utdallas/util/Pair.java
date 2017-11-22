package edu.utdallas.util;

public class Pair<F, S> implements Tuple {
	protected F fst;
	protected S snd;

	public Pair(F first, S second) {
		this.fst = first;
		this.snd = second;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * (fst != null ? fst.hashCode() : 0);
		result += prime * (snd != null ? snd.hashCode() : 0);
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof Pair))
			return false;
		Pair<?,?> other = (Pair<?,?>) o;
		return fst.equals(other.fst) && snd.equals(other.snd);
	}
	
	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ")";
	}
}
