package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class SorensonCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "Sorenson";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return (2.0 * a) / ( (a+b) + (a+c) );
	}

	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}
	
}
