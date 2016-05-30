package br.ufmg.dcc.labsoft.java.jmove.basic;

public class Parameters {

	int p;// p -> # comuns entre conjunto A e B
	int q;// q -> # que tem em A e n�o tem em B
	int r;// r -> # que tem em B e n�o tem em A
	int s;// s -> # que n�o tem em ambos (n�o precisa calcular para o Jaccard)

	public int getP() {
		return p;
	}

	public void setP(int p) {
		this.p = p;
	}

	public int getQ() {
		return q;
	}

	public void setQ(int q) {
		this.q = q;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "p -> " + p + " q -> " + q + " r -> " + r + " s -> " + s;
	}

}
