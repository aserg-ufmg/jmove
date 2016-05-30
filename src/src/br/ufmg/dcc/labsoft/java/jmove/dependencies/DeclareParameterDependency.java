package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IVariableBinding;

public final class DeclareParameterDependency extends DeclareDependency {
	private final String VariableNameA;
	private final String methodNameA;
	private final IMethod ImethodA;
	private final IVariableBinding iVariableA;

	public DeclareParameterDependency(String classNameA, String classNameB,
			String methodNameA, String fieldNameA, final IMethod ImethodA,
			IVariableBinding iVariableA) {
		super(classNameA, classNameB);
		this.VariableNameA = fieldNameA;
		this.methodNameA = methodNameA;
		this.ImethodA = ImethodA;
		this.iVariableA = iVariableA;
	}

	public String getVariableName() {
		return this.VariableNameA;
	}

	public String getMethodName() {
		return methodNameA;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	public IVariableBinding getiVariableBinding() {
		return iVariableA;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the formal parameter '"
				+ this.VariableNameA + "' in method '" + this.methodNameA
				+ "' whose type is '" + this.classNameB + "'";
	}

}