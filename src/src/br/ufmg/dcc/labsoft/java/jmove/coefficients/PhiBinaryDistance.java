package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class PhiBinaryDistance implements ICoefficientStrategy {

	
	public String getName() {
		return "Phi Binary Distance";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return (a * d - b * c) / Math.sqrt((a + b) * (a + c) * (b + d) * (c + d));
	}

	
	public double getMinimumValue() {
		return -1;
	}

	
	public double getMaximumValue() {
		return 1;
	}
}
