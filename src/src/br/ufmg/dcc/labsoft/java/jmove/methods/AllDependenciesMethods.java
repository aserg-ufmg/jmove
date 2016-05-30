package br.ufmg.dcc.labsoft.java.jmove.methods;

import java.util.HashSet;
import java.util.Set;

public class AllDependenciesMethods {

	private static Set<Integer> allDependenciesMethodID;
	private static AllDependenciesMethods instance = null;

	private AllDependenciesMethods() {
		// TODO Auto-generated constructor stub
		allDependenciesMethodID = new HashSet<Integer>();

	}

	public Set<Integer> getAllDependenciesMethodID() {

		return allDependenciesMethodID;

	}

	public static AllDependenciesMethods getInstance() {
		if (instance == null) {
			instance = new AllDependenciesMethods();
		}
		return instance;
	}

}
