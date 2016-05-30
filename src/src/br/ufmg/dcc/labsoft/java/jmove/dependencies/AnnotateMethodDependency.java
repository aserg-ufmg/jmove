package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;

public final class AnnotateMethodDependency extends AnnotateDependency {
	private final String methodNameA;
	private final IMethod ImethodA;

	public AnnotateMethodDependency(String classNameA, String classNameB,
			String methodNameA, IMethod ImethodA) {
		super(classNameA, classNameB);
		this.methodNameA = methodNameA;
		this.ImethodA = ImethodA;
	}

	public String getMethodName() {
		return this.methodNameA;
	}

	
	public IMethod getImethodA() {
		return ImethodA;
	}

	public String toString() {
		return "'" + this.classNameA + "' contains the method '"
				+ this.methodNameA + "' which is annotated by '"
				+ this.classNameB + "'";
	}

}