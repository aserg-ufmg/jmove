package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.IVariableBinding;

public final class AccessFieldDependency extends AccessDependency {
	private final String methodNameA;
	private final String fieldNameB;
	private final IMethod ImethodA;
	private final IVariableBinding iVariableBinding;
	private final boolean staticAccess;

	public AccessFieldDependency(String classNameA, String classNameB,
			String methodName, String fieldName, IMethod ImethodA,
			IVariableBinding iVariableBinding, boolean staticAccess) {
		super(classNameA, classNameB);
		this.methodNameA = methodName;
		this.fieldNameB = fieldName;
		this.staticAccess = staticAccess;
		this.ImethodA = ImethodA;
		this.iVariableBinding = iVariableBinding;
	}

	public String getMethodName() {
		return this.methodNameA;
	}

	public String getFieldName() {
		return this.fieldNameB;
	}

	public boolean isStaticAccess() {
		return this.staticAccess;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	public IVariableBinding getiVariableBinding() {
		return iVariableBinding;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the method '"
				+ this.methodNameA + "' that accesses "
				+ ((this.staticAccess) ? "statically " : "") + "the field '"
				+ this.fieldNameB + "' of an object of '" + this.classNameB
				+ "'";
	}

}