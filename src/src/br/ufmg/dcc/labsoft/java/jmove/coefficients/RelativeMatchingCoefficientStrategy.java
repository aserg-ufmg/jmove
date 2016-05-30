package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class RelativeMatchingCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Relative Matching";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return (((double) a) + Math.sqrt(a * d)) / (a + b + c + d + Math.sqrt(a * d));
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}
}
