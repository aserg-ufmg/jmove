package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class RogersTanimotoCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "Rogers and Tanimoto";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a+d) / (a + 2 * (b+c) + d);
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
