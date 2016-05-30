package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class SokalBinaryDistanceCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Sokal Binary Distance";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return Math.sqrt(((double) b + c) / (a + b + c + d));
	}
	
	
	public double getMinimumValue() {
		return 1;
	}

	
	public double getMaximumValue() {
		return 0;
	}

}
