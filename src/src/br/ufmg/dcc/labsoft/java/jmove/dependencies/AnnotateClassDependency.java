package br.ufmg.dcc.labsoft.java.jmove.dependencies;

public final class AnnotateClassDependency extends AnnotateDependency {
	
	public AnnotateClassDependency(String classNameA, String classNameB) {
		super(classNameA,classNameB);
	}
	
	@Override
	public String toString() {
		return "'" + 
				this.classNameA + "' is annotated by '" + 
				this.classNameB + "'";
	}
	
}