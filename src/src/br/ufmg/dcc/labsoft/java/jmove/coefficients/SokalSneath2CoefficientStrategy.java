package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class SokalSneath2CoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Sokal and Sneath 2";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a) / (a + 2 * (b + c));
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	public double getMaximumValue() {
		return 1;
	}
}
