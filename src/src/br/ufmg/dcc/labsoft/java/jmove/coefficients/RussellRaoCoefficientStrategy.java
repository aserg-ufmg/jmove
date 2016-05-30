package br.ufmg.dcc.labsoft.java.jmove.coefficients;


public class RussellRaoCoefficientStrategy implements ICoefficientStrategy {
	
	
	public String getName() {
		return "Russell and Rao";
	}
	
	
	public double calculate(int a, int b, int c, int d) {
		return ((double) a) / (a + b + c + d);
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
