package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class YuleCoefficientStrategy implements ICoefficientStrategy {
	
	public String getName() {
		return "Yule";
	}
	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a*d - b*c) / (a*d + b*c);
	}

	public double getMinimumValue() {
		return -1;
	}

	public double getMaximumValue() {
		return 1;
	}
	
}
