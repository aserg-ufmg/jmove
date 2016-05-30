package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class SokalSneath4CoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Sokal and Sneath 4";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return ((((double) a) / (a + b) + ((double) a) / (a + c) + ((double) d) / (b + d) + ((double) d) / (c + d))) / 4.0;
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}
}
