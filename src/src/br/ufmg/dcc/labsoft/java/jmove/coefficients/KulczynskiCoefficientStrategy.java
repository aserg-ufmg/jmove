package br.ufmg.dcc.labsoft.java.jmove.coefficients;

public class KulczynskiCoefficientStrategy implements ICoefficientStrategy {

	
	public String getName() {
		return "Kulczynski";
	}

	
	public double calculate(int a, int b, int c, int d) {
		return (((double) a) / (a + b) + ((double) a) / (a + c)) / 2.0;
	}
	
	
	public double getMinimumValue() {
		return 0;
	}

	
	public double getMaximumValue() {
		return 1;
	}

}
