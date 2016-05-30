package br.ufmg.dcc.labsoft.java.jmove.suggestion;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

public class MethodHasAssigment {

	private Set<IMethod> methodsWithAssig = null;
	private static MethodHasAssigment instance = null;

	private MethodHasAssigment() {
		this.methodsWithAssig = new HashSet<IMethod>();
		// TODO Auto-generated constructor stub
	}

	public static MethodHasAssigment getInstance() {
		if (instance == null) {
			instance = new MethodHasAssigment();
		}
		return instance;
	}

	public void insertMapping(IMethod ime) {
		if (!methodsWithAssig.contains(ime)) {
			methodsWithAssig.add(ime);
		}
	}

	public boolean contains(IMethod ime) {
		if (methodsWithAssig.contains(ime))
			return true;

		return false;
	}
}
