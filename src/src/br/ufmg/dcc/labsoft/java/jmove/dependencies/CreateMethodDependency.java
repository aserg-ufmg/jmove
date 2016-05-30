package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;

public class CreateMethodDependency extends CreateDependency {
	private String methodNameA;
	private final IMethod ImethodA;

	public String getMethodNameA() {
		return methodNameA;
	}

	public CreateMethodDependency(String classNameA, String classNameB,
			String methodNameA, IMethod ImethodA) {
		super(classNameA, classNameB);
		this.methodNameA = methodNameA;
		this.ImethodA = ImethodA;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	public String toString() {
		return "'" + this.classNameA + "' contains the method '"
				+ this.methodNameA + "' that creates an object of '"
				+ this.classNameB + "'";
	}

}