package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class BaroniUrbaniCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "BaroniUrbani";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return (Math.sqrt(a * d) + a) / (Math.sqrt(a * d) + a + b + c);
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
