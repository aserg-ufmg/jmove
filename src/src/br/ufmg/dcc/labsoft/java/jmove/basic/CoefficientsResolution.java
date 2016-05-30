package br.ufmg.dcc.labsoft.java.jmove.basic;

import java.util.ArrayList;
import java.util.List;

import br.ufmg.dcc.labsoft.java.jmove.coefficients.BaroniUrbaniCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.DotProductCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.HamannCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.ICoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.JaccardCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.KulczynskiCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.OchiaiCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.PSCCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.PhiBinaryDistance;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.RelativeMatchingCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.RogersTanimotoCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.RussellRaoCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SMCCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SokalBinaryDistanceCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SokalSneath2CoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SokalSneath4CoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SokalSneathCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.SorensonCoefficientStrategy;
import br.ufmg.dcc.labsoft.java.jmove.coefficients.YuleCoefficientStrategy;

public class CoefficientsResolution {

	public static enum CoefficientStrategy {
		Jaccard, SMC, Yule, Hamann, Sorenson, RogersTanimoto, SokalSneath, RussellRao, BaroniUrbani, SokalBinary, Ochiai, DotProduct, Kulczynski, PhiBinary, PSC, RelativeMatching, SokalSneath2, SokalSneath4
	};

	CoefficientStrategy currentStrategy = null;
	private static final ICoefficientStrategy[] coefficientStrategies = {
			new JaccardCoefficientStrategy(), new SMCCoefficientStrategy(),
			new YuleCoefficientStrategy(), new HamannCoefficientStrategy(),
			new SorensonCoefficientStrategy(),
			new RogersTanimotoCoefficientStrategy(),
			new SokalSneathCoefficientStrategy(),
			new RussellRaoCoefficientStrategy(),
			new BaroniUrbaniCoefficientStrategy(),
			new SokalBinaryDistanceCoefficientStrategy(),
			new OchiaiCoefficientStrategy(),
			new DotProductCoefficientStrategy(),
			new KulczynskiCoefficientStrategy(), new PhiBinaryDistance(),
			new PSCCoefficientStrategy(),
			new RelativeMatchingCoefficientStrategy(),
			new SokalSneath2CoefficientStrategy(),
			new SokalSneath4CoefficientStrategy() };

	private static ICoefficientStrategy coefficientStrategy;

	public CoefficientsResolution() {
		// TODO Auto-generated constructor stub
	}

	public double calculate(Parameters parameters, CoefficientStrategy strategy) {
		// TODO Auto-generated method stub

		if (currentStrategy != strategy) {
			defineStrategy(strategy);
		}

		int a = parameters.getP();
		int b = parameters.getQ();
		int c = parameters.getR();
		int d = parameters.getS();

		double coefficient = coefficientStrategy.calculate(a, b, c, d);
		if (Double.isNaN(coefficient))
			return 0;

		return coefficient;
	}

	private void defineStrategy(CoefficientStrategy strategy) {
		switch (strategy) {
		case Jaccard:
			coefficientStrategy = coefficientStrategies[0];
			if (!(coefficientStrategy instanceof JaccardCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case SMC:
			coefficientStrategy = coefficientStrategies[1];
			if (!(coefficientStrategy instanceof SMCCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case Yule:
			coefficientStrategy = coefficientStrategies[2];
			if (!(coefficientStrategy instanceof YuleCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case Hamann:
			coefficientStrategy = coefficientStrategies[3];
			if (!(coefficientStrategy instanceof HamannCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case Sorenson:
			coefficientStrategy = coefficientStrategies[4];
			if (!(coefficientStrategy instanceof SorensonCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case RogersTanimoto:
			coefficientStrategy = coefficientStrategies[5];
			if (!(coefficientStrategy instanceof RogersTanimotoCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case SokalSneath:
			coefficientStrategy = coefficientStrategies[6];
			if (!(coefficientStrategy instanceof SokalSneathCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case RussellRao:
			coefficientStrategy = coefficientStrategies[7];
			if (!(coefficientStrategy instanceof RussellRaoCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case BaroniUrbani:
			coefficientStrategy = coefficientStrategies[8];
			if (!(coefficientStrategy instanceof BaroniUrbaniCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case SokalBinary:
			coefficientStrategy = coefficientStrategies[9];
			if (!(coefficientStrategy instanceof SokalBinaryDistanceCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case Ochiai:
			coefficientStrategy = coefficientStrategies[10];
			if (!(coefficientStrategy instanceof OchiaiCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case DotProduct:
			coefficientStrategy = coefficientStrategies[11];
			if (!(coefficientStrategy instanceof DotProductCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case Kulczynski:
			coefficientStrategy = coefficientStrategies[12];
			if (!(coefficientStrategy instanceof KulczynskiCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case PhiBinary:
			coefficientStrategy = coefficientStrategies[13];
			if (!(coefficientStrategy instanceof PhiBinaryDistance))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case PSC:
			coefficientStrategy = coefficientStrategies[14];
			if (!(coefficientStrategy instanceof PSCCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case RelativeMatching:
			coefficientStrategy = coefficientStrategies[15];
			if (!(coefficientStrategy instanceof RelativeMatchingCoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case SokalSneath2:
			coefficientStrategy = coefficientStrategies[16];
			if (!(coefficientStrategy instanceof SokalSneath2CoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		case SokalSneath4:
			coefficientStrategy = coefficientStrategies[17];
			if (!(coefficientStrategy instanceof SokalSneath4CoefficientStrategy))
				throw new Error("Usando coeficiente errado para calculo");
			break;
		default:
			break;

		}
	}

	public static List<CoefficientStrategy> AllCoefficientStrategy() {

		List<CoefficientStrategy> list = new ArrayList<CoefficientStrategy>();
		list.add(CoefficientStrategy.SokalSneath2);
		     list.add(CoefficientStrategy.Jaccard);
		 //list.add(CoefficientStrategy.SMC);
		 //list.add(CoefficientStrategy.Yule);
		 //list.add(CoefficientStrategy.Hamann);
		     list.add(CoefficientStrategy.Sorenson);
		 //list.add(CoefficientStrategy.RogersTanimoto);
		 //list.add(CoefficientStrategy.SokalSneath);
		 //list.add(CoefficientStrategy.RussellRao);
		 //list.add(CoefficientStrategy.BaroniUrbani);
		 //list.add(CoefficientStrategy.SokalBinary);
		     list.add(CoefficientStrategy.Ochiai);
		     list.add(CoefficientStrategy.DotProduct);
		     list.add(CoefficientStrategy.Kulczynski);
		 //list.add(CoefficientStrategy.PhiBinary);
		     list.add(CoefficientStrategy.PSC);
		 //list.add(CoefficientStrategy.RelativeMatching);
		 //list.add(CoefficientStrategy.SokalSneath4);

		return list;

	}

}
