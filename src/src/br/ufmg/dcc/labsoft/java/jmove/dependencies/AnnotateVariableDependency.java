package br.ufmg.dcc.labsoft.java.jmove.dependencies;



public final class AnnotateVariableDependency extends AnnotateDependency {
	private final String variableNameA;
	private final String methodNameA;
	
	public AnnotateVariableDependency(String classNameA, String classNameB, String methodNameA, String variableNameA) {
		super(classNameA,classNameB);
		this.methodNameA = methodNameA;
		this.variableNameA = variableNameA;
	}

	public String getVariableNameA() {
		return this.variableNameA;
	}
	
	public String getMethodNameA() {
		return this.methodNameA;
	}
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' contains the variable '" + this.variableNameA + 
				"' in method '" + this.methodNameA + "' which is annotated by '" + this.classNameB + "'";
	}
	
}