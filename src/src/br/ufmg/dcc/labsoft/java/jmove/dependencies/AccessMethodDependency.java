package br.ufmg.dcc.labsoft.java.jmove.dependencies;

import org.eclipse.jdt.core.IMethod;

public final class AccessMethodDependency extends AccessDependency {
	private final String methodNameA;
	private final String methodNameB;
	private final IMethod ImethodA;
	private final IMethod ImethodB;
	private final boolean staticAccess;

	public AccessMethodDependency(String classNameA, String classNameB,
			String methodNameA, String methodNameB, IMethod ImethodA,
			IMethod ImethodB, boolean staticAccess) {
		super(classNameA, classNameB);
		this.methodNameA = methodNameA;
		this.methodNameB = methodNameB;
		this.staticAccess = staticAccess;
		this.ImethodA = ImethodA;
		this.ImethodB = ImethodB;
	}

	public String getMethodNameA() {
		return this.methodNameA;
	}

	public String getMethodNameB() {
		return this.methodNameB;
	}

	public boolean isStaticInvoke() {
		return this.staticAccess;
	}

	public IMethod getImethodA() {
		return ImethodA;
	}

	public IMethod getImethodB() {
		return ImethodB;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the method '"
				+ this.methodNameA + "' that "
				+ ((staticAccess) ? "statically " : "")
				+ "invokes the method '" + this.methodNameB
				+ "' of an object of '" + this.classNameB + "'";
	}

}