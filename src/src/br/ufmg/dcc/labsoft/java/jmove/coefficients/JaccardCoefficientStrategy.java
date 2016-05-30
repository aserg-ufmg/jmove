package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class JaccardCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "Jaccard";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a) / (a + b + c);
	}

	
	public double getMinimumValue() {
		return 0;
	}


	public double getMaximumValue() {
		return 1;
	}

}
