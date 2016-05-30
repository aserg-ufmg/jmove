package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class DotProductCoefficientStrategy implements ICoefficientStrategy {


	public String getName() {
		return "Dot-Product";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a) / (b + c + (2 * a));
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
