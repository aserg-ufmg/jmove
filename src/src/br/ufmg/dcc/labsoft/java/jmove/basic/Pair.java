package br.ufmg.dcc.labsoft.java.jmove.basic;


// Class Pair whose consider that (p1,p2) == (p2,p1)  if they are same type (A==B)
public class Pair<A, B> { 
	private A first;
	private B second;

	public Pair(A first, B second) {
		super();
		this.first = first;
		this.second = second;
	}
	
	public Pair() {
		super();
		this.first = null;
		this.second = null;
	}

	@Override
	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair<?, ?> otherPair = (Pair<?, ?>) other;
			
			if ((this.first.equals(otherPair.first)
					&& this.second.equals(otherPair.second)
					)|| (this.first.equals(otherPair.second)
					&& this.second.equals(otherPair.first))) {
				return true;
			}
	
			return ((this.first == otherPair.first || (this.first != null
					&& otherPair.first != null && this.first
						.equals(otherPair.first))) && (this.second == otherPair.second || (this.second != null
					&& otherPair.second != null && this.second
						.equals(otherPair.second))));
		}

		return false;
	}

	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}
}