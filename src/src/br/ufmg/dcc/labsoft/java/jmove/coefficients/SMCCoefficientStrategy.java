package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class SMCCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "SMC";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return ((double) (a + d)) / (a + b + c + d);
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
