package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.ITypeBinding;

public final class DeclareLocalVariableDependency extends DeclareDependency {
	private final String fieldNameA;
	private final String methodNameA;
	private final IMethod ImethodA;
	private final ITypeBinding iTypeBinding;

	public DeclareLocalVariableDependency(String classNameA, String classNameB,
			String methodNameA, String fieldNameA, IMethod ImethodA,
			ITypeBinding iTypeBinding) {
		super(classNameA, classNameB);
		this.fieldNameA = fieldNameA;
		this.methodNameA = methodNameA;
		this.ImethodA = ImethodA;
		this.iTypeBinding = iTypeBinding;
	}

	public String getFieldName() {
		return this.fieldNameA;
	}

	public String getMethodName() {
		return methodNameA;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	public ITypeBinding getiTypeBinding() {
		return iTypeBinding;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the local variable '"
				+ this.fieldNameA + "' in method '" + this.methodNameA
				+ "' whose type is '" + this.classNameB + "'";
	}

}