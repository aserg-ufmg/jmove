package br.ufmg.dcc.labsoft.java.jmove.dependencies;



public final class DeclareParameterizedTypeDependency extends DeclareDependency {
	private final String methodNameA;
	
	public DeclareParameterizedTypeDependency(String classNameA, String classNameB) {
		super(classNameA, classNameB);
		this.methodNameA = null;
	}
	
	public DeclareParameterizedTypeDependency(String classNameA, String classNameB, String methodNameA) {
		super(classNameA, classNameB);
		this.methodNameA = methodNameA;
	}
	
	public String getMethodNameA() {
		return this.methodNameA;
	}

	@Override
	public String toString() {
		return "'" + this.classNameA + "' contains the parameterized type '" + this.classNameB + "'"
				+ ((methodNameA!=null) ? " (inside '"+ this.methodNameA + "'" : "") ;
	}
	
}