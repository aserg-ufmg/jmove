package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import br.ufmg.dcc.labsoft.java.jmove.basic.Pair;
import br.ufmg.dcc.labsoft.java.jmove.basic.Parameters;

public class StatisticsMethod2Method {
	// parametros p,q,r,s para calculo de similaridade
	private Map<Pair<MethodJMove, MethodJMove>, Parameters> allParameters;

	public StatisticsMethod2Method(List<MethodJMove> allMethodsList) {
		// TODO Auto-generated constructor stub
		allParameters = new HashMap<Pair<MethodJMove, MethodJMove>, Parameters>();
		//createParameters(allMethodsList);
	}

//	private void createParameters(List<MethodRefine> allMethodsList) {
//		// TODO Auto-generated method stub
//		float total= allMethodsList.size();
//		System.out.println("Total de metodos "+ (int)total);
//		for (int i = 0; i < allMethodsList.size(); i++) {
//			System.out.printf("%2.2" +
//					"f %c \n" , (100)*(i/total),'%');
//			for (int j = i + 1; j < allMethodsList.size(); j++) {
//				Pair<MethodRefine, MethodRefine> pair = new Pair<MethodRefine, MethodRefine>(
//						allMethodsList.get(i), allMethodsList.get(j));
//				Parameters p = calculateParameters(allMethodsList.get(i),
//						allMethodsList.get(j));
//				allParameters.put(pair, p);
//
//			}
//		}
//
//		// ##############
////		Iterator<Pair<Method, Method>> it = allParameters.keySet().iterator();
////		while (it.hasNext()) {
////			Pair<Method, Method> key = it.next();
////			System.out.println(key);
////			System.out.println(allParameters.get(key));
////			System.out.println();
////
////		}
////
////		for (Integer i : AllDependenciesMethods.getInstance()
////				.getAllDependenciesMethodID()) {
////			System.out.println(i + " "
////					+ AllEntitiesMapping.getInstance().getByID(i));
////
////		}
////		System.out.println(AllDependenciesMethods.getInstance()
////				.getAllDependenciesMethodID().size()
////				+ " numero total");
////
////		System.out.println("\nStatistics FINISH\n\n");
//
//		// ########
//
//	}

	public Parameters calculateParameters(MethodJMove method1, MethodJMove method2) {
		// TODO Auto-generated method stub

		Parameters parameters = new Parameters();

		int p = inBoth(method1.getMethodsDependencies(),
				method2.getMethodsDependencies());
		parameters.setP(p);
		int q = inFirstOnly(method1.getMethodsDependencies(),
				method2.getMethodsDependencies());
		parameters.setQ(q);
		int r = inSecondOnly(method1.getMethodsDependencies(),
				method2.getMethodsDependencies());
		parameters.setR(r);
		int s = inNone(method1.getMethodsDependencies(),
				method2.getMethodsDependencies());
		parameters.setS(s);

		return parameters;
	}

	private int inBoth(Set<Integer> ConjuntoA, Set<Integer> ConjuntoB) {
		// TODO Auto-generated method stub

		int tamA = ConjuntoA.size();
		int tamB = ConjuntoB.size();
		Set<Integer> union = new HashSet<Integer>();

		union.addAll(ConjuntoA);
		union.addAll(ConjuntoB);

		int tamEnd = union.size();

		return tamA + tamB - tamEnd;
	}

	private int inFirstOnly(Set<Integer> ConjuntoA, Set<Integer> ConjuntoB) {
		// TODO Auto-generated method stub

		Set<Integer> union = new HashSet<Integer>(ConjuntoB);

		int tamBegin = union.size();
		union.addAll(ConjuntoA);
		int tamEnd = union.size();

		return tamEnd - tamBegin;

	}

	private int inSecondOnly(Set<Integer> ConjuntoA, Set<Integer> ConjuntoB) {
		// TODO Auto-generated method stub

		Set<Integer> union = new HashSet<Integer>(ConjuntoA);

		int tamBegin = union.size();
		union.addAll(ConjuntoB);
		int tamEnd = union.size();

		return tamEnd - tamBegin;
	}

	private int inNone(Set<Integer> ConjuntoA, Set<Integer> ConjuntoB) {
		// TODO Auto-generated method stub
		int cont = 0;
		Set<Integer> union = new HashSet<Integer>();
		Set<Integer> allDepenciesSet = AllDependenciesMethods.getInstance()
				.getAllDependenciesMethodID();

		union.addAll(ConjuntoA);
		union.addAll(ConjuntoB);

		for (Integer ID : allDepenciesSet) {
			if (!union.contains(ID)) {
				cont++;
			}
		}

		return cont;
	}

	public Map<Pair<MethodJMove, MethodJMove>, Parameters> getAllParameters() {
		return allParameters;
	}

}
