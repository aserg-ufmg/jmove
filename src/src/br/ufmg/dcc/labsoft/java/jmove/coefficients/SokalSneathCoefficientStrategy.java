package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class SokalSneathCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "Sokal and Sneath";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return (2.0 * (a+d)) / (2*(a+d) + b + c);
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
