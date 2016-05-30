package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;

public class DeclareReturnDependency extends DeclareDependency {
	private final String methodNameA;
	private final IMethod ImethodA;

	public DeclareReturnDependency(String classNameA, String classNameB,
			String methodNameA, IMethod ImethodA) {
		super(classNameA, classNameB);
		this.methodNameA = methodNameA;
		this.ImethodA = ImethodA;
	}

	public String getMethodName() {
		return methodNameA;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the method '"
				+ this.methodNameA + "' whose return type is '"
				+ this.classNameB + "'";
	}

}