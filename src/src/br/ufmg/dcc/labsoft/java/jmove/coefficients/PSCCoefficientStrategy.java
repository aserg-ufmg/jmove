package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class PSCCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "PSC";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return Math.pow(a,2) / ((b+a) * (c+a));
	}

	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}
	
}
