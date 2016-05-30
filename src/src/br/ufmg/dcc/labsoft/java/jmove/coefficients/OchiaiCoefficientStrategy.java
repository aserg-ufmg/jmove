package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class OchiaiCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Ochiai";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return a / Math.sqrt((a + b) * (a + c));
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
